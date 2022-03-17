package com.epam.getgrading

import com.epam.getgrading.Course._
import com.epam.getgrading.Grade._
import com.epam.getgrading.Utils._
import com.epam.getgrading.Student._
import cats.effect._
import scala.util.parsing.combinator._
import scala.sys.exit

class CommandParser extends JavaTokenParsers {
  def command:Parser[EitherStateIO[Boolean]] = course | exit | grade | list
  def course:Parser[EitherStateIO[Boolean]] =
    "add"~>stringLiteral~decimalNumber~gradings ^^
  { case name~nCredits~gradings => addWeightedCourse(name,
                                                     nCredits.toInt,
                                                     gradings) }
  def grade:Parser[EitherStateIO[Boolean]] =
    "grade"~>stringLiteral~floatingPointNumber ^^
  { case name~grade => registerGrade(name,
                                     grade.toDouble) }
  def list:Parser[EitherStateIO[Boolean]] = "list"~>stringLiteral ^^
  { case name => listCourse(name) }
  def exit:Parser[EitherStateIO[Boolean]] = "exit" ^^
  { case _ => exitApp }
  def gradings:Parser[Map[String,Grade]]  = "["~repsep(gradeElem, ",")~"]" ^^
  { case "["~ms~"]" => Map() ++ ms
    case _          => Map() }

  def gradeElem:Parser[(String,Grade)] = (stringLiteral<~":")~
  (number ^^ ((n) => ((s:String) => Grade(s,n)))
   | gradings ^^ ((g) => ((s:String) => Grade(s,sumMapWeight(g),g)))) ^^
  { case s~f => (s,f(s)) }

  def number:Parser[Double] = floatingPointNumber ^^
  (x => x.toDouble)
}

object CommandParser extends CommandParser {
  def parseCommand(str:String):ParseResult[Any] = parseAll(command, str)
}
