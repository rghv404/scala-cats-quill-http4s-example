package io.ssc.`export`.api.database

import cats.data.EitherT
import cats.effect.Bracket
import io.getquill.{ActionReturning, EntityQuery, Insert, SnakeCase}
import io.ssc.`export`.api.model.{Customer, CustomerNotFoundError, JobError, JobType, Queued, Request}
import doobie._
import doobie.implicits._
import cats.syntax.all._
import java.time.Instant
import java.util.UUID

private object JobSQL{
  import ctx._

  // this will hold
  val customer: ctx.Quoted[EntityQuery[Customer]] = quote {
    querySchema[Customer]("customer_metadata")
  }

  val request = quote {
    querySchema[Request]("request_metadata")
  }

  def find(id: UUID): Result[RunQueryResult[Customer]] = ctx.run(customer.filter(_.customer_id == lift(id)))

  def insert(req: Request): Result[RunActionReturningResult[UUID]] = ctx.run(request.insert(lift(req)).returning(_.request_id))

}

class JobRepository[F[_]: Bracket[*[_], Throwable]](val xa: Transactor[F]) {
  import JobSQL._

  def findCustomer(id: UUID) = for {
    res <- find(id).transact(xa)
  } yield res.headOption

//    find(id).transact(xa) match {
//        case Seq(customer:Customer) => customer.customer_id.asRight
//        case Seq(_) => CustomerNotFoundError.asLeft
//    })

  def insertJobRecord(db_run_id: Int, db_run_url: String, customerId: UUID,
                      request_body: JobType): EitherT[F, JobError, Request] = {
    // create request id as uuid
    val job_id = UUID.randomUUID()
    val req = Request(
      request_id          = job_id,
      status              = Queued.toString.toLowerCase,
      customer_id         = customerId,
      databricks_run_id   = db_run_id,
      databricks_run_url  = db_run_url,
      request_submit_time = Instant.now.getEpochSecond,
      request_json        = request_body
    )

//    EitherT(
//      Sync[F].delay{
//        ctx.run(quote{query[Request].insert(lift(req)).returning(_.request_id)}).map { uuid => req.copy(request_id = uuid).asRight
//        }.getOrElse(CustomerNotFoundError.asLeft)
//      })
  }
}

object JobRepository {
  def apply[F[_]: Bracket[*[_], Throwable]](xa: Transactor[F]): JobRepository[F] = new JobRepository[F](xa)
}