package com.epam.getgrading

import com.epam.getgrading.Utils._
import com.epam.getgrading.Grade._
import com.epam.getgrading.Eval._

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
            creditNumber:Int):Course =
    CCourse(name, creditNumber, OnRun, Map[String,Grade]())

  def apply(name:String,
            creditNumber:Int,
            grades:Set[String]):Course = {
    val listGrades:Map[String,Grade] =
      grades.foldLeft(Map[String,Grade]())((m:Map[String,Grade],
                                            gradeName:String) =>
                                              m + (gradeName ->
                                                   Grade(gradeName)))
    CCourse(name, creditNumber, OnRun, listGrades)
  }

  def apply(name:String,
            creditNumber:Int,
            grades:List[(String,Double)]):Either[String, Course] = {
    val sum = grades.foldLeft(0.0)((r:Double,e:(String,Double)) =>
      r + e._2)
    if (sum.toInt != 1)
      Left(s"The sum of weights each course must be equals to 1.0 but $sum")
    else {
      val listGrades:Map[String,Grade] =
        grades.foldLeft(Map[String,Grade]())((m:Map[String,Grade],
                                              grW:(String,Double)) =>
                                                m + (grW._1 -> Grade(grW._1,
                                                                     grW._2)))
      Right(CCourse(name, creditNumber, OnRun, listGrades))
    }
  }

  def grading(course:Course,
              strGrade:String,
              grade:Double):ErrorOr[Course] = {

    if (course.grades.contains(strGrade)) {
      val ngrades = course.grades.updatedWith(strGrade)(g => for {
        gp <- g
        ngp <- setGrade(gp, grade).toOption
      } yield ngp)
      Right(course match {
        case c @ CCourse(_,_,_,_) => c.copy(grades = ngrades)
      })
    }
    else
      Left(s"Grade: $strGrade doesn't exists at Course ${course.name}")
  }

  def getGradeCourse(course:Course):ErrorOr[Eval] = {
    val e = course.grades.foldLeft(Eval())((r,e) => getGC(r,e._2))
    Right(getFGC(e))
  }
}
