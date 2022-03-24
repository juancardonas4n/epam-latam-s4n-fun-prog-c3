  package com.epam.getgrading

import com.epam.getgrading.Grade._
import com.epam.getgrading.Utils._

case class Eval(evaluatedPercen:Double,
                evaluatedGrade:Double,
                percenNotEvaluated:Double,
                expectedGrade:Double,
                notEvaluated:Int,
                evaluated:Int)

object Eval {
  def apply():Eval = Eval(0.0,0.0,0.0,0.0,0,0)

  def fromGradeGetEval(e:Eval,g:Grade):Eval = {
    def aux(gg:(Option[Double],Double)):Eval = (e,gg) match {
      case (ee @ Eval(_,_,pne,_,ne,n),(None, w))    =>
        ee.copy(percenNotEvaluated = pne + w,
                notEvaluated = n + 1)
      case (ee @ Eval(ep,eg,_,_,_,n),(Some(gp), w)) =>
        ee.copy(evaluatedPercen = ep + w,
                evaluatedGrade = eg + gp,
                evaluated = n + 1)
    }
    aux(getGrade(g))
  }

  def completeEvalExpectedValues(eval:Eval):Eval = eval match {
    case ceval @ Eval(_,_,pne,_,_,_)   if (pne == 0.0)              =>
      ceval
    case ceval @ Eval(ep,eg,pne,_,_,_) if (pne > 0.0 && pne <= 1.0) =>
      eval.copy(expectedGrade = if (eg >= 3.0) 0.0 else (3.0 - eg) / pne)
    case ceval @ Eval(ep,eg,pne,_,ne,e) => {
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

  def eval2String(e:Eval):String =
    f"""%%${e.evaluatedPercen * 100}%2.0f Accumulated Grade:
       | ${e.evaluatedGrade}%1.2f
       | Current Grade: ${e.evaluatedGrade / e.evaluatedPercen}%1.2f
       | Next expected grade: ${e.expectedGrade}%1.2f
       |""".stripMargin.replaceAll("\n", " ")
}
