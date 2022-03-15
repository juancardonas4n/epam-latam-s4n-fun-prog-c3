package com.epam.getgrading

import com.epam.getgrading.CommandParser._
import cats.effect._
import cats.syntax.all._
import cats.effect.IO
import cats.effect.implicits._

object Main extends IOApp {
  def evalCommand(str:String):IO[Unit] = for {
    pcr <- IO { parseCommand(str) }
    _ <- pcr match {
      case Success(r,_)   => IO { println("Valid") }
      case Failure(msg,_) => IO { println("Invalid") }
      case _              => IO { println("Error") }
    }
    } yield ()

  val bye = IO { println("Bye!") }
  def prompt(str:String) = IO {
        print(str)
        Console.flush()
      }
  val readLine:IO[String] =
    IO { Console.in.readLine }
  val promptGetReading = prompt("getGrading> ")

  def program:IO[Unit] =
    for {
      _    <- promptGetReading
      line <- readLine
      _    <- if (line != "")
        for {
          _ <- evalCommand(line)
          _ <- program
        } yield ()
              else bye
    } yield ()

  override def run(args: List[String]):IO[ExitCode] =
      for {
        _ <- program
      } yield ExitCode.Success
}
