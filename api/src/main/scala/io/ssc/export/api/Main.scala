package io.ssc.export.api

import cats.data.EitherT
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import com.http4s.rho.swagger.ui.SwaggerUi
import doobie.util.ExecutionContexts
import io.ssc.`export`.api.routes.JobRoutes
import io.ssc.export.api.routes.MyRoutes
import org.http4s.rho.swagger.SwaggerMetadata
import org.http4s.rho.swagger.models.{Info, Tag}
import org.http4s.rho.swagger.syntax.{io => ioSwagger}
import org.http4s.server.blaze.BlazeServerBuilder
import org.log4s.getLogger
import org.http4s.implicits._
import org.http4s.server.{Router, Server => H4Server}
import org.http4s.implicits._
import io.ssc.`export`.api.Config._
import io.ssc.`export`.api.database.{DatabaseConfig, JobRepository}
import io.ssc.`export`.api.model.JobError
import io.ssc.`export`.api.service.JobService
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigConvert.fromReaderAndWriter
import pureconfig.ConfigSource

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  private val logger = getLogger

  private val port: Int = Option(System.getenv("HTTP_PORT"))
    .map(_.toInt)
    .getOrElse(8080)

  logger.info(s"Starting Swagger example on '$port'")

  logger.info(printConfigInfo())

  def createServer[F[_] : ContextShift : ConcurrentEffect : Timer]: Resource[F, H4Server[F]] = {
    for {
      serverEc                <- ExecutionContexts.cachedThreadPool[F]
      connEc                  <- ExecutionContexts.fixedThreadPool[F](10) // change later to read from conf
      txnEc                   <- ExecutionContexts.cachedThreadPool[F]
      xa                      <- DatabaseConfig.dbTransactor(config.db, connEc, Blocker.liftExecutionContext(txnEc))
      jobRepo                 =  JobRepository[F](xa)
      jobService              =  JobService[F](jobRepo)
      metadata                =  SwaggerMetadata(apiInfo = Info(title = "Rho demo", version = "1.2.3"),
                                                  tags = List(Tag(name = "hello", description = Some("These are the hello routes.")))
                                                )
      swaggerUiRhoMiddleware  =  SwaggerUi[F].createRhoMiddleware(Blocker.liftExecutionContext(serverEc), swaggerMetadata = metadata)
      routes                  =  Router("/v1" -> new MyRoutes[F](ioSwagger[F]).toRoutes(swaggerUiRhoMiddleware),
                                        "/v1/run" -> new JobRoutes[F](ioSwagger[F], jobService).toRoutes(swaggerUiRhoMiddleware)
                                        ).orNotFound
      _                       <- Resource.eval(DatabaseConfig.initializeDb(config.db))
      server                  <- BlazeServerBuilder[F](serverEc)
                                  .bindHttp(config.server.port, config.server.host)
                                  .withHttpApp(routes)
                                  .resource
    } yield server
  }

  def printConfigInfo(): String = ???

  def run(args: List[String]): IO[ExitCode] = {
//    Blocker[IO].use { blocker =>
//      val metadata = SwaggerMetadata(
//        apiInfo = Info(title = "Rho demo", version = "1.2.3"),
//        tags = List(Tag(name = "hello", description = Some("These are the hello routes.")))
//      )
//
//      val swaggerUiRhoMiddleware =
//        SwaggerUi[IO].createRhoMiddleware(blocker, swaggerMetadata = metadata)
//
//
//      val myRoutes = new MyRoutes[IO](ioSwagger).toRoutes(swaggerUiRhoMiddleware)
//      val jobRoutes = new JobRoutes[IO](ioSwagger).toRoutes(swaggerUiRhoMiddleware)
//
//
//      val apis = Router(
//        "/v1" -> myRoutes,
//        "/v1/run" -> jobRoutes
//      ).orNotFound
//
//      BlazeServerBuilder[IO](global)
//        .withHttpApp(apis)
//        .bindHttp(port, "0.0.0.0")
//        .serve
//        .compile
//        .drain
//        .as(ExitCode.Success)

//    for {
//      logger  <- Slf4jLogger.create[IO]
//      _       <- logger.info("Starting server....")
//      _       <- logger.info(printConfigInfo())
//      server = createServer
//    } yield server.use(_ => IO.never).as(ExitCode.Success)
    createServer.use(_ => IO.never).as(ExitCode.Success)
  }
}