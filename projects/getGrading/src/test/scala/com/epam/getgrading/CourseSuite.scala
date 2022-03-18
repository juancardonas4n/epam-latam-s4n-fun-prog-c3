package com.epam.getgrading

import org.scalatest._
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec.AsyncFreeSpec
// import flatspec._
// import matchers._
import cats.syntax.either
import cats.data.State
import cats.data.StateT
import cats.data.EitherT
import cats.Monad
import cats.Functor
import cats.implicits._
import cats.effect._
// import cats.effect.testing.scalatest.AsyncIOSpec
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.IO._
import cats.effect.implicits._
import com.epam.getgrading.Utils._
import com.epam.getgrading.Student
import com.epam.getgrading.Student.{recordCourse}
import com.epam.getgrading.Course
import com.epam.getgrading.Course._
import com.epam.getgrading.Grade
import com.epam.getgrading.Grade._


// class CourseSuite extends AnyFlatSpec with should.Matchers {
class CourseSuite extends AsyncFreeSpec with AsyncIOSpec with Matchers {

  val cST0270 = 
    Course("ST0270", 4,
           Map("Parcial 1"     -> Grade("Parcial 1", 0.20),
               "Parcial 2"     -> Grade("Parcial 2", 0.20),
               "Parcial 3"     -> Grade("Parcial 3", 0.20),
               "Seguimiento"   -> Grade("Seguimiento", 0.10),
               "Trabajo final" -> Grade("Trabajo final",0.20)))

  val cST0275 = 
    Course("ST0275", 5,
           Map("Parcial 1" -> Grade("Parcial 1", 0.25),
               "Parcial 2" -> Grade("Parcial 2", 0.25),
               "Seguimiento" -> Grade("Seguimiento", 0.20),
               "Trabajo final" -> Grade("Trabajo final", 0.30)))

  def testRecordOneCourse:EitherStateIO[Unit] = for {
    c <- cST0270
    _ <- recordCourse(c)
  } yield ()

  def testRecordTwoCourses:EitherStateIO[Unit] = for {
    c1 <- cST0270
    _ <- recordCourse(c1)
    c2 <- cST0275
    _ <- recordCourse(c2)
  } yield ()

  def launchIO(std:Student):IO[Int] = for {
    r <- testRecordOneCourse.runS(std).value
    rstd <- r match {
      case Right(nstd) => IO { nstd.courses.size }
      case Left(_)     => IO { 0  } 
    }
  } yield (rstd)


    "A course added to student" - {
      "contains only one course" in {
        launchIO(Student()).asserting(_ shouldBe 1)
      }
    }
}

//   "Two courses added to Student" should "contains two courses" in {
//     val std = Student()
//     val test = for {
//       r <- testRecordTwoCourses.runS(std).value
//       rstd <- r match {
//         case Right(nstd) => IO { nstd }
//         case Left(_)     => IO { std  } 
//       }
//       _ <- IO { println("This is running") }
//     } yield (rstd.courses.size should be (3))
//   }

//   "Test course" should "this must failed" in {
//     val x = 10
//     x should be (10)
//   }

// }

