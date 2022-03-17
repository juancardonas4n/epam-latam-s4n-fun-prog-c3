package com.epam.getgrading

import org.scalatest._
import flatspec._
import matchers._
import cats.syntax.either
import cats.data.State
import cats.data.StateT
import cats.data.EitherT
import cats.Monad
import cats.Functor
import cats.implicits._
import cats.effect.IO
import cats.effect.IO._
import cats.effect.implicits._
import com.epam.getgrading.Utils._
import com.epam.getgrading.Student
import com.epam.getgrading.Student.{recordCourse}
import com.epam.getgrading.Course
import com.epam.getgrading.Course._
import com.epam.getgrading.Grade
import com.epam.getgrading.Grade._

class CourseSuite extends AnyFlatSpec with should.Matchers {

  val cST0270 = 
    Course("ST0270", 4,
           Map("Parcial 1"     -> Grade("Parcial 1", 0.20),
               "Parcial 2"     -> Grade("Parcial 2", 0.20),
               "Parcial 3"     -> Grade("Parcial 3", 0.20),
               "Seguimiento"   -> Grade("Seguimiento", 0.10),
               "Trabajo final" -> Grade("Trabajo final",0.20)))

  val cST0275 = Course("ST0275", 5,
                       List(("Parcial 1", 0.25),
                            ("Parcial 2", 0.25),
                            ("Seguimiento", 0.20),
                            ("Trabajo final", 0.30)))

  // def testRecordCourse:EitherStateIO[Unit] = for {
  def testRecordCourse:EitherStateIO[Unit] = for {
    c <- cST0270
    _ <- recordCourse(c)
  } yield ()

  "A course added to Student" should "contains only one course" in {
    val std = Student()
    val test = for {
      r <- testRecordCourse.run(std).value
      rstd <- r match {
        case Right((nstd,_)) => liftResult[Student](nstd)
        case Left(_)         => liftResult[Student](std) 
      }
    } yield (rstd.courses.size should be (1)) // (rstd.courses.size should be (1))
      
  }
}
