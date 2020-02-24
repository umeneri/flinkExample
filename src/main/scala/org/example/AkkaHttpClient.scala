package org.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import io.circe.parser.parse
import io.circe.{Json, ParsingFailure}

import scala.concurrent.{ExecutionContextExecutor, Future}

class AkkaHttpClient()(
                        implicit val system: ActorSystem,
                        implicit val materializer: ActorMaterializer,
                        implicit val executionContext: ExecutionContextExecutor
                    ) {
  def post(baseUrl: String, endpoint: String, body: String): Future[Either[ParsingFailure, Json]] = {
    makePostRequest(baseUrl, endpoint, body)
  }

  private def makePostRequest(baseUrl: String, endpoint: String, body: String): Future[Either[ParsingFailure, Json]] = {
    val method = HttpMethods.POST
    val url = Uri(s"$baseUrl/$endpoint")
    val queryParams = ""

    val request = HttpRequest(
      uri = url.withQuery(Query(queryParams)),
      method = method,
      entity = HttpEntity(body).withContentType(ContentTypes.`application/json`)
    )

    println(request)

    for {
      httpResponse <- Http().singleRequest(request)
      string <- Unmarshal(httpResponse.entity).to[String]
    } yield {
      println(httpResponse)
      println(string)
      parse(string)
    }
  }

}
