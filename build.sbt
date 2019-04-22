name := "CustomBot"

version := "0.1"

scalaVersion := "2.12.8"

lazy val circeVersion = "0.10.0"
lazy val phantom = "2.39.0"

resolvers ++= Seq(
  Resolver.bintrayRepo("hseeberger", "maven"),
  Resolver.sonatypeRepo("releases"),
  Resolver.typesafeRepo("releases"),
  Resolver.bintrayRepo("websudos", "oss-releases")
)
libraryDependencies ++= Seq(
  "com.outworkers" %% "phantom-dsl" % "2.39.0",
  "com.outworkers" %% "phantom-streams" % "2.39.0",
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
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "com.outworkers" %% "util-testing" % "0.50.0" % Test,
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
  "com.typesafe.slick" %% "slick-testkit" % "3.3.0" % "test",
  "com.github.blemale" %% "scaffeine" % "2.5.0" % "compile",
  "org.postgresql" % "postgresql" % "42.2.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.7" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.21" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.21" % Test,
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.scalamock" %% "scalamock" % "4.1.0" % Test,
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.6"
)



