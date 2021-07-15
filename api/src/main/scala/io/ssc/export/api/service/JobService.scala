package io.ssc.`export`.api.service

import cats.data.EitherT
import io.ssc.`export`.api.database.JobRepository
import io.ssc.`export`.api.model.{Customer, JobError, JobResponse, JobType, MissingDomains}

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

//  type EitherApp[A] = EitherT[F, JobError, A]
//
//  type Result[A] = ReaderT[EitherApp, ]

  def findCustomerId(customerId: UUID): EitherT[F, JobError, Customer] = ??? //F[Either[_, UUID]]

  def ensureInputFileExists(fileName: String): EitherT[F, JobError, Unit] = ???

  def ensureUniqueRequest(customerId: UUID, fileName: String): EitherT[F, JobError, Unit] = ???

  // job Type should be concrete
  def submitJob(jobType: JobType)(databricksClient: Any): EitherT[F, JobError, Int] = ???

  // does some stateful work
  def insertJobId(jobId: Int): F[JobResponse] = ???

  def run(customerId: UUID, domains: MissingDomains) = for {
    customer <- findCustomerId[F](customerId)
  } yield

}

object JobService {
  def apply[F[_]](repo: JobRepository[F]): JobService[F] = new JobService[F](repo)
}
