ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "com.epam"


libraryDependencies += "org.typelevel" %% "cats-core" %
                    "2.3.0"
libraryDependencies += "org.scala-lang.modules" %%
                    "scala-parser-combinators" % "2.0.0"
libraryDependencies += "org.typelevel" %% "cats-effect" %
                    "2.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" %
                    "3.2.11" % Test

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation"
)