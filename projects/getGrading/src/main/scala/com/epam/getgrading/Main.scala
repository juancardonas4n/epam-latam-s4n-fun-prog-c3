package com.epam.getgrading

import com.epam.getgrading.CommandParser._
import com.epam.getgrading.Utils._
import com.epam.getgrading.Student._
import cats.data.StateT
import cats.data.EitherT
import cats.effect._
import cats.syntax.all._
import cats.effect.IO
import cats.effect.IO._
import cats.effect.implicits._

object Main extends IOApp {

  def evalCommand(str:String):EitherStateIO[Boolean] = for {
    pcr <- liftResult(parseCommand(str))
    r <- pcr match {
      case Success(res,_) => liftResult(res)
      case Failure(msg,_) => liftMsgError(msg)
      case _              => liftMsgError("Parser: Unknow error")
    }
  } yield true

  def prompt(str:String) = IO {
        print(str)
        Console.flush()
      }
  def printError(msg:String) = IO {
    println(msg)
  }

  def prompt2(str:String):EitherStateIO[Unit] =
    liftResult {
      print(str)
      Console.flush()
    }

  val readLine:IO[String] =
    IO { Console.in.readLine }

  val promptGetReading = prompt("getGrading> ")

  def program(std:Student):IO[Unit] =
    for {
      _    <- promptGetReading
      line <- readLine
      _    <- if (line != "") for {
        r <- evalCommand(line).run(std).value
        _ <- r match {
          case (Right(t)) => if (t._2) program(t._1) else unit
          case (Left(msg)) => for {
              _ <- printError(msg)
              _ <- program(std)
          } yield ()
        }
      } yield ()
              else program(std)
    } yield ()

  override def run(args: List[String]):IO[ExitCode] =
    for {
      _ <- program(Student())
    } yield ExitCode.Success
}
