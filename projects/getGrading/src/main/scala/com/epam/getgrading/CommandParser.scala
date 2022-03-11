package com.epam.getgrading

import scala.util.parsing.combinator._

sealed trait GradeElement
final case class GradePercent(percent:Double) extends GradeElement
final case class GradeMap(map:Map[String,GradeElement]) extends GradeElement

class CommandParser extends JavaTokenParsers {
  def command:Parser[Any]   = course | grade | list | exit
  def course:Parser[Any]    = "add"~>stringLiteral~decimalNumber~gradings
  def grade:Parser[Any]     = "grade"~>stringLiteral~floatingPointNumber
  def list:Parser[Any]      = "list"~>stringLiteral
  def exit:Parser[Any]      = "exit"
  def gradings:Parser[GradeElement]  = "["~repsep(gradeElem, ",")~"]" ^^
  { case "["~ms~"]" => GradeMap(Map() ++ ms)
    case _          => GradeMap(Map())
  }
  def gradeElem:Parser[(String,GradeElement)] = (stringLiteral<~":")~
  (number | gradings) ^^
  { case s~g => (s,g) }
  def number:Parser[GradeElement] = floatingPointNumber ^^
  (x => GradePercent(x.toDouble))
}

object CommandParser extends CommandParser {
  def run(str:String) = {
    println(parseAll(command, str))
  }
}
