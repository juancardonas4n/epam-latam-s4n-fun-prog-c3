package com.epam.getgrading

sealed trait CourseState

final case object OnRun    extends CourseState
final case object Success  extends CourseState
final case object Failed   extends CourseState
final case object Canceled extends CourseState
