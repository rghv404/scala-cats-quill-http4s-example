package io.ssc.export.api.routes

import java.nio.charset.StandardCharsets

import org.http4s.headers.`Content-Type`
import org.http4s.{Entity, EntityEncoder, MediaType}

import scala.collection.compat.immutable.ArraySeq

import io.circe.{ Decoder, Encoder }
import io.circe.shapes
import shapeless.{ Coproduct, Generic }

object JsonEncoder {
//   import fs2.Stream
// //   import org.json4s._
// //   import org.json4s.jackson.Serialization
// //   import org.json4s.jackson.Serialization.write

//   trait AutoSerializable extends AnyRef with Product

//   private implicit val formats: Formats =
//     Serialization.formats(NoTypeHints)

//   implicit def autoSerializableEntityEncoder[F[_], A <: AutoSerializable]: EntityEncoder[F, A] =
//     EntityEncoder.encodeBy(`Content-Type`(MediaType.application.json)) { a =>
//       val bytes = write(a).getBytes(StandardCharsets.UTF_8)
//       Entity(Stream.emits(ArraySeq.unsafeWrapArray(bytes)), Some(bytes.length))
//     }

    implicit def encodeAdtNoDiscr[A, Repr <: Coproduct](implicit
        gen: Generic.Aux[A, Repr],
        encodeRepr: Encoder[Repr]
    ): Encoder[A] = encodeRepr.contramap(gen.to)

    implicit def decodeAdtNoDiscr[A, Repr <: Coproduct](implicit
        gen: Generic.Aux[A, Repr],
        decodeRepr: Decoder[Repr]
    ): Decoder[A] = decodeRepr.map(gen.from)

}