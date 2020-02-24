package org.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}

import scala.concurrent.ExecutionContextExecutor

// for invoke,
case class SessionItem(body: String) extends java.io.Serializable

object AsyncHttpSink {
  val baseUrl: String = "http://localhost:9200"
  val endpoint: String = "my-index/my-type"
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val httpClient = new AkkaHttpClient()
}


class AsyncHttpSink(httpClient: AkkaHttpClient)(implicit ec: ExecutionContextExecutor) extends SinkFunction[String] with java.io.Serializable {
//  val httpStatusesAccumulator: Histogram = getRuntimeContext.getHistogram("http_statuses")

//  def open(parameters: Nothing): Unit = {
//  }

//  override def close(): Unit = {
////    httpStatusesAccumulator.resetLocal()
//  }

  val body: String = """{"name":"sink"}"""

  override def invoke(body: String): Unit = {
    println("post")
    httpClient.post(AsyncHttpSink.baseUrl, AsyncHttpSink.endpoint, body).map { res =>
      res.fold({ failure =>
        println(failure)
//        httpStatusesAccumulator.add(-1)
      },
        { response =>
          println(response)
//          httpStatusesAccumulator.add(1)
        })
    }
  }

}


