package com.vigil.service

import com.typesafe.scalalogging.LazyLogging
import com.vigil.tapir.EndpointError

import scala.concurrent.Future

trait ServiceCommon extends LazyLogging {

  protected def handleExceptions[U](
      logMessage: String
  ): PartialFunction[Throwable, Future[Either[EndpointError, U]]] = {
    case e: Throwable =>
      val errorMsg = s"Error when: $logMessage"
      logger.error(errorMsg, e)
      Future.successful(Left(EndpointError("An unexpected error has occurred.")))
  }
}
