package com.vigil.dal.migration

import com.vigil.config.api.ConfigData.Postgres
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

class Migrate(postgresConfig: Postgres) {

  private val databaseUrl =
    s"jdbc:postgresql://${postgresConfig.properties.serverName}:${postgresConfig.properties.portNumber}/${postgresConfig.properties.databaseName}"

  def migrate(): MigrateResult = {
    Flyway
      .configure()
      .dataSource(databaseUrl, postgresConfig.properties.user, postgresConfig.properties.password)
      .load()
      .migrate()
  }
}
