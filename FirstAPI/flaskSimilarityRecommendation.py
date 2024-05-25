from flask import Flask, jsonify, request
import pymysql
import json
from scipy.spatial.distance import cosine
import logging

app = Flask(__name__)

# Set up logging
logging.basicConfig(level=logging.DEBUG)


def get_db_connection():
    try:
        connection = pymysql.connect(
            host="localhost",
            port=3306,
            user="root",
            password="Tmdcks6502@",
            db="TMSDB",
            charset="utf8",
        )
        logging.info("Database connection established")
        return connection
    except Exception as e:
        logging.error(f"Error connecting to the database: {e}")
        raise


def recommend_similar_articles(db, base_embedding):
    similar_articles = []
    cursor = db.cursor()
    try:
        cursor.execute("SELECT uuid_id, title, embedding FROM TMSDB.news")
        articles = cursor.fetchall()
        logging.info("Fetched articles from the database")

        if not base_embedding:
            logging.warning("No base embedding found")
            return [("No base embedding found", 0.0)]

        base_embedding_vector = json.loads(base_embedding)
        if not base_embedding_vector:
            logging.warning("Invalid base embedding")
            return [("Invalid base embedding", 0.0)]

        for article in articles:
            article_embedding_json = article[2]
            if article_embedding_json:
                try:
                    article_embedding_vector = json.loads(article_embedding_json)
                    distance = cosine(base_embedding_vector, article_embedding_vector)
                    similarity = 1 - distance
                    if similarity < 1.0:
                        similar_articles.append((article[1], similarity))
                except (json.JSONDecodeError, TypeError) as e:
                    logging.error(
                        f"Error decoding JSON or calculating similarity for article {article[0]}: {e}"
                    )

        if similar_articles:
            similar_articles.sort(key=lambda x: x[1], reverse=True)
            return similar_articles[:5]
        else:
            logging.info("No similar articles found")
            return [("No similar articles found", 0.0)]
    finally:
        cursor.close()


def generate_uuid_with_dashes(uuid_str):
    return "-".join(
        [uuid_str[:8], uuid_str[8:12], uuid_str[12:16], uuid_str[16:20], uuid_str[20:]]
    )


@app.route("/similarity/<uuid>", methods=["GET"])
def get_similar_articles(uuid):
    db = get_db_connection()
    cursor = db.cursor()

    try:
        cursor.execute(
            "SELECT embedding FROM TMSDB.news WHERE uuid_id = UNHEX(REPLACE(%s, '-', ''))",
            (uuid,),
        )
        base_embedding = cursor.fetchone()
    except Exception as e:
        logging.error(f"Error executing query: {e}")
        return jsonify({"error": "Database query failed"}), 500
    finally:
        cursor.close()
        db.close()

    if not base_embedding or not base_embedding[0]:
        logging.warning("Article not found or embedding missing")
        return jsonify({"error": "Article not found or embedding missing"}), 404

    db = get_db_connection()
    similar_articles = recommend_similar_articles(db, base_embedding[0])
    recommended = []
    for title, similarity in similar_articles:
        cursor = db.cursor()
        cursor.execute("SELECT uuid_id FROM TMSDB.news WHERE title = %s", (title,))
        base_uuid = cursor.fetchone()
        cursor.close()
        if base_uuid:
            base_uuid_str = generate_uuid_with_dashes(base_uuid[0].hex())
            recommended.append(
                {"uuid": base_uuid_str, "title": title, "similarity": float(similarity)}
            )
    db.close()
    return jsonify({"recommendedArticles": recommended})


def calculate_cosine_similarity(embedding1, embedding2):
    try:
        vector1 = json.loads(embedding1)
        vector2 = json.loads(embedding2)
        distance = cosine(vector1, vector2)
        similarity = 1 - distance
        return similarity
    except (json.JSONDecodeError, TypeError) as e:
        logging.error(f"JSON deconding 또는 계산 오류: {e}")
        return None


@app.route("/article/similarity/<uuid>", methods=["GET"])
def get_similarity(uuid):
    db = get_db_connection()
    cursor = db.cursor()

    try:
        cursor.execute(
            "SELECT contentEmbedding, gptContentEmbedding FROM TMSDB.news WHERE uuid_id = UNHEX(REPLACE(%s, '-', ''))",
            (uuid,),
        )
        embeddings = cursor.fetchone()
    except Exception as e:
        logging.error(f"쿼리 실행 오류: {e}")
        return jsonify({"error": "쿼리 실행 오류"}), 500
    finally:
        cursor.close()
        db.close()

    if not embeddings or not embeddings[0] or not embeddings[1]:
        logging.warning("임베딩 값이 없습니다.")
        return jsonify({"error": "임베딩 값이 없습니다."}), 404

    similarity = calculate_cosine_similarity(embeddings[0], embeddings[1])
    if similarity is None:
        logging.error("유사도 값 계산에 실패했습니다.")
        return jsonify({"error": "유사도 값 계산에 실패했습니다."}), 500

    return jsonify({"similarity": similarity})


if __name__ == "__main__":
    app.run(debug=True, port=8082)
