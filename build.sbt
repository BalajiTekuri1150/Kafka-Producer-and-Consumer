ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"

lazy val root = (project in file("."))
  .settings(
    name := "Sample",
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "3.5.1",
      "org.slf4j" % "slf4j-simple" % "1.7.32" // For logging (you can choose a different logging library if you prefer)
    )
  )
