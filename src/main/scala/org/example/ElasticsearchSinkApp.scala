package org.example

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}

import scala.concurrent.ExecutionContextExecutor

object ElasticsearchSinkApp extends App {
  val hostName = "localhost"
  val port = 9000
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val stream: DataStream[String] = env.socketTextStream(hostName, port)
  val sessionItemInfo = createTypeInformation[SessionItem]

  implicit val ec: ExecutionContextExecutor = AsyncHttpSink.executionContext
  private val sink = new AsyncHttpSink(AsyncHttpSink.httpClient)
  stream.map(str => SessionItem(str).body).addSink(sink)

  env.execute("Elasticsearch SocketTextStreamWordCount Example")
}

