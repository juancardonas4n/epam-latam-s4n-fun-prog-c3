package com.epam.getgrading

import com.epam.getgrading.Course.{apply,
                                   grading,
                                   getGradeCourse,
                                   course2Doc,
                                   recordCourse,
                                   updateState}
import com.epam.getgrading.Eval.{eval2Doc,
                                 setNextCourseState}
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

  private def checkWithFunction[A](idCG:A,
                                   msg:String)(f:A=>Boolean):
  StateEitherIO[Unit] = StateT.liftF[ErrorOrIO,Student,Unit](
      EitherT.cond[IO](
        f(idCG),
        (),
        msg))

  def recordGrade(idCG:String,
                  grade:Double):StateEitherIO[Eval] = for {
    s <- StateT.get[ErrorOrIO,Student]
    cs = s.courses

    idCourseGrade:Array[String] = idCG.split(":")

    _ <- checkWithFunction(idCourseGrade,
                  s"idCourse CourseID:grade bad format $idCG") {
      id => !(id.size == 1 || id.size > 3)
    }

    _ <- checkWithFunction(idCourseGrade,
                  s"""Course id: ${idCourseGrade(0)} doesn't exist.
                     | Register it!""".stripMargin.replaceAll(eol, " ")) {
      id => s.courses.contains(id(0))
    }

    rit = (c:Course) => for {
      newc <- liftResult1[Course](grading(c,idCourseGrade(1),grade))
      eval <- liftResult1[Eval](getGradeCourse(newc))
      ns = setNextCourseState(newc.state,eval)
      newCState = updateState(newc,ns)
      _ <- StateT.modify[ErrorOrIO,
                         Student](_.copy(courses = cs +
                                         (newc.name -> newCState)))
    } yield eval

    rif = liftMsgError[Eval](s"""Course ${idCourseGrade(0)}
                                | and Grade ${idCourseGrade(1)}
                                | not found at registered
                                | courses""".stripMargin.replaceAll(eol, " "))
    r <- if (cs.contains(idCourseGrade(0))) rit(cs(idCourseGrade(0)))
         else rif
  } yield r

  def addCourse(name:String,
                nCredits:Int,
                map:Map[String,Grade]):
  StateEitherIO[Boolean] = for {
    c <- Course(name,nCredits,map)
    _ <- recordCourse(c)
    _ <- liftResult(println(s"Course $name registed"))
  } yield true

  def registerGrade(name:String,
                    grade:Double):
  StateEitherIO[Boolean] = for {
    s <- StateT.get[ErrorOrIO,Student]
    eval <- recordGrade(name,grade)
    _ <- liftResult[Unit](println(eval2Doc(eval).render(80)))
  } yield true

  def listCourse(name:String):
  StateEitherIO[Boolean] = for {
    s <- StateT.get[ErrorOrIO,Student]
    courses = s.courses
    _ <- if (courses.contains(name))
      for {
        _ <- liftResult(println(course2Doc(courses(name)).render(80)))
        eval <- liftResult1[Eval](getGradeCourse(courses(name)))
        _ <- liftResult[Unit](println(eval2Doc(eval).render(80)))
      } yield ()
        else
      liftMsgError(s"Course ${name} doesn't exits")
  } yield true

  def exitApp:StateEitherIO[Boolean] = for {
      _ <- liftResult(println("getGrading is ending"))
  } yield false
}
