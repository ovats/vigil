package com.vigil

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging
import com.vigil.config.api.ApiConfig
import com.vigil.dal.Connection
import com.vigil.dal.migration.Migrate
import com.vigil.routes.Routes

import scala.util.{Failure, Success}

object MainApi extends LazyLogging {

  def main(args: Array[String]): Unit = {
    logger.info(s"Starting Api ...")

    implicit val system: ActorSystem = ActorSystem("api")
    import system.dispatcher

    // Configuration
    implicit val conf: ApiConfig = ApiConfig()
    val interface: String        = conf.http.host
    val port: Int                = conf.http.port

    // Database versioning
    val flywayMigration = new Migrate(conf.postgres)
    flywayMigration.migrate()

    // Database connection
    val cn = new Connection(conf.postgres)

    // Routes
    val routes = new Routes(cn).routes

    // Start server
    Http()
      .newServerAt(interface, port)
      .bind(routes)
      .onComplete {
        case Success(_) => logger.info(s"Started at port $port")
        case Failure(e) => logger.error("Failed to start ... ", e)
      }
  }

}
