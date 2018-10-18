package fr.lunatech.fpfpp

import io.circe.{Decoder, Encoder}
import io.circe.generic.decoding.DerivedDecoder
import io.circe.generic.encoding.DerivedObjectEncoder

case class Image(id: String, url: String)
object Image {
  implicit val encoder: Encoder[Image] = DerivedObjectEncoder.deriveEncoder
  implicit val decoder: Decoder[Image] = DerivedDecoder.deriveDecoder
}
