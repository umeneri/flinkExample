ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

name := "Flink Project"

version := "0.1-SNAPSHOT"

organization := "org.example"

ThisBuild / scalaVersion := "2.12.7"

val flinkVersion = "1.9.2"
val elastic4sVersion = "5.6.10"
val akkaVersion = "2.5.25"
val akkaHttpVersion = "10.0.10"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-connector-elasticsearch5" % flinkVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.29.1",
  "org.scalatest"     %% "scalatest"           % "3.0.0" % "test",
  //  "com.softwaremill.sttp.client" %% "akka-http-backend" % "2.0.0-RC13",
  //  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  //  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
  //  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
  //  "com.sksamuel.elastic4s" %% "elastic4s-http-streams" % elastic4sVersion,
)
lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies
  )

assembly / mainClass := Some("org.example.Job")

// make run command include the provided dependencies
Compile / run := Defaults.runTask(Compile / fullClasspath,
  Compile / run / mainClass,
  Compile / run / runner
).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false)
