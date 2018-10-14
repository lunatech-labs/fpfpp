package fr.lunatech.fpfpp

import akka.http.scaladsl.server.Directives
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalaj.http._

class WebService extends Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

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
        complete(buildImages)
      }
    }

  private def buildImages = {
    val response = Http("https://api.unsplash.com/search/photos")
      .header("content-type", "application/json")
      .header(
        "Authorization",
        "Client-ID 035a955759a49d0ca13bd8d380f89abfe303e1a83dd40eea939151e4e0f6c697")
      .param("query", "halloween")
      .asString
    val json = Json.parse(response.body)
    val imagesJson = json("results")

    val images = imagesJson
      .validate[Seq[Image]]
      .fold(err => {
        println(err)
        Seq.empty
      }, valid => valid)
    images
  }

  implicit val reads: Reads[Image] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "urls" \ "regular").read[String]
  )(Image.apply _)
}
