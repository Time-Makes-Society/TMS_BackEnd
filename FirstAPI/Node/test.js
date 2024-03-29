// // // app.js
// // const express = require("express")
// // const mongoose = require("mongoose")
// // const axios = require("axios")
// // const cheerio = require("cheerio")

// // const app = express()

// // // MongoDB Atlas 클러스터 연결 URI
// // const MONGODB_URI =
// //   "mongodb+srv://ks_cold:Tmdcks6502%40@practicedb.rf3w9v5.mongodb.net/"

// // // MongoDB 연결
// // // mongoose
// // //   .connect(MONGODB_URI, {
// // //     useNewUrlParser: true,
// // //     useUnifiedTopology: true,
// // //   })
// // //   .then(() => console.log("MongoDB connected"))
// // //   .catch((err) => console.error("MongoDB connection error:", err))

// // mongoose
// //   .connect(MONGODB_URI, {
// //     useNewUrlParser: true,
// //     useUnifiedTopology: true,
// //     socketTimeoutMS: 3000000, // 30 seconds
// //     connectTimeoutMS: 3000000, // 30 seconds
// //   })
// //   .then(() => console.log("MongoDB connected"))
// //   .catch((err) => console.error("MongoDB connection error:", err))

// // // MongoDB 스키마 정의
// // const newsSchema = new mongoose.Schema({
// //   category: String,
// //   title: String,
// //   description: String,
// //   url: String,
// //   urlToImage: String,
// //   publishedAt: Date,
// // })

// // const News = mongoose.model("News", newsSchema)

// // // sleep 함수 정의
// // function sleep(ms) {
// //   return new Promise((resolve) => setTimeout(resolve, ms))
// // }

// // // 뉴스 크롤링 및 저장하는 함수
// // async function crawlAndSaveNews(keyword, lastPage) {
// //   try {
// //     let page_num = 1
// //     for (let i = 1; i <= lastPage * 10; i += 10) {
// //       console.log(`${page_num} 페이지...`)
// //       const response = await axios.get(
// //         `https://search.naver.com/search.naver?where=news&sm=tab_jum&query=${encodeURIComponent(
// //           keyword
// //         )}&start=${i}`
// //       )
// //       const html = response.data
// //       const $ = cheerio.load(html)
// //       const articles = $("div.info_group")

// //       articles.each(async (index, element) => {
// //         const links = $(element).find("a.info")
// //         if (links.length >= 2) {
// //           const url = links.eq(1).attr("href")
// //           try {
// //             const response = await axios.get(url, {
// //               headers: { "User-Agent": "Mozilla/5.0" },
// //             })
// //             const html = response.data
// //             const $ = cheerio.load(html)
// //             let title, content

// //             // 분리
// //             if (response.request.res.responseUrl.includes("entertain")) {
// //               title = $(".end_tit").text().trim()
// //               content = $("#articeBody").text().trim()
// //             } else if (response.request.res.responseUrl.includes("sports")) {
// //               title = $("h4.title").text().trim()
// //               content = $("#newsEndContents").text().trim()
// //             } else {
// //               title = $(".media_end_head_headline").text().trim()
// //               content = $("#dic_area").text().trim()
// //             }

// //             // 데이터 저장
// //             const news = new News({
// //               category: keyword,
// //               title: title,
// //               description: content,
// //               url: url,
// //             })
// //             await news.save()
// //             console.log("데이터 저장 완료:", news)
// //           } catch (error) {
// //             console.error(error)
// //           }
// //         }
// //       })

// //       await sleep(300) // 0.3초 지연
// //       page_num++
// //     }
// //   } catch (error) {
// //     console.error(error)
// //   }
// // }

// // // 뉴스 저장 API 엔드포인트
// // app.get("/news/:category", async (req, res) => {
// //   const category = req.params.category
// //   const validCategories = [
// //     "business",
// //     "entertainment",
// //     "general",
// //     "health",
// //     "science",
// //     "sports",
// //     "technology",
// //   ]

// //   if (!validCategories.includes(category)) {
// //     return res.status(400).json({ error: "Invalid category" })
// //   }

// //   const keyword = category
// //   const lastPage = 2 // 예시로 마지막 페이지를 2로 설정

// //   try {
// //     await crawlAndSaveNews(keyword, lastPage)
// //     res.json({ message: "뉴스 저장 완료" })
// //   } catch (error) {
// //     res.status(500).json({ error: "Internal server error" })
// //   }
// // })

// // const PORT = process.env.PORT || 8080
// // app.listen(PORT, () => console.log(`Server is running on port ${PORT}`))

// const express = require("express")
// const mongoose = require("mongoose")
// const axios = require("axios")
// const cheerio = require("cheerio")

// const app = express()

// // MongoDB Atlas 클러스터 연결 URI
// const MONGODB_URI =
//   "mongodb+srv://ks_cold:Tmdcks6502%40@practicedb.rf3w9v5.mongodb.net/"

// // MongoDB 연결
// mongoose
//   .connect(MONGODB_URI, {
//     useNewUrlParser: true,
//     useUnifiedTopology: true,
//     socketTimeoutMS: 3000000, // 30 seconds
//     connectTimeoutMS: 3000000, // 30 seconds
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

// // sleep 함수 정의
// function sleep(ms) {
//   return new Promise((resolve) => setTimeout(resolve, ms))
// }

// // HTML 코드를 가지고 오는 함수
// const getHTML = async (keyword) => {
//   try {
//     return await axios.get(
//       "https://search.naver.com/search.naver?where=news&ie=UTF-8&query=" +
//         encodeURI(keyword)
//     )
//   } catch (err) {
//     console.log(err)
//   }
// }

// // 파싱 함수
// const parsing = async (keyword) => {
//   const html = await getHTML(keyword)
//   const $ = cheerio.load(html.data) // 가지고 오는 data load
//   const $titlist = $(".news_area")

//   let informations = []
//   $titlist.each(async (idx, node) => {
//     const title = $(node).find(".news_tit:eq(0)").text() // 뉴스제목 크롤링
//     const press = $(node).find(".info_group > a").text() // 출판사 크롤링
//     const time = $(node).find(".info_group > span").text() // 기사 작성 시간 크롤링
//     const contents = $(node).find(".dsc_wrap").text() // 기사 내용 크롤링

//     // 데이터 저장
//     const news = new News({
//       category: keyword,
//       title: title,
//       description: contents,
//       url: "",
//     })
//     try {
//       await news.save()
//       console.log("데이터 저장 완료:", news)
//     } catch (error) {
//       console.error(error)
//     }
//   }) //for문과 동일
// }

// // 뉴스 크롤링 및 저장하는 함수
// async function crawlAndSaveNews(keyword, lastPage) {
//   try {
//     for (let i = 1; i <= lastPage * 10; i += 10) {
//       console.log(`${i / 10} 페이지...`)
//       await parsing(keyword)
//       await sleep(300) // 0.3초 지연
//     }
//   } catch (error) {
//     console.error(error)
//   }
// }

// // 뉴스 저장 API 엔드포인트
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

//   const keyword = category
//   const lastPage = 2 // 예시로 마지막 페이지를 2로 설정

//   try {
//     await crawlAndSaveNews(keyword, lastPage)
//     res.json({ message: "뉴스 저장 완료" })
//   } catch (error) {
//     res.status(500).json({ error: "Internal server error" })
//   }
// })

// const PORT = process.env.PORT || 8080
// app.listen(PORT, () => console.log(`Server is running on port ${PORT}`))

const express = require("express")
const mongoose = require("mongoose")
const axios = require("axios")
const cheerio = require("cheerio")

const app = express()
const PORT = process.env.PORT || 8080

// MongoDB 연결
mongoose.connect(
  "mongodb+srv://ks_cold:Tmdcks6502%40@practicedb.rf3w9v5.mongodb.net/",
  {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  }
)
const db = mongoose.connection
db.on("error", console.error.bind(console, "MongoDB connection error:"))
db.once("open", () => {
  console.log("Connected to MongoDB")
})

// 뉴스 정보를 저장할 MongoDB 모델 정의
const News = mongoose.model("News", {
  title: String,
  press: String,
  time: String,
  contents: String,
})

// HTML 코드를 가져오는 함수
const getHTML = async (keyword) => {
  try {
    return await axios.get(
      "https://search.naver.com/search.naver?where=news&ie=UTF-8&query=" +
        encodeURI(keyword)
    )
  } catch (err) {
    console.log(err)
  }
}

// 파싱 함수
const parsing = async (keyword) => {
  const html = await getHTML(keyword)
  const $ = cheerio.load(html.data)
  const $titlist = $(".news_area")

  let informations = []
  $titlist.each((idx, node) => {
    const title = $(node).find(".news_tit").text()
    const press = $(node).find(".info_group > a").text()
    const time = $(node).find(".info_group > span").text()
    const contents = $(node).find(".dsc_wrap").text()

    informations.push({
      title,
      press,
      time,
      contents,
    })
  })

  // MongoDB에 저장
  await News.insertMany(informations)
  console.log("News saved to MongoDB:", informations)
}

// Express 라우트
app.get("/news/:keyword", async (req, res) => {
  const { keyword } = req.params // 요청의 키워드를 가져옴
  await parsing(keyword)
  res.send("News Crawled and Saved!")
})

// 서버 시작
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`)
})
