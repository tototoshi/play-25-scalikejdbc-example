name := """play-25-scalikejdbc-example"""
version := "1.0-SNAPSHOT"

lazy val commonSettings = Seq(
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
      jdbc,
      "org.scalikejdbc" %% "scalikejdbc" % "2.4.2",
      "org.scalikejdbc" %% "scalikejdbc-config" % "2.4.2",
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
    )
)

lazy val simple = (project in file("simple"))
  .enablePlugins(PlayScala)
  .settings(
    name := "simple"
  )
  .settings(commonSettings:_*)

lazy val multiple = (project in file("multiple"))
  .enablePlugins(PlayScala)
  .settings(
    name := "multiple"
  )
  .settings(commonSettings:_*)