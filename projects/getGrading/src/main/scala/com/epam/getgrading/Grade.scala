package com.epam.getgrading

import com.epam.getgrading.Utils._
import org.typelevel.paiges._
import cats.effect.IO
import cats.effect.implicits._

trait Grade {
  def name:String
  def grade:Option[Double]
  def subGrades:Map[String,Grade]
  def testGrade:ErrorOr[Unit] = this.grade match {
    case None => Right( () )
    case _    => Left(s"Grade: $grade.name already has note $grade.grade")
  }
  def updateWithGradeValue(newGradeValue:Double):ErrorOr[Grade]
  def getGrade(total:Double):(Option[Double],Double)
  def getWeight:Double
  def grade2Doc:Doc
}

import com.epam.getgrading.Grade._

final case class WeightedGrade(name:String,
                               grade:Option[Double],
                               subGrades:Map[String,Grade],
                               weight:Double) extends Grade {
  def updateWithGradeValue(newGradeValue:Double):ErrorOr[Grade] = for {
    _ <- testGrade
    _ <- testGradeRange(newGradeValue)
    ng = this.copy(grade = Some(newGradeValue))
  } yield ng

  def getGrade(total:Double):(Option[Double],Double) = {
    val w = this.weight
    this.grade match {
      case None     => (None, w)
      case Some(gp) => (Some(gp*w), w)
    }
  }

  def getWeight = weight

  def grade2Doc:Doc = {
    val prefix = Doc.spaces(10)
    val inner  = Doc.spaces(2)
    prefix + Doc.text(f"${name}%-20s") + inner +
    (this.grade match {
      case None         => Doc.text("*.**")
      case Some(grade)  => Doc.text(f"${grade}%1.2f")
    })
  }
}

final case class NoWeightedGrade(name:String,
                                 grade:Option[Double],
                                 subGrades:Map[String,Grade]) extends Grade {
  def updateWithGradeValue(newGradeValue:Double):ErrorOr[Grade] = for {
    _ <- testGrade
    _ <- testGradeRange(newGradeValue)
    ng = this.copy(grade = Some(newGradeValue))
  } yield ng
  def getGrade(total:Double):(Option[Double],Double) = {
    val w = 1.0 / total
    this.grade match {
      case None     => (None, w)
      case Some(gp) => (Some(gp*w),w)
    }
  }

  def getWeight = 1.0

  def grade2Doc:Doc = {
    val prefix = Doc.spaces(10)
    val inner  = Doc.spaces(2)
    prefix + Doc.text(f"${name}%-20s") + inner +
    (this.grade match {
      case None         => Doc.text("*.**")
      case Some(grade)  => Doc.text(f"${grade}%1.2f")
    })
  }
}

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

  def isWeightedGrade(map:Map[String,Grade]):Boolean = {
    def isWeightedGradeAux(elem:(String,Grade)) = elem._2 match {
      case WeightedGrade(_,_,_,_) => true
      case _                      => false
    }
    !map.takeWhile(isWeightedGradeAux(_)).isEmpty
  }

  def sumWeights(m:Map[String,Grade]):Double = {
    def sumWeightsAux(g:Grade):Double = {
      val mp = g.subGrades
      if (mp.isEmpty) g.getWeight
      else sumWeights(mp)
    }
    m.foldLeft(0.0)((t,es) => t + sumWeightsAux(es._2))
  }

  def testGradeRange(grade:Double):ErrorOr[Unit] = {
    if (grade > 5.0 || grade < 0.0)
      Left(s"""Incorrect grade value $grade
           |Grade must be a value between 0.0 and 5.0
           |""".stripMargin.replaceAll(eol, " "))
    else
      Right( () )
  }
}
