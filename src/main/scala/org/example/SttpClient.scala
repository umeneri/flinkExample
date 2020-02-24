//package org.example
//
//import sttp.client.akkahttp.AkkaHttpBackend
//import sttp.client._
//
//class SttpClient {
//  implicit val sttpBackend = AkkaHttpBackend()
//
//  def get = {
//    val response = basicRequest
//      .body("Hello, world!")
//      .post(uri"https://httpbin.org/post?hello=world").send()
//    println(response)
//  }
//
//}
