package com.vigil.dal

import com.vigil.config.api.ConfigData.Postgres
import slick.jdbc.PostgresProfile.api._

class Connection(postgresConfig: Postgres) {
  private val databaseUrl =
    s"jdbc:postgresql://${postgresConfig.properties.serverName}:${postgresConfig.properties.portNumber}/${postgresConfig.properties.databaseName}"

  val db = Database.forURL(databaseUrl, postgresConfig.properties.user, postgresConfig.properties.password)

}
