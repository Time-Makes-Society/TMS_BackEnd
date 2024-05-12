# from flask import Flask, jsonify, request
# import pymysql
# import numpy as np
# import json
# from scipy.spatial.distance import cosine

# app = Flask(__name__)

# # MySQL 연결 설정
# try:
#     # MySQL 연결 설정
#     db = pymysql.connect(
#         host="localhost",
#         port=3306,
#         user="root",
#         password="Tmdcks6502@",
#         db="TMSDB",
#         charset="utf8",
#     )
#     print("Database connected successfully!")

# except Exception as e:
#     print("Error connecting to the database:", e)


# # 유사한 기사 추천 함수
# import time


# # 유사한 기사 추천 함수
# def recommend_similar_articles(base_embedding):
#     similar_articles = []
#     cursor = db.cursor()
#     cursor.execute("SELECT uuid_id, title, embedding FROM TMSDB.news")
#     articles = cursor.fetchall()
#     start_time = time.time()  # 시작 시간 기록
#     for article in articles:
#         distance = cosine(json.loads(base_embedding), json.loads(article[2]))
#         similarity = 1 - distance
#         if similarity < 1.0:  # 유사도가 1.0이 아닌 경우만 추가
#             similar_articles.append(
#                 (article[1], similarity)
#             )  # title, similarity를 함께 추가
#         if time.time() - start_time > 3:  # 최대 3초까지만 처리
#             break
#     if len(similar_articles) > 0:
#         similar_articles.sort(key=lambda x: x[1], reverse=True)
#         return similar_articles[:5]  # 상위 5개 기사만 반환
#     else:
#         # 유사한 기사가 없으면 현재 데이터 중에서 가장 유사한 기사 반환
#         most_similar_article = max(
#             articles, key=lambda x: cosine(json.loads(base_embedding), json.loads(x[2]))
#         )
#         return [(most_similar_article[1], 1.0)]  # title, similarity를 함께 반환


# # 유사한 기사 추천 엔드포인트
# @app.route("/similarity/<uuid>", methods=["GET"])
# def get_similar_articles(uuid):
#     cursor = db.cursor()

#     cursor.execute(
#         "SELECT embedding FROM TMSDB.news WHERE uuid_id = UNHEX(REPLACE(%s, '-', ''))",
#         (uuid,),
#     )

#     base_embedding = cursor.fetchone()
#     if not base_embedding:
#         return jsonify({"error": "Article not found"}), 404

#     similar_articles = recommend_similar_articles(base_embedding[0])
#     recommended = [
#         {"title": article, "similarity": float(similarity)}
#         for article, similarity in similar_articles
#     ]
#     return jsonify({"recommended_articles": recommended})


# if __name__ == "__main__":
#     app.run(debug=True, port=8082)


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


# 유사한 기사 추천 함수
def recommend_similar_articles(base_embedding):
    similar_articles = []
    cursor = db.cursor()
    cursor.execute("SELECT uuid_id, title, embedding FROM TMSDB.news")
    articles = cursor.fetchall()
    start_time = time.time()  # 시작 시간 기록
    for article in articles:
        distance = cosine(json.loads(base_embedding), json.loads(article[2]))
        similarity = 1 - distance
        if similarity < 1.0:  # 유사도가 1.0이 아닌 경우만 추가
            similar_articles.append(
                (article[1], similarity)
            )  # title, similarity를 함께 추가
        if time.time() - start_time > 3:  # 최대 3초까지만 처리
            break
    if len(similar_articles) > 0:
        similar_articles.sort(key=lambda x: x[1], reverse=True)
        return similar_articles[:5]  # 상위 5개 기사만 반환
    else:
        # 유사한 기사가 없으면 현재 데이터 중에서 가장 유사한 기사 반환
        # most_similar_article = max(
        #     articles, key=lambda x: cosine(json.loads(base_embedding), json.loads(x[2]))
        # )
        most_similar_article = max(
            articles,
            key=lambda x: cosine(
                json.loads(base_embedding), np.array(json.loads(x[2]))
            ),
        )
        return [(most_similar_article[1], 1.0)]  # title, similarity를 함께 반환


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
