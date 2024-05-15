from flask import Flask, jsonify, request
import pymysql
import numpy as np
import json
from scipy.spatial.distance import cosine
import time
import uuid

app = Flask(__name__)

# MySQL 연결 설정
try:
    # MySQL 연결 설정
    db = pymysql.connect(
        host="localhost",
        port=3306,
        user="root",
        password="Tmdcks6502@",
        db="TMSDB",
        charset="utf8",
    )
    print("Database connected successfully!")

except Exception as e:
    print("Error connecting to the database:", e)


def recommend_similar_articles(base_embedding):
    similar_articles = []
    cursor = db.cursor()
    cursor.execute("SELECT uuid_id, title, embedding FROM TMSDB.news")
    articles = cursor.fetchall()
    if not base_embedding:
        return [("No base embedding found", 0.0)]  # 기본 임베딩이 없는 경우
    for article in articles:
        if article[2]:  # 임베딩이 있는 경우에만 처리
            distance = cosine(json.loads(base_embedding), json.loads(article[2]))
            similarity = 1 - distance
            if similarity < 1.0:  # 유사도가 1.0이 아닌 경우만 추가
                similar_articles.append(
                    (article[1], similarity)
                )  # title, similarity를 함께 추가
    if similar_articles:
        similar_articles.sort(key=lambda x: x[1], reverse=True)
        return similar_articles[:5]  # 상위 5개 기사만 반환
    else:
        return [("No similar articles found", 0.0)]  # 유사한 기사를 찾을 수 없는 경우


# UUID 문자열을 "-"를 포함하여 생성하는 함수
def generate_uuid_with_dashes(uuid_str):
    return "-".join(
        [uuid_str[:8], uuid_str[8:12], uuid_str[12:16], uuid_str[16:20], uuid_str[20:]]
    )


# 유사한 기사 추천 엔드포인트
@app.route("/similarity/<uuid>", methods=["GET"])
def get_similar_articles(uuid):
    cursor = db.cursor()

    cursor.execute(
        "SELECT embedding FROM TMSDB.news WHERE uuid_id = UNHEX(REPLACE(%s, '-', ''))",
        (uuid,),
    )

    base_embedding = cursor.fetchone()
    if not base_embedding:
        return jsonify({"error": "Article not found"}), 404

    similar_articles = recommend_similar_articles(base_embedding[0])
    recommended = []
    for title, similarity in similar_articles:
        cursor.execute("SELECT uuid_id FROM TMSDB.news WHERE title = %s", (title,))
        base_uuid = cursor.fetchone()
        base_uuid_str = generate_uuid_with_dashes(base_uuid[0].hex())
        recommended.append(
            {"uuid": base_uuid_str, "title": title, "similarity": float(similarity)}
        )
    return jsonify({"recommendedArticles": recommended})


if __name__ == "__main__":
    app.run(debug=True, port=8082)
