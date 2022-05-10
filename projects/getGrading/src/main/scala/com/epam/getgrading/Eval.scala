package com.epam.getgrading

import com.epam.getgrading.Grade._
import com.epam.getgrading.Utils._
import org.typelevel.paiges._

case class Eval(evaluatedPercen:Double,
                evaluatedGrade:Double,
                percenNotEvaluated:Double,
                expectedGrade:Double,
                notEvaluated:Int,
                evaluated:Int,
                total:Int)

object Eval {
  def apply():Eval = Eval(0.0,0.0,0.0,0.0,0,0,0)
  def apply(total:Int):Eval = Eval(0.0,0.0,0.0,0.0,total,0,total)

  def fromGradeGetEval(e:Eval,g:Grade):Eval = {
    def aux(gg:(Option[Double],Double)):Eval = (e,gg) match {
      case (ee @ Eval(_,_,pne,_,ne,n,_),(None, w))    =>
        ee.copy(percenNotEvaluated = pne + w,
                notEvaluated = n + 1)
      case (ee @ Eval(ep,eg,_,_,_,n,_),(Some(gp), w)) =>
        ee.copy(evaluatedPercen = ep + w,
                evaluatedGrade = eg + gp,
                evaluated = n + 1)
    }
    aux(g.getGrade(e.total.toDouble))
  }

  def completeEvalExpectedValues(eval:Eval):Eval = eval match {
    case ceval @ Eval(_,_,pne,_,_,_,_)   if (pne == 0.0)              =>
      ceval
    case ceval @ Eval(ep,eg,pne,_,_,_,_) if (pne > 0.0 && pne <= 1.0) =>
      eval.copy(expectedGrade = if (eg >= 3.0) 0.0 else (3.0 - eg) / pne)
    case ceval @ Eval(ep,eg,pne,_,ne,e,_)                             => {
      val n = ne + e
      val ag = eg / e
      val nep = ne / n
      ceval.copy(expectedGrade = if (ag >= 3.0) 0.0 else (3.0 - eg) / nep,
                 percenNotEvaluated = nep)
    }
  }

  def setNextCourseState(curState:CourseState,
                         e:Eval):CourseState = curState match {
    case cs @ OnRun  => if (e.expectedGrade > 5.0)
      Failed
    else if (e.evaluatedGrade >= 3.0 &&
             equalsDouble(e.evaluatedPercen,1.0))
      Success
    else
      cs
    case cs @ _ => cs
  }

  private def computeExpectedPoints(e:Eval):Int = {
    val remainPoints = e.total * e.percenNotEvaluated
    val fac = e.expectedGrade / 5.0
    (remainPoints * fac).toInt
  }

  def eval2Doc(e:Eval):Doc =
    Doc.text(f"%%${e.evaluatedPercen * 100}%2.0f") +
  Doc.space +
  Doc.text(f"Accumulated Grade: ${e.evaluatedGrade}%1.2f") +
  Doc.space +
  Doc.text(f"""Current Course Grade:
              |${e.evaluatedGrade / e.evaluatedPercen}%1.2f""") +
  Doc.space +
  (if (e.total < 11) Doc.text(f"Next expected grade: ${e.expectedGrade}%1.2f")
   else
     Doc.text(f"""Expected remain points to obtain:
                 | ${computeExpectedPoints(e)}%02d""".stripMargin.replaceAll(eol, " ")))
}
