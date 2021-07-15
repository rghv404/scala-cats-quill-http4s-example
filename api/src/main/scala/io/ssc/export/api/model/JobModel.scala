package io.ssc.`export`.api.model

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

sealed trait JobError
case object CustomerNotFoundError extends JobError
case object FileNotFoundError extends JobError
case object RequestNotUniqueError extends JobError
case class DataBricksClientError(errorCode: Int) extends JobError

sealed trait JobType extends Product with Serializable
case class MissingDomains(fileName: String) extends JobType

case class JobResponse(runId: String)

object MissingDomains {
  implicit val missingDomainsResponseCodec: Codec.AsObject[MissingDomains] = deriveCodec
}
object JobResponse {
  implicit val missingDomainsResponseCodec: Codec.AsObject[MissingDomains] = deriveCodec
 }

