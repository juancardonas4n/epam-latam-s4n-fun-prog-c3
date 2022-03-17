package com.epam.getgrading

import com.epam.getgrading.Course.{apply,grading,getGradeCourse}
import com.epam.getgrading.Eval
import com.epam.getgrading.Utils._
import cats.syntax.either
import cats.data.State
import cats.data.StateT
import cats.data.EitherT
import cats.Monad
import cats.Functor
import cats.implicits._
import cats.effect.IO
import cats.effect.implicits._

case class Student(courses:Map[String,Course])

object Student {

  def apply():Student = Student(Map[String,Course]())

  private def checkIdCourseWithFunction(idCG:Array[String],
                                        msg:String)(f:Array[String]=>Boolean):
  EitherStateIO[Unit] = for {
    s <- StateT.get[ErrorOrIO,Student]
    ift = StateT.liftF[ErrorOrIO,
                       Student,
                       Unit](EitherT.liftF(IO { () } ))
    iff = StateT.liftF[ErrorOrIO,
                       Student,
                       Unit](EitherT.left[Unit](IO {msg} ))
    r <- if (f(idCG)) ift else iff
  } yield r

  def recordGrade(idCG:String,
                  grade:Double):EitherStateIO[Eval] = for {
    s <- StateT.get[ErrorOrIO,Student]

    cs:Map[String,Course] = s.courses

    idCourseGrade:Array[String] = idCG.split(":")

    _ <- checkIdCourseWithFunction(idCourseGrade,
                  s"idCourse CourseID:grade bad format $idCG") {
      id => !(id.size == 1 || id.size > 3)
    }

    rit = (c:Course) => for {
      cr <- StateT.liftF[ErrorOrIO,
                         Student,
                         Course](grading(c,idCourseGrade(1),grade))
      _ <- StateT.modify[ErrorOrIO,
                         Student](_.copy(courses = cs + (cr.name -> cr)))
      r <- StateT.liftF[ErrorOrIO,
                        Student,
                        Eval](getGradeCourse(cr))
    } yield r

    rif = StateT.
    liftF[ErrorOrIO,
          Student,
            Eval](EitherT.
                  left[Eval](IO {
                    s"Course ${idCourseGrade(0)} not found at registered courses"
                  } ))
    r <- if (cs.contains(idCourseGrade(0))) rit(cs(idCourseGrade(0))) else rif
  } yield r


  def recordCourse(c:Course):EitherStateIO[Unit] = for {
    s <- StateT.get[ErrorOrIO,Student]
    cs = s.courses
    rit = StateT.liftF[ErrorOrIO,
                       Student,
                       Unit](EitherT.left[Unit](IO {s"Key $c.name already exists" } ))
    rif = for {
      _ <- StateT.modify[ErrorOrIO,
                         Student](_.copy(courses = cs +
                                         (c.name -> c)))
      r_ <- StateT.liftF[ErrorOrIO,
                         Student,
                         Unit](EitherT.liftF(IO { () } ))
    } yield r_
    r <- if (cs.contains(c.name)) rit else rif
  } yield r

  def addWeightedCourse(name:String,
                        nCredits:Int,
                        map:Map[String,Grade]):
  EitherStateIO[Boolean] = for {
    _ <- liftResult(println("Start register course"))
    c <- Course(name,nCredits,map)
    _ <- liftResult(println(s"Add course ${name}"))
    _ <- recordCourse(c)
    _ <- liftResult(println(s"Course $name registed"))
  } yield true

  def registerGrade(name:String,
                    grade:Double):
  EitherStateIO[Boolean] = for {
      eval <- recordGrade(name,grade.toDouble)
      _ <- liftResult[Unit](println(s"${eval.evaluatedGrade}"))
  } yield true

  def listCourse(name:String):
  EitherStateIO[Boolean] = for {
    _ <- liftResult(println("Not yet implemented"))
  } yield true

  def exitApp:EitherStateIO[Boolean] = for {
      _ <- liftResult(println("The application is ending"))
  } yield false

  // val fc = Course("ST0000", 4, List(("Parcial 1", 0.20),
  //                                   ("Parcial 2", 0.20),
  //                                   ("Parcial 2", 0.20),
  //                                   ("Seguimiento", 0.10),
  //                                   ("Trabajo final", 0.20)))

  // val cST0270 = Course("ST0270", 4, List(("Parcial 1", 0.20),
  //                                 ("Parcial 2", 0.20),
  //                                 ("Parcial 3", 0.20),
  //                                 ("Seguimiento", 0.10),
  //                                 ("Trabajo final", 0.30)))

  // val cST0275 = Course("ST0275", 5, List(("Parcial 1", 0.25),
  //                                 ("Parcial 2", 0.25),
  //                                 ("Seguimiento", 0.20),
  //                                 ("Trabajo final", 0.30)))

  // def test:EitherState[Eval] = for {
  //   c <- StateT.liftF[ErrorOr,
  //                     Student,
  //                     Course](cST0270)
  //   _ <- recordCourse(c)
  //   _ <- recordGrade("ST0270:Parcial 1", 3.1)
  //   r <- recordGrade("ST0270:Parcial 2", 3.3)
  // } yield r

  // def test2:EitherState[Eval] = for {
  //   c <- StateT.liftF[ErrorOr,
  //                     Student,
  //                     Course](cST0270)
  //   _ <- recordCourse(c)
  //   _ <- recordGrade("ST0270:Parcial 1", 3.1)
  //   _ <- recordGrade("ST0270:Parcial 2", 3.3)
  //   r <- recordGrade("ST0270:Parcial 3", 4.5)
  // } yield r

  // def test3:EitherState[Eval] = for {
  //   c <- StateT.liftF[ErrorOr,
  //                     Student,
  //                     Course](cST0270)
  //   _ <- recordCourse(c)
  //   _ <- recordGrade("ST0270:Parcial 1", 3.1)
  //   _ <- recordGrade("ST0270:Parcial 2", 3.3)
  //   r <- recordGrade("ST0270:Practica Final", 4.5)
  // } yield r
}
