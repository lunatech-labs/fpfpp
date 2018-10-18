package fr.lunatech.fpfpp

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.unmarshalling.Unmarshaller.stringUnmarshaller
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, HCursor}

import scala.concurrent.ExecutionContext
import scala.util._

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
        onComplete(buildImages) {
          case Success(value) => complete(value.asJson.noSpaces)
          case _ => complete("Error")
        }
      }
    }

  private def buildImages = {
    implicit val imageDecoder: Decoder[Image] = (c: HCursor) => {
      for {
        id <- c.downField("id").as[String]
        src <- c.downField("urls").downField("regular").as[String]
      } yield Image(id, src)
    }

    val resultDecoder: Decoder[Seq[Image]] = (c: HCursor) => {
      c.downField("results").as[Seq[Image]]
    }

    Http().singleRequest(
      HttpRequest(uri = "https://api.unsplash.com/search/photos?query=halloween")
        .withHeaders(RawHeader("Authorization", "Client-ID 035a955759a49d0ca13bd8d380f89abfe303e1a83dd40eea939151e4e0f6c697"))
    ).flatMap(res => stringUnmarshaller(res.entity))
      .map(json => decode[Seq[Image]](json)(resultDecoder).right.toSeq.flatten)
  }
}
