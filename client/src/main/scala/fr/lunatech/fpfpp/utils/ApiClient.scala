package fr.lunatech.fpfpp.utils

import fr.lunatech.fpfpp.Image
import io.circe.parser.decode
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.extra.Ajax
import japgolly.scalajs.react.extra.router.BaseUrl

object ApiClient {
  private val baseUrl = BaseUrl.fromWindowOrigin.value

  def getImages(success: Seq[Image] => Callback,
                error: Exception => Callback = Callback(_)): Callback =
    Ajax(
      "GET",
      s"$baseUrl/images"
    ).setRequestContentTypeJsonUtf8.send.onComplete { res =>
      res.status match {
        case 200 =>
          decode[Seq[Image]](res.responseText) match {
            case Right(images) => success(images)
            case Left(e)       => error(e)
          }
        case e =>
          error(new RuntimeException(s"Unexpected error during call api: $e"))
      }
    }.asCallback
}
