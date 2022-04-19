package com.epam.getgrading

import com.epam.getgrading.Utils._
import com.epam.getgrading.Grade.{setGrade,
                                  isWeightedGrade,
                                  grade2String,
                                  sumMapWeighted,
                                  grade2Doc}
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
    lazy val sum = sumMapWeighted(grades)
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

  def grading(course:Course,
              strGrade:String,
              grade:Double):ErrorOrIO[Course] = {

    if (course.grades.contains(strGrade)) {
      val ngrades = course.grades.updatedWith(strGrade)(g => for {
        gp <- g
        ngp <- setGrade(gp, grade).toOption
      } yield ngp)
      if (ngrades.size == course.grades.size)
        EitherT.liftF(course match {
          case c @ CCourse(_,_,_,_) => IO { c.copy(grades = ngrades) }
        })
      else {
        EitherT.left[Course](IO { s"""Grade: $strGrade
                                     | has been already registed
                                     | """.stripMargin.replaceAll(eol, " ") })
      }
    }
    else
      EitherT.left[Course](IO { s"""Course: $course
                                   |Grade: $strGrade doesn't exists at Course
                                   | ${course.name}
                                   |""".stripMargin.replaceAll(eol, " ") })
  }



  def getGradeCourse(course:Course):ErrorOrIO[Eval] = {
    val ef = course.grades.foldLeft(Eval())((r,e) => fromGradeGetEval(r,e._2))
    EitherT.liftF( IO { completeEvalExpectedValues(ef) } )
  }

  def course2String(course:Course):String = course match {
    case CCourse(name,creditNumber,state,grades) =>
      s"Course: ${name} Credits: ${creditNumber} Status: ${state}" +
    eol + grades.foldLeft("")((r,e) => r + grade2String(e._2) + eol)
  }

  def course2Doc(course:Course):Doc = course match {
    case CCourse(name,creditNumber,state,grades) =>
      Doc.text("Course:") + Doc.space + Doc.text(s"${name}") +
    Doc.space + Doc.text(s"Credits: ${creditNumber}") +
    Doc.space + Doc.text(s"Status: ${state}") +
    Doc.line +
    Doc.space + Doc.spaces(10) + Doc.text("_" * 20) +
    Doc.spaces(2) + Doc.text("_" * 4) +
    Doc.line +
    grades.foldLeft(Doc.empty)((r,e) => r + grade2Doc(e._2) + Doc.line) +
    Doc.line
  }
}
