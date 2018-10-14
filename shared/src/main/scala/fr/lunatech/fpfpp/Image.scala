package fr.lunatech.fpfpp

import io.circe.{Decoder, Encoder}
import io.circe.generic.decoding.DerivedDecoder
import io.circe.generic.encoding.DerivedObjectEncoder

case class Image private (name: String, url: String)
object Image {
  def newForm(name: String, url: String): Image = Image(name, url)
  implicit val encoder: Encoder[Image] = DerivedObjectEncoder.deriveEncoder
  implicit val decoder: Decoder[Image] = DerivedDecoder.deriveDecoder
}
