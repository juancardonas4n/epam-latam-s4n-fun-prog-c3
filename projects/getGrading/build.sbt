ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "com.epam"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"

scalacOptions ++= Seq(
  "-Xfatal-warnings"
)