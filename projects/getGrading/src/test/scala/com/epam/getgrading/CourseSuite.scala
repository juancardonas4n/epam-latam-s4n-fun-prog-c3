package com.epam.getgrading

import org.scalatest._
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec.AsyncFreeSpec
import cats.syntax.either
import cats.data.State
import cats.data.StateT
import cats.data.EitherT
import cats.Monad
import cats.Functor
import cats.implicits._
import cats.effect._
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.IO._
import cats.effect.implicits._
import com.epam.getgrading.Utils._
import com.epam.getgrading.Student
import com.epam.getgrading.Student.{addCourse,recordGrade}
import com.epam.getgrading.Course
import com.epam.getgrading.Course._
import com.epam.getgrading.Eval
import com.epam.getgrading.Eval._
import com.epam.getgrading.Grade
import com.epam.getgrading.Grade._

class CourseSuite extends AsyncFreeSpec with AsyncIOSpec with Matchers {
  val cST0270 =
    addCourse("ST0270", 4,
              Map("Parcial 1"     -> Grade("Parcial 1", 0.20),
                  "Parcial 2"     -> Grade("Parcial 2", 0.20),
                  "Parcial 3"     -> Grade("Parcial 3", 0.20),
                  "Seguimiento"   -> Grade("Seguimiento", 0.10),
                  "Trabajo final" -> Grade("Trabajo final",0.30)))

  val dcST0270 =
    addCourse("ST0270", 4,
              Map("Parcial 1"     -> Grade("Parcial 1", 0.20),
                  "Parcial 2"     -> Grade("Parcial 2", 0.20),
                  "Parcial 3"     -> Grade("Parcial 3", 0.20),
                  "Seguimiento"   -> Grade("Seguimiento", 0.10),
                  "Trabajo final" -> Grade("Trabajo final",0.20)))

  val cST0275 =
    addCourse("ST0275", 5,
              Map("Parcial 1" -> Grade("Parcial 1", 0.25),
                  "Parcial 2" -> Grade("Parcial 2", 0.25),
                  "Seguimiento" -> Grade("Seguimiento", 0.20),
                  "Trabajo final" -> Grade("Trabajo final", 0.30)))

  def testRecordOneCourse:StateEitherIO[Unit] =
    for {
      _ <- cST0270
    } yield ()

  def testRecordTwoCourses:StateEitherIO[Unit] =
    for {
      _ <- cST0270
      _ <- cST0275
    } yield ()

  def testRecordOneCourseBadly:StateEitherIO[Unit] =
    for {
      _ <- dcST0270
    } yield ()

  def testRegisterOneGrade:StateEitherIO[Eval] =
    for {
      _ <- cST0270
      e <- recordGrade("ST0270:Parcial 1", 2.1)
    } yield e

  def testRegisterTwoGrades:StateEitherIO[Eval] =
    for {
      _ <- cST0270
      _ <- recordGrade("ST0270:Parcial 1", 2.1)
      e <- recordGrade("ST0270:Parcial 2", 3.1)
    } yield e

  def testRegisterThreeGrades:StateEitherIO[Eval] =
    for {
      _ <- cST0270
      _ <- recordGrade("ST0270:Parcial 1", 2.1)
      _ <- recordGrade("ST0270:Parcial 2", 3.1)
      e <- recordGrade("ST0270:Parcial 3", 4.1)
    } yield e

  def testRegisterFourGrades:StateEitherIO[Eval] =
    for {
      _ <- cST0270
      _ <- recordGrade("ST0270:Parcial 1", 2.1)
      _ <- recordGrade("ST0270:Parcial 2", 3.1)
      _ <- recordGrade("ST0270:Parcial 3", 4.1)
      e <- recordGrade("ST0270:Seguimiento", 5.0)
    } yield e

  def testRegisterFiveGrades:StateEitherIO[Eval] =
    for {
      _ <- cST0270
      _ <- recordGrade("ST0270:Parcial 1", 2.1)
      _ <- recordGrade("ST0270:Parcial 2", 3.1)
      _ <- recordGrade("ST0270:Parcial 3", 4.1)
      _ <- recordGrade("ST0270:Seguimiento", 5.0)
      e <- recordGrade("ST0270:Trabajo final", 2.8)
    } yield e

  def testRegisterRepitedNameCourse:StateEitherIO[Unit] =
    for {
      _ <- cST0270
      _ <- cST0270
    } yield ()

  def testRegisterBadGradeName:StateEitherIO[Eval] =
    for {
      _ <- cST0270
      e <- recordGrade("ST0270:Evaluaciones", 2.1)
    } yield e

  def testRegisterBadGradeValue:StateEitherIO[Eval] =
    for {
      _ <- cST0270
      e <- recordGrade("ST0270:Parcial 1", -1.0)
    } yield e

  def testRegisterBadGradeValue2:StateEitherIO[Eval] =
    for {
      _ <- cST0270
      e <- recordGrade("ST0270:Parcial 1", 6.0)
    } yield e

  def launchIO[A,B](std:Student,
                    test:StateEitherIO[B],
                    f:Student => A):IO[A] =
    for {
      r <- test.runS(std).value
      rstd <- r match {
        case Right(nstd) => IO { f(nstd) }
        case Left(_)     => IO { f(std)  }
      }
    } yield (rstd)

  def launchIOValue[A,B](std:Student,
                         test:StateEitherIO[A],
                         f:A => B,
                         optValue:B):IO[B] =
    for {
      r <- test.runA(std).value
      rstd <- r match {
        case Right(nstd) => IO { f(nstd)  }
        case Left(_)     => IO { optValue }
      }
    } yield (rstd)

  def launchIOError[A](std:Student,
                       test:StateEitherIO[A]):IO[Either[String,A]] =
    for {
      r <- test.runA(std).value
    } yield r

 "A course that was added to an student" - {
   "contains only one course" in {
     launchIO(Student(),
              testRecordOneCourse,
              ((s:Student) => s.courses.size)).asserting(_ shouldBe 1)
    }
  }

 "Two courses that were added to an student" - {
   "contains two courses" in {
     launchIO(Student(),
              testRecordTwoCourses,
              ((s:Student) => s.courses.size)).asserting(_ shouldBe 2)
   }
 }

 "Register a grade in an specific course" - {
   "obtain a posible grade" in {
     val res = 2.1 * 0.2
     launchIOValue(Student(),
                   testRegisterOneGrade,
                   ((e:Eval) => e.evaluatedGrade),
                   -1.0).asserting(_ shouldBe res)
   }
 }

 "Register two grades in an specific course" - {
   "obtain a posible grade" in {
     val res = 2.1 * 0.2 + 3.1 * 0.2
     launchIOValue(Student(),
                   testRegisterTwoGrades,
                   ((e:Eval) => e.evaluatedGrade),
                   -1.0).asserting(_ shouldBe res)
   }
 }

 "Register three grades in an specific course" - {
   "obtain a posible grade" in {
     val res = 2.1 * 0.2 + 3.1 * 0.2 + 4.1 * 0.2
     launchIOValue(Student(),
                   testRegisterThreeGrades,
                   ((e:Eval) => e.evaluatedGrade),
                   -1.0).asserting(_ shouldBe res)
   }
 }

 "Register four grades in an specific course" - {
   "obtain a posible grade" in {
     val res = 2.1 * 0.2 + 3.1 * 0.2 + 4.1 * 0.2 + 5.0 * 0.1
     launchIOValue(Student(),
                   testRegisterFourGrades,
                   ((e:Eval) => e.evaluatedGrade),
                   -1.0).asserting(_ shouldBe res)
   }
 }

 "Register five grades in an specific course" - {
   "obtain a posible grade" in {
     val res = 2.1 * 0.2 + 3.1 * 0.2 + 4.1 * 0.2 +
     5.0 * 0.1 + 2.8 * 0.3
     launchIOValue(Student(),
                   testRegisterFiveGrades,
                   ((e:Eval) => e.evaluatedGrade),
                   -1.0).asserting(_ shouldBe res)
   }
 }

 "A bad course that was added to an student" - {
   " is bad register" in {
     launchIOError(Student(),
                   testRecordOneCourseBadly).asserting(_.isLeft shouldBe true)
    }
  }

 "A course will be grading with a bad name grade" - {
   " is must fail" in {
     launchIOError(Student(),
                   testRegisterBadGradeName).asserting(_.isLeft shouldBe true)
    }
  }

 "A course will be grading with a bad grade value < 0.0" - {
   " is must fail" in {
     launchIOError(Student(),
                   testRegisterBadGradeValue).asserting(_.isLeft shouldBe true)
    }
  }

 "A course will be grading with a bad grade value > 5.0" - {
   " is must fail" in {
     launchIOError(Student(),
                   testRegisterBadGradeValue2).asserting(_.isLeft shouldBe true)
    }
  }

 "A course will register two times " - {
   " is must fail" in {
     launchIOError(Student(),
                   testRegisterRepitedNameCourse).asserting(_.isLeft shouldBe true)
    }
  }
}
