package fr.lunatech.fpfpp.model

sealed trait FPFPP

object FPFPP {
  case object FaitPeur extends FPFPP
  case object FaitPasPeur extends FPFPP
}
