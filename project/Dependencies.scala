import sbt._

object Dependencies {

  object Versions {

    val pureconfigVersion = "0.17.2"

    val logbackVersion      = "1.4.5"
    val scalaLoggingVersion = "3.9.5"

    val flyWayDbVersion = "9.15.0"
    val slickVersion    = "3.4.1"
    val postgresVersion = "42.5.4"
    val hikaricpVersion = "3.4.1"

    val tapirVersion         = "1.2.9"
    val akkaVersion          = "2.6.20"
    val akkaHttpCirceVersion = "1.39.2"

    val scalaTestVersion    = "3.2.15"
    val mockitoScalaVersion = "1.17.12"
    val akkaHttpVersion     = "10.2.9"
  }

  object Libraries {

    // PureConfig
    val pureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.pureconfigVersion

    // Logs
    val logback      = "ch.qos.logback"              % "logback-classic" % Versions.logbackVersion % Runtime
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging"   % Versions.scalaLoggingVersion

    // Flyway version control
    val flyWayDb = "org.flywaydb" % "flyway-core" % Versions.flyWayDbVersion

    //Slick
    val slick    = "com.typesafe.slick" %% "slick"          % Versions.slickVersion
    val postgres = "org.postgresql"      % "postgresql"     % Versions.postgresVersion
    val hikaricp = "com.typesafe.slick" %% "slick-hikaricp" % Versions.hikaricpVersion

    //Tapir
    val tapirCore      = "com.softwaremill.sttp.tapir" %% "tapir-core"              % Versions.tapirVersion
    val tapirAkkaHttp  = "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server"  % Versions.tapirVersion
    val tapirSwaggerUI = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % Versions.tapirVersion
    val tapirCirce     = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"        % Versions.tapirVersion
    val akkaHttpCirce  = "de.heikoseeberger"           %% "akka-http-circe"         % Versions.akkaHttpCirceVersion

    // Test
    val scalaTest    = "org.scalatest" %% "scalatest"     % Versions.scalaTestVersion    % "test" //"test,it"
    val mockitoScala = "org.mockito"   %% "mockito-scala" % Versions.mockitoScalaVersion % "test"
    val mockitoScalaTest =
      "org.mockito" %% "mockito-scala-scalatest" % Versions.mockitoScalaVersion % "test" // "test,it"

    // Akka
    val akkaTest     = "com.typesafe.akka" %% "akka-testkit"      % Versions.akkaVersion     % "test"
    val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttpVersion % "test" //"test,it"

    val logDependencies    = Seq(logback, scalaLogging)
    val flywayDependencies = Seq(flyWayDb)
    val slickDependencies  = Seq(slick, postgres, hikaricp)
    val tapirDependencies  = Seq(tapirCore, tapirAkkaHttp, tapirCirce, tapirSwaggerUI, akkaHttpCirce)
    val akkaDependencies   = Seq(akkaTest, akkaHttpTest)
    val testDependencies   = Seq(scalaTest, mockitoScala, mockitoScalaTest)

    val allDependencies = Seq(
        pureConfig
      ) ++ logDependencies ++ slickDependencies ++ flywayDependencies ++ tapirDependencies ++ akkaDependencies ++ testDependencies
  }

}
