package io.ssc.`export`.api.service

import cats.{Functor, MonadError}
import cats.data.EitherT
import cats.implicits._
import cats.mtl.{ApplicativeHandle, FunctorRaise}
import cats.mtl.implicits._
import io.ssc.`export`.api.database.JobRepository
import io.ssc.`export`.api.model.{Customer, CustomerNotFoundError, FileNotFoundError, JobError, JobResponse, JobType, MissingDomains, RequestNotUniqueError}

import java.util.UUID

class JobService[F[_]](repo: JobRepository[F]) {
  /* For missing routes
  1. Check if customerId exists in the DB
  2. Find if the submitted file exist in s3
  3. Query potgres DB to look if same customer is queued in for same end point
        <-> in process or <-> completed within day with same file name
  4. If new request -> Call Data bricks client to trigger the job and create new request Id
  5. and insert into postgres db with request id
  5.
  6.
   */


  type EitherApp[A] = EitherT[F, JobError, A]

  def findCustomerId(customerId: UUID): EitherApp[Customer] = ???

  def ensureInputFileExists(fileName: String): EitherApp[Unit] = ???

  def ensureUniqueRequest(customerId: UUID, fileName: String): EitherApp[Unit] = ???

  def submitJob(jobType: JobType): EitherApp[Int] = ???

  def insertJobId(jobId: Int): EitherApp[JobResponse] = ???

  def run(customerId: UUID, fileName: String, jobType: JobType) (
    implicit ME: MonadError[F, Throwable],
    functorRaise: FunctorRaise[F, JobError]
  )= for {
      customer <- findCustomerId(customerId) // either customer exist or not exist error
      _ <- ensureInputFileExists(fileName) // either file exists or not exist error
      _ <- ensureUniqueRequest(customerId, fileName) // either uniquer request or a duplicate request error
      jobId <- submitJob(jobType)
      resp <- insertJobId(jobId)
    } yield resp
//    res.handleWith({
//      case Left(CustomerNotFoundError) => "Customer Id not found".asLeft
//      case Left(RequestNotUniqueError) => "Customer Id not found".asLeft
//      case Left(FileNotFoundError) => "Uploaded File not found".asLeft
//      case Right(resp: JobResponse) => s"Nice! $resp".asRight
//    }).recoverWith({
//      case e: Throwable => ME.raiseError(new RuntimeException("Something terribly wrong has hapened", e))
//    })
}

object JobService {
  def apply[F[_]](repo: JobRepository[F]): JobService[F] = new JobService[F](repo)
}
