ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"

name := "Producer and Consumer"

version := "1.0"


enablePlugins(
  JavaAppPackaging,
  DockerPlugin
)

Compile / mainClass := Some("Producer")


lazy val root = (project in file("."))
  .settings(
    name := "Sample",
    mainClass := Some("Producer"),
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "3.5.1",
      "com.typesafe" % "config" % "1.4.1",
      "org.slf4j" % "slf4j-simple" % "1.7.32" // For logging (you can choose a different logging library if you prefer)
    )


  )
