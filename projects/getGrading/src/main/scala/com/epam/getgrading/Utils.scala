package com.epam.getgrading

import cats.syntax.either
import cats.data.EitherT
import cats.data.StateT
import cats.effect.IO
import cats.effect.implicits._

object Utils {

type Error = String
type ErrorOr[A] = Either[Error,A]
type ErrorOrIO[A] = EitherT[IO,Error,A]
type EitherState[A] = StateT[ErrorOr,Student,A]
type EitherStateIO[A] = StateT[ErrorOrIO,Student,A]
}
