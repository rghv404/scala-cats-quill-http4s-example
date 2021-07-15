package io.ssc.`export`.api

import io.circe.Decoder
import pureconfig.generic.semiauto._
import io.ssc.`export`.api.database.DatabaseConfig
import pureconfig.{ConfigReader, ConfigSource}

object Config {

  case class ServerConfig(host: String, port: Int)

  case class ExportApiConfig(db: DatabaseConfig, server: ServerConfig)

  implicit val exportApiConfigReader: ConfigReader[ExportApiConfig] = deriveReader[ExportApiConfig]

  lazy val config = ConfigSource.default.loadOrThrow[ExportApiConfig]

}