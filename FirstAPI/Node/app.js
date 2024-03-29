// // app.js
// const express = require("express")
// const mongoose = require("mongoose")
// const NewsAPI = require("newsapi")

// const app = express()
// const newsapi = new NewsAPI("c77f5337872a4688a1ebd82fca07dc98")

// // MongoDB Atlas 클러스터 연결 URI
// const MONGODB_URI =
//   "mongodb+srv://ks_cold:Tmdcks6502%40@practicedb.rf3w9v5.mongodb.net/"

// // MongoDB 연결
// mongoose
//   .connect(MONGODB_URI, {
//     useNewUrlParser: true,
//     useUnifiedTopology: true,
//   })
//   .then(() => console.log("MongoDB connected"))
//   .catch((err) => console.error("MongoDB connection error:", err))

// // MongoDB 스키마 정의
// const newsSchema = new mongoose.Schema({
//   category: String,
//   title: String,
//   description: String,
//   url: String,
//   urlToImage: String,
//   publishedAt: Date,
// })

// const News = mongoose.model("News", newsSchema)

// app.get("/news/:category", async (req, res) => {
//   const category = req.params.category
//   const validCategories = [
//     "business",
//     "entertainment",
//     "general",
//     "health",
//     "science",
//     "sports",
//     "technology",
//   ]

//   if (!validCategories.includes(category)) {
//     return res.status(400).json({ error: "Invalid category" })
//   }

//   try {
//     // 뉴스 API로부터 데이터 가져오기
//     const response = await newsapi.v2.topHeadlines({
//       category: category,
//       language: "ko",
//       country: "kr",
//     })

//     // MongoDB에 저장된 기사 중에서 제목이 중복되지 않는 것만 필터링하여 가져오기
//     const savedTitles = await News.distinct("title", { category: category })

//     // 새로운 기사만 필터링하여 저장할 데이터 준비
//     const newArticles = response.articles.filter(
//       (article) => !savedTitles.includes(article.title)
//     )

//     // 새로운 기사만 MongoDB에 저장
//     if (newArticles.length > 0) {
//       const formattedArticles = newArticles.map((article) => {
//         return {
//           category: category,
//           title: article.title,
//           description: article.description,
//           url: article.url,
//           urlToImage: article.urlToImage,
//           publishedAt: article.publishedAt,
//         }
//       })

//       const savedNewArticles = await News.insertMany(formattedArticles)
//       res.json(savedNewArticles)
//     } else {
//       res.json({ message: "No new articles to save" })
//     }
//   } catch (error) {
//     res.status(500).json({ error: "Internal server error" })
//   }
// })

// const PORT = process.env.PORT || 8080
// app.listen(PORT, () => console.log(`Server is running on port ${PORT}`))

const express = require("express")
const mongoose = require("mongoose")
const NewsAPI = require("newsapi")
const cors = require("cors") // cors 미들웨어 추가

const app = express()
const newsapi = new NewsAPI("c77f5337872a4688a1ebd82fca07dc98")

// CORS 설정
app.use(cors())

// MongoDB Atlas 클러스터 연결 URI
const MONGODB_URI =
  "mongodb+srv://ks_cold:Tmdcks6502%40@practicedb.rf3w9v5.mongodb.net/"

// MongoDB 연결
mongoose
  .connect(MONGODB_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => console.log("MongoDB connected"))
  .catch((err) => console.error("MongoDB connection error:", err))

// MongoDB 스키마 정의
const newsSchema = new mongoose.Schema({
  category: String,
  title: String,
  description: {
    type: mongoose.Schema.Types.String,
    maxlength: 10000, // 적절한 최대 길이로 조정하세요
  },
  url: String,
  urlToImage: String,
  publishedAt: Date,
})

const News = mongoose.model("News", newsSchema)

app.get("/news/:category", async (req, res) => {
  const category = req.params.category
  const validCategories = [
    "business",
    "entertainment",
    "general",
    "health",
    "science",
    "sports",
    "technology",
  ]

  if (!validCategories.includes(category)) {
    return res.status(400).json({ error: "Invalid category" })
  }

  try {
    // 뉴스 API로부터 데이터 가져오기
    const response = await newsapi.v2.topHeadlines({
      category: category,
      language: "ko",
      country: "kr",
    })

    console.log(response)

    // MongoDB에 저장된 기사 중에서 제목이 중복되지 않는 것만 필터링하여 가져오기
    const savedTitles = await News.distinct("title", { category: category })

    // 새로운 기사만 필터링하여 저장할 데이터 준비
    const newArticles = response.articles.filter(
      (article) => !savedTitles.includes(article.title)
    )

    // 새로운 기사만 MongoDB에 저장
    if (newArticles.length > 0) {
      const formattedArticles = newArticles.map((article) => {
        return {
          category: category,
          title: article.title,
          description: article.description,
          url: article.url,
          urlToImage: article.urlToImage,
          publishedAt: article.publishedAt,
        }
      })

      const savedNewArticles = await News.insertMany(formattedArticles)
      res.json(savedNewArticles)
    } else {
      res.json({ message: "No new articles to save" })
    }
  } catch (error) {
    res.status(500).json({ error: "Internal server error" })
  }
})

app.get("/getnews/:category", async (req, res) => {
  const category = req.params.category
  const validCategories = [
    "business",
    "entertainment",
    "general",
    "health",
    "science",
    "sports",
    "technology",
  ]

  if (!validCategories.includes(category)) {
    return res.status(400).json({ error: "Invalid category" })
  }

  try {
    // 해당 카테고리에 해당하는 모든 기사를 조회
    const articles = await News.find({ category: category })

    if (articles.length > 0) {
      res.json(articles)
    } else {
      res.json({ message: "No articles found for this category" })
    }
  } catch (error) {
    res.status(500).json({ error: "Internal server error" })
  }
})

const PORT = process.env.PORT || 8080
app.listen(PORT, () => console.log(`Server is running on port ${PORT}`))
