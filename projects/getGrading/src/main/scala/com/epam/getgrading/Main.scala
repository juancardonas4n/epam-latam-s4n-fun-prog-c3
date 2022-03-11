package com.epam.getgrading

import com.epam.getgrading.CommandParser._
import cats.effect._
import cats.effect.IO
import cats.effect.implicits._

object Main extends App {
  def evalCommand(str:String):IO[Unit] = IO {
    println(s"Not yet implemented ${str}")
  }

  val program = for {
    _ <- IO { 
      print("getGrading> ")
      Console.flush()
    }
    line <- IO { Console.in.readLine }
    _ <- evalCommand(line)
  } yield ()

  program.unsafeRunSync()
}
