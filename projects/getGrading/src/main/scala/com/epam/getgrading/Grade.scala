package com.epam.getgrading

import com.epam.getgrading.Utils._
import org.typelevel.paiges._
import cats.effect.IO
import cats.effect.implicits._

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

  def apply(name:String, map:Map[String,Grade]):Grade =
    NoWeightedNote(name,
                   None,
                   map)

  def apply(name:String, weight:Double):Grade =
    WeightedNote(name,
                 None,
                 Map[String,Grade](),
                 weight)

  def apply(name:String, weight:Double, map:Map[String,Grade]):Grade =
    WeightedNote(name,
                 None,
                 map,
                 weight)

  def setGrade(grade:Grade, actGrade:Double):ErrorOr[Grade] = {
    def testGrade(grade:Grade):ErrorOr[Grade] = grade.grade match {
      case None => Right(grade)
      case _    => Left(s"Grade: $grade.name already has note $grade.grade")
    }
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

  def isWeightGrade(map:Map[String,Grade]):Boolean = {
    def isWeightGradeAux(elem:(String,Grade)) = elem._2 match {
      case WeightedNote(_,_,_,_) => true
      case _                     => false
    }
    !map.takeWhile(isWeightGradeAux(_)).isEmpty
  }

  def isNoWeightGrade(map:Map[String,Grade]):Boolean = {
    def isNoWeightGradeAux(elem:(String,Grade)) = elem._2 match {
      case NoWeightedNote(_,_,_) => true
      case _                     => false
    }
    !map.dropWhile(isNoWeightGradeAux(_)).isEmpty
  }

  def sumMapWeight(m:Map[String,Grade]):Double = {
    def sumMapWeightAux(g:Grade):Double = g match {
      case WeightedNote(_,_,mp,w) if mp.isEmpty => w
      case WeightedNote(_,_,mp,_)               => sumMapWeight(mp)
      case _                                    => ???
    }
    m.foldLeft(0.0)((t,es) => t + sumMapWeightAux(es._2))
  }

  def grade2String(grade:Grade):String = grade match {
    case WeightedNote(name,None,_,_)        =>
      f"\t\t${name}\t*.**"
    case WeightedNote(name,Some(grade),_,_) =>
      f"\t\t${name}\t${grade}%1.2f"
    case NoWeightedNote(name,None,_)        =>
      f"\t\t${name}\t*.**"
    case NoWeightedNote(name,Some(grade),_) =>
      f"\t\t${name}\t${grade}%1.2f"
  }

  def grade2Doc(grade:Grade):Doc = {
    val prefix = Doc.spaces(10)
    val inner  = Doc.spaces(2)
    grade match {
    case WeightedNote(name,None,_,_)        =>
      prefix + Doc.text(f"${name}%-20s") + inner + Doc.text("*.**")
    case WeightedNote(name,Some(grade),_,_) =>
      prefix + Doc.text(f"${name}%-20s") + inner + Doc.text(f"${grade}%1.2f")
    case NoWeightedNote(name,None,_)        =>
      prefix + Doc.text(f"${name}%-20s") + inner + Doc.text("*.**")
    case NoWeightedNote(name,Some(grade),_) =>
      prefix + Doc.text(f"${name}%-20s") + inner + Doc.text(f"${grade}%1.2f")
    }
  }
}
