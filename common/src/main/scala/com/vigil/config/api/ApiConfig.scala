package com.vigil.config.api

import com.typesafe.scalalogging.LazyLogging
import com.vigil.config.api.ConfigData.{HttpConf, Postgres}
import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._

case class ApiConfig(http: HttpConf, postgres: Postgres)

object ApiConfig extends LazyLogging {

  def apply(resource: String = "application.conf"): ApiConfig = {

    implicit def hint[A] = ProductHint[A](ConfigFieldMapping(CamelCase, CamelCase))

    ConfigSource.resources(resource).load[ApiConfig] match {
      case Left(errors) =>
        val msg = s"Unable to load service configuration"
        logger.error(s"$msg \n${errors.toList.map(_.description).mkString("* ", "\n*", "")}")
        throw new IllegalStateException(msg)
      case Right(config) => config
    }
  }

}
