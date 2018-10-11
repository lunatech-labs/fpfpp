package fr.lunatech.fpfpp

import akka.http.scaladsl.server.Directives

class WebService extends Directives {

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
  }

}
