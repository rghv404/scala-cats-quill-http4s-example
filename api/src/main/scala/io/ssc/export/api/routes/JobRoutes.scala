package io.ssc.export.api.routes

import cats.Monad
import cats._
import cats.effect._
import cats.implicits._
import io.chrisdavenport.fuuid.FUUID
import io.circe.Codec
import io.ssc.`export`.api.model.MissingDomains
import org.http4s.Status
import org.http4s.Status.BadRequest
import shapeless.Lazy.apply

import java.util.UUID
import io.ssc.export.api.routes.JobRoutes._
import org.http4s.{DecodeFailure, EntityDecoder}
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.jsonOf
import org.http4s.rho.RhoRoutes
import org.http4s.rho.swagger.SwaggerSyntax

class JobRoutes[F[+_]: Effect](swaggerSyntax: SwaggerSyntax[F]) extends RhoRoutes[F] {

  import swaggerSyntax._

  "The new route will be a POST query to see missing domains for a list of domains" **
    List("customerId", "domains") @@
    POST / "missing" / pathVar[UUID](
      id="customerId",
      description = "Customer Id to run the job for") ^ jsonOf[F, MissingDomains].intercept |>> {
    (customerId:UUID, domains: MissingDomains) =>

//      s"You posted $domains for $customerId"
//      lazy val customerUUID = FUUID.fromString(customerId) match {
//        case Left(_) => BadRequest.status.asLeft.pure[IO]
//        case Right(id) => id
//      }
      Ok(s"You posted $domains for $customerId")
                  // fin
    //      .flatMap{
//        case Left(NotFound.status)   => NotFound("")
//        case Left(BadRequest.status) => BadRequest("")
//        case Right(r)                => r
//        case _                       => InternalServerError("")
//      }
    }
}

object JobRoutes {

  implicit class EntityDecoderOps[F[_]: Monad, A](val entityDecoder: EntityDecoder[F, A]) {
    def intercept: EntityDecoder[F, A] = entityDecoder.handleErrorWith { f: DecodeFailure =>
      val detailedError = s"${f.getMessage()} ${f.getCause().getMessage}"
      throw new Throwable(detailedError)
    }
  }

//  case class MissingDomains(fileName: String)
//
//
//  object MissingDomains {
//    implicit val missingDomainsResponseCodec: Codec.AsObject[MissingDomains] = deriveCodec
//  }
}
