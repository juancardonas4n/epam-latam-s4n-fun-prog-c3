package com.epam.getgrading

import com.epam.getgrading.Utils._
import com.epam.getgrading.Grade.{setGrade,isWeightGrade,
                                  grade2String,sumMapWeight}
import com.epam.getgrading.Eval._
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
    val sum = sumMapWeight(grades)
    if (isWeightGrade(grades) && !equalsDouble(sum,1.0)) {
      liftMsgError[Course](s"The sum of weights each course must be equals to 1.0 but $sum" )
    }
    else
      liftResult(CCourse(name, creditNumber, OnRun, grades))
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
        EitherT.left[Course](IO { s"Grade: $strGrade has been already registed " } )
      }
    }
    else
      EitherT.left[Course](IO { s"Course: $course Grade: $strGrade doesn't exists at Course ${course.name}" } )
  }

  def getGradeCourse(course:Course):ErrorOrIO[Eval] = {
    val ef = course.grades.foldLeft(Eval())((r,e) => fromGradeGetEval(r,e._2))
    EitherT.liftF( IO { completeEvalExpectedValues(ef) } )
  }

  def course2String(course:Course):String = course match {
    case CCourse(name,creditNumber,state,grades) =>
      s"Course: ${name} Credits: ${creditNumber} ${state}\n" +
    grades.foldLeft("")((r,e) => r + grade2String(e._2) + "\n")
  }
}
