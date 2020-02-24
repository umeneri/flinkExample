package org.example

import java.net.{InetAddress, InetSocketAddress}

import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch5.ElasticsearchSink
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

object ElasticsearchSinkFactory {
  def get(): ElasticsearchSink[String] = {
    val config = new java.util.HashMap[String, String]
    config.put("cluster.name", "my-cluster-name")
    config.put("bulk.flush.max.actions", "1")
    val transportAddresses = new java.util.ArrayList[InetSocketAddress]
    transportAddresses.add(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 9300))
    transportAddresses.add(new InetSocketAddress(InetAddress.getByName("10.2.3.1"), 9300))
    val esFunction = new ElasticsearchCreateFunction()

    new ElasticsearchSink(config, transportAddresses, esFunction)
  }

  class ElasticsearchCreateFunction extends ElasticsearchSinkFunction[String] {
    def createIndexRequest(element: String): IndexRequest = {
      val json = new java.util.HashMap[String, String]
      json.put("data", element)

      Requests.indexRequest()
        .index("my-index")
        .`type`("my-type")
        .source(json)
    }

    override def process(element: String, ctx: RuntimeContext, indexer: RequestIndexer) {
      indexer.add(createIndexRequest(element))
    }
  }

}
