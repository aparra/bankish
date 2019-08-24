name := "bankish"
organization := "com.aparra"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.0"
javacOptions ++= Seq("-source", "11")

libraryDependencies ++= Seq(
  guice,
  "junit"        % "junit"           % "4.12" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test
)
