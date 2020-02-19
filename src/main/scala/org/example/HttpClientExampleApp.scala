package org.example

import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy
import com.sksamuel.elastic4s.http.ElasticDsl._

object HttpClientExampleApp extends App {

  val client = HttpClient(ElasticsearchClientUri("localhost", 9200))

  client.execute {
    bulk(
      indexInto("my-index" / "my-type").fields("country" -> "Mongolia", "capital" -> "Ulaanbaatar"),
      indexInto("my-index" / "my-type").fields("country" -> "Namibia", "capital" -> "Windhoek")
    ).refresh(RefreshPolicy.WAIT_UNTIL)
  }.await

  val result = client.execute {
    search("my-index").matchQuery("capital", "ulaanbaatar")
  }.await

  // prints out the original json
  println(result.hits.hits.head.sourceAsString)

  client.close()

}