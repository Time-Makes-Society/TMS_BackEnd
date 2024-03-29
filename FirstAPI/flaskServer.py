from flask import Flask, request, jsonify
import requests
from newspaper import Article
from pymongo import MongoClient

app = Flask(__name__)

try:
    client = MongoClient(
        "mongodb+srv://ks_cold:Tmdcks6502%40@practicedb.rf3w9v5.mongodb.net/"
    )
    db = client["TMS"]
    collection = db["news"]
    print("MongoDB 클라이언트가 성공적으로 연결되었습니다.")
except Exception as e:
    print("MongoDB 클라이언트 연결 중 오류 발생:", e)

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

            # 데이터베이스에서 해당 제목의 기사가 이미 존재하는지 확인
            existing_article = collection.find_one({"title": title})
            if existing_article:
                print(f"이미 존재하는 기사입니다: {title}")
                continue

            try:
                article = Article(original_link, language="ko")
                article.download()
                article.parse()

                # 기사 내용 및 이미지 가져오기
                content = article.text
                image_url = article.top_image if article.top_image else ""

                # 데이터베이스에 저장
                collection.insert_one(
                    {
                        "title": title,
                        "original_link": original_link,
                        "pubDate": pubDate,
                        "content": content,
                        "image_url": image_url,
                        "category": query,  # 카테고리로 설정
                    }
                )
                print(f"새로운 기사를 저장했습니다: {title}")
            except Exception as e:
                print("기사 내용을 가져오는 중 오류 발생:", e)

        return jsonify({"message": "뉴스 기사 저장 완료"})
    else:
        return jsonify({"message": "뉴스 기사를 찾을 수 없습니다."})


@app.route("/news/<keyword>", methods=["GET"])
def get_news(keyword):
    # 요청으로부터 키워드 받기
    query = keyword

    # 데이터베이스에서 해당 키워드(카테고리)에 대한 뉴스 기사 조회
    news_articles = list(
        collection.find({"category": query}, {"_id": 0})
    )  # '_id' 필드 제거

    if news_articles:
        return jsonify(news_articles)
    else:
        return jsonify({"message": "해당 키워드에 대한 뉴스 기사를 찾을 수 없습니다."})


if __name__ == "__main__":
    app.run(debug=True, port=8081)
