from flask import Flask, request, jsonify
from newspaper import Article
import pymysql
import requests
from datetime import datetime
from urllib.parse import urlparse
from dateutil import parser

app = Flask(__name__)

# MySQL 연결 설정
# ec2 설정
# db = pymysql.connect(
#     host="ec2-3-39-185-190.ap-northeast-2.compute.amazonaws.com",
#     port=3306,
#     user="kscold",
#     password="Tmdcks6502@",
#     db="TMSDB",
#     charset="utf8",
# )
# MySQL 로컬 DB로 설정
# db = pymysql.connect(
#     host="localhost",
#     port=3306,
#     user="root",
#     password="3516",
#     db="tms",
#     charset="utf8",
# )

db = pymysql.connect(
    host="localhost",
    port=3306,
    user="root",
    password="Tmdcks6502@",
    db="TMSDB",
    charset="utf8",
)

# 데이터베이스 커서 생성
cursor = db.cursor()

# 테이블 생성 쿼리
create_table_query = """
CREATE TABLE IF NOT EXISTS pre_news (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    createdDate DATETIME,
    category VARCHAR(255),
    image VARCHAR(255), 
    link VARCHAR(255),
    publisher VARCHAR(255)
)
"""

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

    # 카테고리 매칭
    category_mapping = {
        "문화": "문화뉴스",
        "경제": "경제뉴스",
        "연예": "연예뉴스",
        "정치": "정치",
        "과학": "과학",
        "사회": "사회뉴스",
        "스포츠": "스포츠경기",
        "기술": "테크뉴스",
        "해외": "해외뉴스",
    }

    if query in category_mapping:
        category = category_mapping[query]
    else:
        return jsonify({"message": "해당 카테고리는 지원되지 않습니다."})

    # 네이버 API로 뉴스 검색 요청 보내기
    url = f"https://openapi.naver.com/v1/search/news.json?display=10&query={category}&sort=date"
    response = requests.get(url, headers=headers)
    data = response.json()

    # 검색 결과에서 새로운 기사만 가져와서 저장
    if "items" in data:
        news_items = data["items"]
        for item in news_items:
            title = item.get("title", "").replace("&quot;", '"')  # &quot;를 "로 대체
            original_link = item.get("originallink", "")
            pubDate = item.get("pubDate", "")

            # 중복 기사인지 확인
            if is_duplicate(title):
                print(f"{title}은 이미 존재하는 기사입니다. 건너뜁니다.")
                continue

            # 기사 내용 가져오기
            try:
                article = Article(original_link)
                article.download()
                article.parse()
                content = article.text
                # 이미지 URL 가져오기
                image_url = article.top_image if article.top_image else ""
                # 기사 내용이 비어있으면 건너뜀
                if not content.strip():
                    print("기사 내용이 비어있습니다. 건너뜁니다.")
                    continue
            except Exception as e:
                print("기사 내용을 가져오는 중 오류 발생:", e)
                continue

            # pubDate를 LocalDateTime으로 변환
            createdDate = parser.parse(pubDate).strftime("%Y-%m-%d %H:%M:%S")

            # SQL query 작성
            # sql = "INSERT INTO pre_news (title, content, createdDate, category, image, link) VALUES (%s, %s, %s, %s, %s, %s)"
            # val = (title, content, createdDate, query, image_url, original_link)

            # newspaper 라이브러리에서 언론사 정보 가져오기
            parsed_url = urlparse(article.source_url)
            domain_parts = parsed_url.netloc.replace("www.", "").split(".")
            publisher = (
                domain_parts[0] if parsed_url.netloc and len(domain_parts) > 1 else ""
            )

            # SQL query 작성
            sql = "INSERT INTO pre_news (title, content, createdDate, category, image, link, publisher) VALUES (%s, %s, %s, %s, %s, %s, %s)"
            val = (
                title,
                content,
                createdDate,
                query,
                image_url,
                original_link,
                publisher,
            )

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


def is_duplicate(title):
    # 기사 제목이 이미 존재하는지 확인
    escaped_title = title.replace("'", "''")  # 작은따옴표를 이스케이프 처리
    sql = f"SELECT COUNT(*) FROM news WHERE title = '{escaped_title}'"
    cursor.execute(sql)
    count = cursor.fetchone()[0]
    return count > 0


@app.route("/news/<keyword>", methods=["GET"])
def get_news(keyword):
    # 요청으로부터 키워드 받기
    query = keyword

    # SQL query 작성
    sql = f"SELECT * FROM news WHERE category LIKE '%{query}%'"

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
