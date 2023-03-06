package com.vigil.config.api

object ConfigData {
  final case class HttpConf(host: String, port: Int)
  final case class PostgresProperties(
      serverName: String,
      portNumber: String,
      databaseName: String,
      user: String,
      password: String,
  )
  final case class Postgres(
      connectionPool: String,
      dataSourceClass: String,
      numThreads: Int,
      properties: PostgresProperties,
  )
}
