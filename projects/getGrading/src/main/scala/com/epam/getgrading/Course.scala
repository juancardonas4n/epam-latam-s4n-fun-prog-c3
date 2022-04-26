package com.epam.getgrading

import com.epam.getgrading.Utils._
import com.epam.getgrading.Grade.{
  isWeightedGrade,
  sumWeights
}
import com.epam.getgrading.Eval._
import org.typelevel.paiges._
import cats.data.EitherT
import cats.data.StateT
import cats.implicits._
import cats.effect.IO
import cats.effect.implicits._

sealed trait Course {
  def name:String
  def creditNumber:Int
  def state:CourseState
  def grades:Map[String,Grade]
}

final case class CCourse(name:String,
                         creditNumber:Int,
                         state:CourseState,
                         grades:Map[String,Grade]) extends Course

object Course {
  def apply(name:String,
            creditNumber:Int,
            grades:Map[String,Grade]):EitherStateIO[Course] = {
    lazy val sum = sumWeights(grades)
    if (isWeightedGrade(grades) && !equalsDouble(sum,1.0)) {
      liftMsgError[Course](s"""The sum of weights each
                              |course must be
                              |equals to 1.0
                              |but $sum""".stripMargin.replaceAll(eol, " "))
    }
    else
      liftResult(CCourse(name, creditNumber, OnRun, grades))
  }

  def updateState(course:Course,
                  newState:CourseState):Course = course match {
    case c @ CCourse(_,_,_,_) => c.copy(state = newState)
  }

  // def grading(course:Course,
  //             strGrade:String,
  //             grade:Double):ErrorOrIO[Course] = {

  //   if (course.grades.contains(strGrade)) {
  //     val ngrades = course.grades.updatedWith(strGrade)(g => for {
  //       gp <- g
  //       ngp <- setGrade(gp, grade).toOption
  //     } yield ngp)
  //     if (ngrades.size == course.grades.size)
  //       EitherT.liftF(course match {
  //         case c @ CCourse(_,_,_,_) => IO { c.copy(grades = ngrades) }
  //       })
  //     else {
  //       EitherT.left[Course](IO { s"""Grade: $strGrade
  //                                    | has been already registed
  //                                    | """.stripMargin.replaceAll(eol, " ") })
  //     }
  //   }
  //   else
  //     EitherT.left[Course](IO { s"""Course: $course
  //                                  |Grade: $strGrade doesn't exists at Course
  //                                  | ${course.name}
  //                                  |""".stripMargin.replaceAll(eol, " ") })
  // }

  def grading(course:Course,
              strGrade:String,
              grade:Double):ErrorOrIO[Course] = for {
    _ <- EitherT.cond[IO] (
      grade >= 0.0 && grade <= 5.0,
      (),
      s"""Incorrect grade value $grade
      |Grade must be a value between 0.0 and 5.0
      |""".stripMargin.replaceAll(eol, " ")
    )
    ngrades <- EitherT.cond[IO] (
      course.grades.contains(strGrade),
      course.grades.updatedWith(strGrade)(g => for {
        gp <- g
        ngp <- gp.updateWithGradeValue(grade).toOption
      } yield ngp),
      s"""Course: $course
      |Grade: $strGrade doesn't exists at Course
      | ${course.name}
      |""".stripMargin.replaceAll(eol, " "))
    ncourse <- EitherT.cond[IO] (
      ngrades.size == course.grades.size,
      course match {
          case c @ CCourse(_,_,_,_) => c.copy(grades = ngrades)
      },
      s"""Course: $course
      |Grade: $strGrade doesn't exists at Course
      | ${course.name}
      |""".stripMargin.replaceAll(eol, " "))
  } yield ncourse

  def getGradeCourse(course:Course):ErrorOrIO[Eval] = {
    val initEval =
      if (isWeightedGrade(course.grades))
        Eval()
      else Eval(sumWeights(course.grades).toInt) // Eval(sumMapElems(course.grades))
    val ef = course.grades.foldLeft(initEval)((r,e) => fromGradeGetEval(r,e._2))
    EitherT.liftF( IO { completeEvalExpectedValues(ef) } )
  }

  def recordCourse(c:Course):EitherStateIO[Unit] = for {
    s <- StateT.get[ErrorOrIO,Student]
    cs = s.courses

    rit = liftMsgError[Unit](s"Key $c.name already exists")

    rif = for {
      _ <- StateT.modify[ErrorOrIO,
                         Student](_.copy(courses = cs +
                                         (c.name -> c)))
      r <- liftResult[Unit]( () )
    } yield r
    _ <- if (cs.contains(c.name)) rit else rif
  } yield ()


  def course2Doc(course:Course):Doc = course match {
    case CCourse(name,creditNumber,state,grades) =>
      Doc.text("Course:") + Doc.space + Doc.text(s"${name}") +
    Doc.space + Doc.text(s"Credits: ${creditNumber}") +
    Doc.space + Doc.text(s"Status: ${state}") +
    Doc.line +
    Doc.space + Doc.spaces(10) + Doc.text("_" * 20) +
    Doc.spaces(2) + Doc.text("_" * 4) +
    Doc.line +
    grades.foldLeft(Doc.empty)((r,e) => r + e._2.grade2Doc + Doc.line) +
    Doc.line
  }
}
