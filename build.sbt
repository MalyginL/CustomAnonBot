name := "CustomBot"

version := "0.1"

scalaVersion := "2.12.8"

lazy val circeVersion = "0.10.0"

resolvers += Resolver.bintrayRepo("hseeberger", "maven")
libraryDependencies ++= Seq(
  "de.heikoseeberger" %% "akka-http-circe" % "1.18.0",
  "net.codingwell" %% "scala-guice" % "4.2.3",
  "com.typesafe.akka" %% "akka-actor" % "2.5.21",
  "com.typesafe.akka" %% "akka-stream" % "2.5.21",
  "com.typesafe.akka" %% "akka-http" % "10.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-testing" % circeVersion,
  "ch.megard" %% "akka-http-cors" % "0.4.0",
  "com.github.blemale" %% "scaffeine" % "2.5.0" % "compile",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.7" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.21" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.21" % Test,
  "org.scalatest" %% "scalatest" % "3.0.3" % Test,
  "org.scalamock" %% "scalamock" % "4.1.0" % Test

)