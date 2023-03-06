package com.vigil

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging

object MainEp extends LazyLogging {

  def main(args: Array[String]): Unit = {

    logger.info("Starting Ep ...")

    implicit val system: ActorSystem = ActorSystem("ep")
    import system.dispatcher

  }

}
