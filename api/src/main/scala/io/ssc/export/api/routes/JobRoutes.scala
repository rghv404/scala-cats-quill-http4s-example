package io.ssc.export.api.routes

import cats.{Monad, MonadError}
import cats.effect._
import cats.implicits._
import cats.mtl.FunctorRaise
import cats.mtl.implicits._
import io.ssc.`export`.api.model.{CustomerNotFoundError, FileNotFoundError, JobError, JobResponse, JobType, MissingDomains, RequestNotUniqueError}
import io.ssc.`export`.api.service.JobService

import java.util.UUID
import io.ssc.export.api.routes.JobRoutes._
import org.http4s.{DecodeFailure, EntityDecoder}
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.jsonOf
import org.http4s.rho.RhoRoutes
import org.http4s.rho.swagger.SwaggerSyntax

class JobRoutes[F[+_] : Effect: Monad](swaggerSyntax: SwaggerSyntax[F], jobService: JobService[F]) extends RhoRoutes[F] {

  import swaggerSyntax._

  "The new route will be a POST query to see missing domains for a list of domains" **
    List("customerId", "domains") @@
      POST / "missing" / pathVar[UUID](
    id = "customerId",
    description = "Customer Id to run the job for") ^ jsonOf[F, MissingDomains].intercept |>> {
    (customerId: UUID, job: MissingDomains) => runJobAndHandle(customerId, job.fileName, job)
      //      s"You posted $domains for $customerId"
      //      lazy val customerUUID = FUUID.fromString(customerId) match {
      //        case Left(_) => BadRequest.status.asLeft.pure[IO]
      //        case Right(id) => id
      //      }
      //
    //                      jobService.run(customerId, job.fileName, job).handleWith({
    //                        case Left(CustomerNotFoundError) => BadRequest("Customer Id not found")
    //                        case Left(RequestNotUniqueError) => BadRequest("Customer Id not found")
    //                        case Left(FileNotFoundError) => BadRequest("Uploaded File not found")
    //                        case Right(resp) => Ok(s"Nice! $resp")
    //                      })
    //                        .recoverWith

    //      Ok(s"You posted $domains for $customerId")
    // fin
    //      .flatMap{
    //        case Left(NotFound.status)   => NotFound("")
    //        case Left(BadRequest.status) => BadRequest("")
    //        case Right(r)                => r
    //        case _                       => InternalServerError("")
    //      }
  }

  def runJobAndHandle(customerId: UUID, fileName:String, job: JobType)(
    implicit ME: MonadError[F, Throwable],
    FR: FunctorRaise[F, JobError]
  ) = jobService.run(customerId, fileName, job).value.flatMap {
    case Left(CustomerNotFoundError) => BadRequest("Customer Id not found")
    case Left(RequestNotUniqueError) => BadRequest("Customer Id not found")
    case Left(FileNotFoundError) => BadRequest("Uploaded File not found")
    case Right(resp: JobResponse) => Ok(s"Nice! $resp")
  }.recoverWith({
    case e: Throwable => ME.raiseError(new RuntimeException("Something terribly wrong happened", e))
  })

}

object JobRoutes {

  implicit class EntityDecoderOps[F[_] : Monad, A](val entityDecoder: EntityDecoder[F, A]) {
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
