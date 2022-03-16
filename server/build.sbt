name := "server"

version := "0.1"

scalaVersion := "2.13.8"

idePackagePrefix := Some("fr.dopolytech.polydrive")

// Having fork to true allows you to run several instances of sbt run for this
// project at the same time
fork := true

val AkkaVersion = "2.6.18"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-discovery" % AkkaVersion,
  "com.typesafe.akka" %% "akka-cluster-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.11",
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion
)
enablePlugins(AkkaGrpcPlugin)
