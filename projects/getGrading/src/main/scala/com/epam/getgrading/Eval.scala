package com.epam.getgrading

import com.epam.getgrading.Grade._

case class Eval(cEval:Double,
                cGrade:Double,
                cNEval:Double,
                cExpG:Double,
                nEvd:Int,
                eVd:Int)

object Eval {
  def apply():Eval = Eval(0.0,0.0,0.0,0.0,0,0)

  def getGC(e:Eval,g:Grade):Eval = {
    def aux(gg:(Option[Double],Double)):Eval = (e,gg) match {
      case (ee @ Eval(_,_,cne,_,ne,n),(None, w))    => ee.copy(cNEval = cne + w,
                                                               nEvd = n + 1)
      case (ee @ Eval(ce,cg,_,_,_,n),(Some(gp), w)) => ee.copy(cEval = ce + gp,
                                                               cGrade = cg + w,
                                                               eVd = n + 1)
    }
    aux(getGrade(g))
  }

  def getFGC(e:Eval):Eval = e match {
    case ee @ Eval(_,_,cn,_,_,_)   if (cn == 0.0) => ee
    case ee @ Eval(ce,cg,cn,_,_,_) if (cn > 0.0 && cn <= 1.0) =>
      ee.copy(cExpG = if (ce >= 3.0) 0.0 else (3.0 - ce) / cn)
    case ee @ Eval(ce,cg,cn,_,ne,e) => {
        val n = ne + e
        val ag = ce / e
        val nep = ne / n
        ee.copy(cExpG = if (ag >= 3.0) 0.0 else (3.0 - ce) / nep,
                cNEval = nep)
    }
  }

  def toString(e:Eval):String = ???
}
