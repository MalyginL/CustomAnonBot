name := "CustomBot"

version := "0.1"

scalaVersion := "2.12.8"

lazy val circeVersion     = "0.10.0"

resolvers += Resolver.bintrayRepo("hseeberger", "maven")
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
"ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
  "io.circe"          %% "circe-core"           % circeVersion,
  "io.circe"          %% "circe-generic"        % circeVersion,
  "io.circe"          %% "circe-generic-extras" % circeVersion,
  "io.circe"          %% "circe-parser"         % circeVersion,
  "io.circe"          %% "circe-testing"        % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.18.0",
  "com.github.blemale" %% "scaffeine" % "2.5.0" % "compile"
)