package fr.lunatech.fpfpp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object WebServer {
  def main(args: Array[String]) {
    implicit val system: ActorSystem = ActorSystem("server-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val host = "localhost"
    val port = 9000

    val service = new WebService()

    Http().bindAndHandle(service.route, host, port)

    println(s"Server online at http://$host:$port")
  }
}

