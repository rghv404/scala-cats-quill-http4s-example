package io.ssc.`export`.api

import doobie.quill.DoobieContext
import io.getquill.{Literal, PostgresJdbcContext, SnakeCase}

package object database {
//  lazy val ctx = new PostgresJdbcContext(Literal, "ctx")
  lazy val ctx = new DoobieContext.Postgres(SnakeCase)
}
