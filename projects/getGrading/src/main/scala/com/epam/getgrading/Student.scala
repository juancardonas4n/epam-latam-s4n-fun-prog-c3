package com.epam.getgrading

import com.epam.getgrading.Course.{apply,grading}
import com.epam.getgrading.Utils._
import cats.syntax.either
import cats.data.State
import cats.data.StateT
import cats.Monad
import cats.Functor
import cats.implicits._

case class Student(courses:Map[String,Course])

object Student {

  def apply():Student = Student(Map[String,Course]())

  private def checkCGS(idCG:Array[String],
                       msg:String)(f:Array[String]=>Boolean)
  :EitherState[Unit] = for {
    s <- StateT.get[ErrorOr,Student]
    ift = StateT.liftF[ErrorOr,
                       Student,
                       Unit](Right(()))
    iff = StateT.liftF[ErrorOr,
                       Student,
                       Unit](Left(msg))
    r <- if (f(idCG)) ift else iff
  } yield r

  def regGrade(idCG:String,
               grade:Double):EitherState[Unit] = for {
    s <- StateT.get[ErrorOr,Student]
    cs:Map[String,Course] = s.courses
    idCourseGrade:Array[String] = idCG.split(":")
    _ <- checkCGS(idCourseGrade,
                  s"idCourse CourseID:grade bad format $idCG") {
      id => !(id.size == 1 || id.size > 3)
    }
    rit = (c:Course) => for {
      cr <- StateT.liftF[ErrorOr,
                         Student,
                         Course](grading(c,idCG,grade))
      r_ <- StateT.modify[ErrorOr,
                          Student](_.copy(courses = cs + (cr.name -> cr)))
    } yield r_

    rif = StateT.liftF[ErrorOr,
                       Student,
                       Unit](Left(s"Course ${idCourseGrade(0)} not found at registered courses"))
    r <- if (cs.contains(idCourseGrade(0))) rit(cs(idCourseGrade(0))) else rif
  } yield r

  def regCourse(c:Course):EitherState[Unit] = for {
    s <- StateT.get[ErrorOr,Student]
    cs = s.courses
    rit = StateT.liftF[ErrorOr,
                       Student,
                       Unit](Left(s"Key $c.name already exists"))
    rif = for {
      _ <- StateT.modify[ErrorOr,
                         Student](_.copy(courses = cs +
                                         (c.name -> c)))
      r_ <- StateT.liftF[ErrorOr,
                         Student,
                         Unit](Right(()))
    } yield r_
    r <- if (cs.contains(c.name)) rit else rif
  } yield r

  val fc = Course("ST0000", 4, List(("Parcial 1", 0.20),
                                    ("Parcial 2", 0.20),
                                    ("Parcial 2", 0.20),
                                    ("Seguimiento", 0.10),
                                    ("Trabajo final", 0.20)))

  val cST0270 = Course("ST0270", 4, List(("Parcial 1", 0.20),
                                  ("Parcial 2", 0.20),
                                  ("Parcial 3", 0.20),
                                  ("Seguimiento", 0.10),
                                  ("Trabajo final", 0.30)))

  val cST0275 = Course("ST0275", 5, List(("Parcial 1", 0.25),
                                  ("Parcial 2", 0.25),
                                  ("Seguimiento", 0.20),
                                  ("Trabajo final", 0.30)))

  def test:EitherState[Unit] = for {
    c <- StateT.liftF[ErrorOr,
                      Student,
                      Course](cST0270)
    _ <- regCourse(c)
    _ <- regGrade("ST0270:Parcial 1", 3.1)
    r <- regGrade("ST0270:Parcial 2", 3.3)
  } yield r
}
