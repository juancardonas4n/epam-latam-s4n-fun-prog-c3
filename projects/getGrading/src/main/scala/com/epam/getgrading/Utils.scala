package com.epam.getgrading

import cats.syntax.either
import cats.data.StateT

object Utils {

type Error = String
type ErrorOr[A] = Either[Error,A]
type EitherState[A] = StateT[ErrorOr,Student,A]
}
