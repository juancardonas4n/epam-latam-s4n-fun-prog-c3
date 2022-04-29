package com.epam.getgrading

import com.epam.getgrading.Course._
import com.epam.getgrading.Grade._
import com.epam.getgrading.Utils._
import com.epam.getgrading.Student._
import cats.effect._
import scala.util.parsing.combinator._
import scala.sys.exit

class CommandParser extends JavaTokenParsers {
  def command:Parser[StateEitherIO[Boolean]] =
    course | exit | grade | list | spaces

  private def getGridQuotes(str:String) = {
    val strSize = str.size
    if (strSize >= 3)
      str.substring(1,str.size - 1)
    else ""
  }

  def course:Parser[StateEitherIO[Boolean]] =
    "add"~>stringLiteral~decimalNumber~(weightedGradings
                                        | noWeightedGradings
                                        // | pointedGradings // Keep commented
                                      ) ^^
  { case name~nCredits~gradings =>
    addCourse(getGridQuotes(name),
              nCredits.toInt,
              gradings) }

  def grade:Parser[StateEitherIO[Boolean]] =
    "grade"~>stringLiteral~( floatingPointNumber ^^ { _.toDouble } |
                             intNumber ^^ { _.toDouble } ) ^^
  { case name~grade => registerGrade(getGridQuotes(name),
                                     grade) }

  def list:Parser[StateEitherIO[Boolean]] =
    "list"~>stringLiteral ^^
  { case name => listCourse(getGridQuotes(name)) }

  def exit:Parser[StateEitherIO[Boolean]] =
    "exit" ^^ { case _ => exitApp }

  def weightedGradings:Parser[Map[String,Grade]]  =
    "["~>repsep(weightedGradeElem, ",")<~"]" ^^ (Map() ++ _)

  def weightedGradeElem:Parser[(String,Grade)] =
    (stringLiteral<~":")~(doubleNumber ^^
                          ((n) => ((s:String) =>
                            Grade(getGridQuotes(s),n)))
   | weightedGradings ^^
                          ((g) => ((s:String) =>
                            Grade(getGridQuotes(s),sumWeights(g),g)))) ^^
  { case s~f => (getGridQuotes(s),f(s)) }

  def noWeightedGradings:Parser[Map[String,Grade]] =
    "{"~>repsep(noWeightedGradeElem, ",")<~"}" ^^ (Map() ++ _)

  def noWeightedGradeElem:Parser[(String,Grade)] =
    stringLiteral~opt(":"~>noWeightedGradings) ^^
  { case s~o => { val name = getGridQuotes(s)
                 o match {
                   case Some(m) => (name, Grade(name,m))
                   case None    => (name, Grade(name))
                 }
               }
  }

  def pointedGradings:Parser[Map[String,Grade]] =
    "("~>repsep(pointedGradeElem, ",")<~")" ^^ (Map() ++ _)

  def pointedGradeElem:Parser[(String,Grade)] =
    (stringLiteral<~":")~(intNumber ^^
                          ((n) => ((s:String) =>
                            Grade(getGridQuotes(s),n)))
    | pointedGradings ^^
                          ((g) => ((s:String) =>
                            Grade(getGridQuotes(s),sumWeights(g).toInt,g)))) ^^
  { case s~f => (getGridQuotes(s),f(s)) }

  def doubleNumber:Parser[Double] =
    floatingPointNumber ^^
  { _.toDouble }

  def intNumber:Parser[Int] =
    decimalNumber ^^
  { _.toInt }

  def spaces:Parser[StateEitherIO[Boolean]] =
    """ *""".r ^^
  { (x) => liftResult( true ) }
}

object CommandParser extends CommandParser {

  def parseCommand(str:String):StateEitherIO[Boolean] =
    parseAll(command, str) match {
      case Success(res,_) => res
      case Failure(msg,_) => liftMsgError(msg)
      case _              => liftMsgError("Parser: Unknow error")
    }
}
