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
    content LONGTEXT,
    createdDate VARCHAR(255),
    category VARCHAR(255),
    image VARCHAR(255),
    link VARCHAR(255)
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
    url = f"https://openapi.naver.com/v1/search/news.json?display=30&query={category}&sort=date"
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


# from flask import Flask, jsonify
# import requests
# from newspaper import Article
# from sqlalchemy import create_engine, Column, Integer, String, Text
# from sqlalchemy.ext.declarative import declarative_base
# from sqlalchemy.orm import sessionmaker
# from sqlalchemy.types import CLOB
# from sqlalchemy.ext.compiler import compiles

# app = Flask(__name__)

# # SQLAlchemy 엔진 생성
# engine = create_engine(
#     "mysql+pymysql://kscold:Tmdcks6502%40@ec2-3-39-185-190.ap-northeast-2.compute.amazonaws.com:3306/TMSDB",
# )

# Base = declarative_base()
# Session = sessionmaker(bind=engine)


# # 데이터베이스 모델 정의
# class News(Base):
#     __tablename__ = "news"

#     id = Column(Integer, primary_key=True)
#     title = Column(String(255))
#     content = Column(Text)  # Change the type to CLOB
#     createdDate = Column(String(255))
#     category = Column(String(255))
#     image = Column(String(255))
#     link = Column(String(255))


# # 커스텀 컴파일러 정의
# @compiles(Text, "mysql")
# def compile_binary_mysql(type_, compiler, **kw):
#     return "LONGTEXT"


# # 테이블 생성
# Base.metadata.create_all(engine)

# # 네이버 API 요청 헤더 정보
# headers = {
#     "X-Naver-Client-Id": "24tVb8KYxigJdaJ_mh7W",
#     "X-Naver-Client-Secret": "jvjDit0K83",
# }

# # SQLAlchemy 세션 생성
# session = Session()


# @app.route("/newssave/<keyword>", methods=["GET"])
# def save_news(keyword):
#     # 요청으로부터 키워드 받기
#     query = keyword

#     # 카테고리 매칭
#     category_mapping = {
#         "문화": "문화뉴스",
#         "경제": "경제뉴스",
#         "연예": "연예뉴스",
#         "정치": "정치",
#         "과학": "과학",
#         "사회": "사회뉴스",
#         "스포츠": "스포츠경기",
#         "기술": "테크뉴스",
#         "해외": "해외뉴스",
#     }

#     if query in category_mapping:
#         category = category_mapping[query]
#     else:
#         return jsonify({"message": "해당 카테고리는 지원되지 않습니다."})

#     # 네이버 API로 뉴스 검색 요청 보내기
#     url = f"https://openapi.naver.com/v1/search/news.json?display=30&query={category}&sort=date"
#     response = requests.get(url, headers=headers)
#     data = response.json()

#     # 검색 결과에서 새로운 기사만 가져와서 저장
#     if "items" in data:
#         news_items = data["items"]
#         for item in news_items:
#             title = item.get("title", "").replace("&quot;", '"')  # &quot;를 "로 대체
#             original_link = item.get("originallink", "")
#             pubDate = item.get("pubDate", "")

#             # 중복 기사인지 확인
#             if is_duplicate(title):
#                 print(f"{title}은 이미 존재하는 기사입니다. 건너뜁니다.")
#                 continue

#             # 기사 내용 가져오기
#             try:
#                 article = Article(original_link)
#                 article.download()
#                 article.parse()
#                 content = article.text  # 이제 인코딩할 필요 없음

#                 # 기사 내용이 너무 길면 일부만 사용
#                 max_content_length = 65532  # MySQL의 TEXT 컬럼 최대 길이
#                 if len(content) > max_content_length:
#                     content = content[:max_content_length]

#                 # 이미지 URL 가져오기
#                 image_url = article.top_image if article.top_image else ""
#                 # 기사 내용이 비어있으면 건너뜀
#                 if not content.strip():
#                     print("기사 내용이 비어있습니다. 건너뜁니다.")
#                     continue
#             except Exception as e:
#                 print("기사 내용을 가져오는 중 오류 발생:", e)
#                 continue

#             # 데이터베이스에 추가
#             news = News(
#                 title=title,
#                 content=content,
#                 createdDate=pubDate,
#                 category=query,
#                 image=image_url,
#                 link=original_link,
#             )
#             session.add(news)
#             print(f"{title} 기사를 데이터베이스에 추가했습니다.")

#         # 변경사항 저장
#         session.commit()

#         return jsonify({"message": "뉴스가 성공적으로 저장되었습니다."})
#     else:
#         return jsonify({"message": "검색 결과가 없습니다."})


# def is_duplicate(title):
#     """제목이 중복되는지 확인하는 함수"""
#     existing_news = session.query(News).filter(News.title == title).first()
#     return existing_news is not None


# if __name__ == "__main__":
#     app.run(debug=True, port=8081)
