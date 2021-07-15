package io.ssc.`export`.api.model

import java.util.UUID
sealed trait Status
case object  Queued extends Status


case class Request(request_id: UUID,
                   status: String,
                   customer_id: UUID,
                   databricks_run_id: Int,
                   databricks_run_url: String,
                   request_submit_time: Long,
                   request_completion_time: Option[Long] = None,
                   request_json: JobType
                  )
