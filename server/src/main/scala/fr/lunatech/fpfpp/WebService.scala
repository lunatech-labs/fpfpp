package fr.lunatech.fpfpp

import akka.http.scaladsl.server.Directives
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.Random

class WebService extends Directives {

  import WebServer._

  implicit val ec: ExecutionContext = system.dispatcher

  val route = {
    pathSingleSlash {
      getFromResource("public/index.html")
    } ~
      pathPrefix("assets" / Remaining) { file =>
        println("Asking file --- " + file)
        encodeResponse {
          getFromResource("public/" + file)
        }
      }
  } ~
    path("images") {
      get {
        val ids = Random.shuffle((1 until 30).toList)
        complete(ids.map(i => Image(i.toString, s"assets/img/images/$i.png")).asJson.noSpaces)
      }
    }
}
