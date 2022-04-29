package com.epam.getgrading

import cats.syntax.either
import cats.data.EitherT
import cats.data.StateT
import cats.effect.IO
import cats.effect.implicits._

object Utils {

  type Error            = String
  type ErrorOr[A]       = Either[Error,A]
  type ErrorOrIO[A]     = EitherT[IO,Error,A]
  type StateEitherIO[A] = StateT[ErrorOrIO,Student,A]

  def liftMsgError[A](msg:String):StateEitherIO[A] =
    StateT.liftF[ErrorOrIO,
                 Student,
                 A](EitherT.left[A](IO { msg } ))

  def liftResult[A](result:A):StateEitherIO[A] =
    StateT.liftF[ErrorOrIO,
                 Student,
                 A](EitherT.liftF(IO { result } ))

  def liftResult1[A](result:ErrorOrIO[A]):StateEitherIO[A] =
    StateT.liftF[ErrorOrIO,
                 Student,
                 A](result)

  def equalsWithEpsilon(a:Double,b:Double)(epsilon:Double):Boolean =
    scala.math.abs(a - b) < epsilon

  val equalsDouble = (a:Double,b:Double) =>
    equalsWithEpsilon(a,b)(0.00001d)

  val eol = sys.props("line.separator")
}
