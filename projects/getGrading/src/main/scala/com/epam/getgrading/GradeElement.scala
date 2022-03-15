package com.epam.getgrading

sealed trait GradeElement
final case class GradePercent(percent:Double) extends GradeElement
final case class GradeMap(map:Map[String,GradeElement]) extends GradeElement


object GradeElement {
  private def sumGradeMap(grades:(Double,
                                  (String,GradeElement))):Double = 
                                     grades._2._2 match {
    case GradePercente(percente) => r + p
    case gm @ GradeMap(map)      => r + sumGradeElement(gm)
  }

  def sumGradeElement(gradeElement:GradeElement):Double = gradeElement match {
    case GradePercent(percente) => p
    case GradeMap(map)          => map.foldLeft(0.0)(sumGradeMap(_,_))
  }
}
