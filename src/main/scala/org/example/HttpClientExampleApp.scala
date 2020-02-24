package org.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.Duration

object HttpClientExampleApp extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val client = new AkkaHttpClient

  val res = client.post(baseUrl = "http://localhost:9200",
    endpoint = "my-index/my-type/1",
    body = """{"name": "test"}""")

  Await.result(res, Duration.Inf)
}
