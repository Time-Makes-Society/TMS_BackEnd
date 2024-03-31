from flask import Flask, request, jsonify
from newspaper import Article
import pymysql
import requests

app = Flask(__name__)

# MySQL 연결 설정
db = pymysql.connect(
    host="ec2-3-39-185-190.ap-northeast-2.compute.amazonaws.com",
    port=3306,
    user="kscold",
    password="Tmdcks6502@",
    db="TMSDB",
    charset="utf8",
)

# 데이터베이스 커서 생성
cursor = db.cursor()

# 테이블 생성 쿼리
create_table_query = """
CREATE TABLE IF NOT EXISTS news (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    createdDate VARCHAR(255),
    category VARCHAR(255),
    image VARCHAR(255), 
    link VARCHAR(255)
)
"""

# 테이블 생성 쿼리 실행
cursor.execute(create_table_query)
db.commit()


# 테이블 생성 쿼리 실행
cursor.execute(create_table_query)
db.commit()

# 네이버 API 요청 헤더 정보
headers = {
    "X-Naver-Client-Id": "24tVb8KYxigJdaJ_mh7W",
    "X-Naver-Client-Secret": "jvjDit0K83",
}


@app.route("/newssave/<keyword>", methods=["GET"])
def save_news(keyword):
    # 요청으로부터 키워드 받기
    query = keyword

    # 네이버 API로 뉴스 검색 요청 보내기
    # 문화 : 문화, 경제 : 경제, 연예 : 연예, 정치 : 정치, 과학 : 일반 과학, 사회 : 사회, 스포츠 : 스포츠 경기, 기술 : IT테크기술, 뉴스
    url = f"https://openapi.naver.com/v1/search/news.json?query={query}&sort=date"
    response = requests.get(url, headers=headers)
    data = response.json()

    # 검색 결과에서 새로운 기사만 가져와서 저장
    if "items" in data:
        news_items = data["items"]
        for item in news_items:
            title = item.get("title", "")
            original_link = item.get("originallink", "")
            pubDate = item.get("pubDate", "")

            # 기사 내용 가져오기
            try:
                article = Article(original_link)
                article.download()
                article.parse()
                content = article.text
                image_url = (
                    article.top_image if article.top_image else ""
                )  # 이미지 URL 가져오기
            except Exception as e:
                print("기사 내용을 가져오는 중 오류 발생:", e)
                content = ""
                image_url = ""

            # SQL query 작성
            sql = "INSERT INTO news (title, content, createdDate, category, image, link) VALUES (%s, %s, %s, %s, %s, %s)"
            val = (title, content, pubDate, query, image_url, original_link)

            try:
                # SQL query 실행
                cursor.execute(sql, val)
                # db 데이터 수정 사항 저장
                db.commit()
                print(f"새로운 기사를 저장했습니다: {title}")
            except Exception as e:
                print("기사를 저장하는 중 오류 발생:", e)

        return jsonify({"message": "뉴스 기사 저장 완료"})
    else:
        return jsonify({"message": "뉴스 기사를 찾을 수 없습니다."})


@app.route("/news/<keyword>", methods=["GET"])
def get_news(keyword):
    # 요청으로부터 키워드 받기
    query = keyword

    # SQL query 작성
    sql = f"SELECT * FROM news WHERE title LIKE '%{query}%'"

    # SQL query 실행
    cursor.execute(sql)

    # 검색 결과 가져오기
    news_articles = cursor.fetchall()

    if news_articles:
        return jsonify(news_articles)
    else:
        return jsonify({"message": "해당 키워드에 대한 뉴스 기사를 찾을 수 없습니다."})


if __name__ == "__main__":
    app.run(debug=True, port=8081)
