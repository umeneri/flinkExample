//// [Scala + PlayFramework + Elasticsearch ことはじめ - Qiita](https://qiita.com/n-gondo123/items/63c067ca9ada30e240c0)
//
//package org.example
//
//import com.sksamuel.elastic4s.ElasticDsl._
//import com.sksamuel.elastic4s.http.JavaClient
//import com.sksamuel.elastic4s.requests.searches.SearchBodyBuilderFn
//import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//object HttpClientExampleApp extends App {
//  val client: ElasticClient = ElasticClient(JavaClient(ElasticProperties("http://localhost:9200")))
//
//  def list(keyword: String, offset: Int, limit: Int): Future[Option[String]] = {
//    val searchRequest =
//      search("my-index")
//        .query(
//          termQuery("keyword", keyword)
//        )
//        .from(offset)
//        .size(limit)
//
//    println(s"search body: ${SearchBodyBuilderFn(searchRequest).string}")
//
//    client.execute(searchRequest).map { response =>
//      println(response.map(_.totalHits))
//
//      response.body.map { body =>
//        println(s"response body: $body")
//        body
//      }
//    }
//  }
//
//  list("hoge", 0, 10)
//}
