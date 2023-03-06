import Dependencies.Libraries

ThisBuild / version := "0.10"

ThisBuild / scalaVersion := "2.13.10"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "utf8",
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-unchecked",
  )
)

lazy val rootProject = project
  .in(file("."))
  .settings(
    name := "vigil"
  )
  .aggregate(common, api)

lazy val common = project
  .settings(
    name := "common",
    commonSettings,
    libraryDependencies ++= Libraries.allDependencies,
  )

lazy val api = project
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "buildInfoArticleApi",
  )
  .dependsOn(common)
  .settings(
    name := "api",
    commonSettings,
  )
  .settings(libraryDependencies ++= Libraries.allDependencies)
  .settings(dockerSettings)
  .enablePlugins(AshScriptPlugin)

lazy val ep = project
  .dependsOn(common)
  .settings(
    name := "ep",
    commonSettings,
  )
  .settings(libraryDependencies ++= Libraries.allDependencies)
  .settings(dockerSettings)
  .enablePlugins(AshScriptPlugin)

lazy val dockerSettings = Seq(
  Docker / packageName := name.value,
  Docker / version := version.value,
  dockerExposedPorts := Seq(8080),
  dockerBaseImage := "adoptopenjdk/openjdk11:alpine-slim",
  dockerUpdateLatest := true,
  dockerEnvVars := Map("POSTGRES_SERVERNAME" -> "host.docker.internal"),
)

addCommandAlias("goStrict", """set scalacOptions in ThisBuild += "-Xfatal-warnings" """)

enablePlugins(AshScriptPlugin)
