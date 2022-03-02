package com.epam.getgrading

import com.epam.getgrading.Utils._

sealed trait Grade {
  def name:String
  def grade:Option[Double]
  def subGrades:Map[String,Grade]
}

final case class WeightedNote(name:String,
                              grade:Option[Double],
                              subGrades:Map[String,Grade],
                              weight:Double) extends Grade
final case class NoWeightedNote(name:String,
                                grade:Option[Double],
                                subGrades:Map[String,Grade]) extends Grade

object Grade {
  def apply(name:String):Grade =
    NoWeightedNote(name,
                   None,
                   Map[String,Grade]())

  def apply(name:String, weight:Double):Grade =
    WeightedNote(name,
                 None,
                 Map[String,Grade](),
                 weight)

  def setGrade(grade:Grade, actGrade:Double):ErrorOr[Grade] = {
    def testGrade(grade:Grade):ErrorOr[Grade] =
      if (grade.grade == None) Right(grade)
      else Left(s"Grade: $grade.name already has note $grade.grade")
    for {
      g <- testGrade(grade)
      ng = g match {
        case wn  @ WeightedNote(_,_,_,_) => wn.copy(grade = Some(actGrade))
        case nwn @ NoWeightedNote(_,_,_) => nwn.copy(grade = Some(actGrade))
      }
    } yield ng
  }

  def getGrade(g:Grade):(Option[Double],Double) = g match {
    case WeightedNote(_,None,_,w)     => (None,w)
    case WeightedNote(_,Some(gp),_,w) => (Some(gp*w),w)
    case NoWeightedNote(_,Some(gp),_) => (Some(gp),1.0)
    case NoWeightedNote(_,None,_) => (None,1.0)
  }
}
