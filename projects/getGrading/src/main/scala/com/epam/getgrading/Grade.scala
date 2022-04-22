package com.epam.getgrading

import com.epam.getgrading.Utils._
import org.typelevel.paiges._
import cats.effect.IO
import cats.effect.implicits._
import scala.Numeric.Implicits._

sealed trait Grade {
  def name:String
  def grade:Option[Double]
  def subGrades:Map[String,Grade]
}

final case class WeightedGrade(name:String,
                               grade:Option[Double],
                               subGrades:Map[String,Grade],
                               weight:Double) extends Grade

final case class NoWeightedGrade(name:String,
                                 grade:Option[Double],
                                 subGrades:Map[String,Grade]) extends Grade

object Grade {
  def apply(name:String):Grade =
    NoWeightedGrade(name,
                    None,
                    Map[String,Grade]())

  def apply(name:String, map:Map[String,Grade]):Grade =
    NoWeightedGrade(name,
                    None,
                    map)

  def apply(name:String, weight:Double):Grade =
    WeightedGrade(name,
                  None,
                  Map[String,Grade](),
                  weight)

  def apply(name:String, weight:Double, map:Map[String,Grade]):Grade =
    WeightedGrade(name,
                  None,
                  map,
                  weight)

  private def testGrade(grade:Grade):ErrorOr[Unit] =
    grade.grade match {
      case None => Right( () )
      case _    => Left(s"Grade: $grade.name already has note $grade.grade")
    }

  def setGrade(grade:Grade, actGrade:Double):ErrorOr[Grade] = {
    for {
      _ <- testGrade(grade)
      ng = grade match {
        case wn  @ WeightedGrade(_,_,_,_)  => wn.copy(grade = Some(actGrade))
        case nwn @ NoWeightedGrade(_,_,_)  => nwn.copy(grade = Some(actGrade))
      }
    } yield ng
  }

  def getGrade(g:Grade,total:Int):(Option[Double],Double) = g match {
    case WeightedGrade(_,None,_,w)       => (None,w)
    case WeightedGrade(_,Some(gp),_,w)   => (Some(gp*w),w)
    case NoWeightedGrade(_,None,_)       => (None,1.0/total.toDouble)
    case NoWeightedGrade(_,Some(gp),_)   => {
      val w = 1.0/total.toDouble
      (Some(gp*w),w)
    }
  }

  def isWeightedGrade(map:Map[String,Grade]):Boolean = {
    def isWeightedGradeAux(elem:(String,Grade)) = elem._2 match {
      case WeightedGrade(_,_,_,_) => true
      case _                      => false
    }
    !map.takeWhile(isWeightedGradeAux(_)).isEmpty
  }

  def isNoWeightedGrade(map:Map[String,Grade]):Boolean = {
    def isNoWeightedGradeAux(elem:(String,Grade)) = elem._2 match {
      case NoWeightedGrade(_,_,_) => true
      case _                      => false
    }
    !map.dropWhile(isNoWeightedGradeAux(_)).isEmpty
  }

  def sumMapWeighted(m:Map[String,Grade]):Double = {
    def sumMapWeightedAux(g:Grade):Double = g match {
      case WeightedGrade(_,_,mp,w) if mp.isEmpty => w
      case WeightedGrade(_,_,mp,_)               => sumMapWeighted(mp)
      case _                                    => ???
    }
    m.foldLeft(0.0)((t,es) => t + sumMapWeightedAux(es._2))
  }

  def sumMapElems(m:Map[String,Grade]):Int = {
    def sumMapElemsAux(g:Grade):Int = g match {
      case NoWeightedGrade(_,_,mp) if mp.isEmpty    => 1
      case NoWeightedGrade(_,_,mp)                  => sumMapElems(mp)
      case _                                        => ???
    }
    m.foldLeft(0)((t,es) => t + sumMapElemsAux(es._2))
  }

  def grade2Doc(grade:Grade):Doc = {
    val prefix = Doc.spaces(10)
    val inner  = Doc.spaces(2)
    grade match {
      case WeightedGrade(name,None,_,_)                 =>
      prefix + Doc.text(f"${name}%-20s") + inner + Doc.text("*.**")
      case WeightedGrade(name,Some(grade),_,_)          =>
      prefix + Doc.text(f"${name}%-20s") + inner + Doc.text(f"${grade}%1.2f")
      case NoWeightedGrade(name,None,_)                 =>
      prefix + Doc.text(f"${name}%-20s") + inner + Doc.text("*.**")
      case NoWeightedGrade(name,Some(grade),_)          =>
      prefix + Doc.text(f"${name}%-20s") + inner + Doc.text(f"${grade}%1.2f")
      case _                                            => ???
    }
  }
}
