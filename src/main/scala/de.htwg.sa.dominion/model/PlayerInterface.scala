package de.htwg.sa.dominion.model

import de.htwg.sa.dominion.model.playercomponent.Player

trait PlayerInterface {

  def constructPlayerNameString(): String

  def constructPlayerDeckString(): String

  def constructPlayerStackerString(): String

  def constructPlayerHandString(): String

  def createPlayer(playerCount: Int, names: List[String]): List[Player]

}
