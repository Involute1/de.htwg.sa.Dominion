package de.htwg.sa.dominion.util

import de.htwg.sa.dominion.model.cardComponent.cardBaseImpl.Card

case class DatabaseList(inhalt: List[List[Card]])

object DatabaseList {
  import play.api.libs.json._
  implicit val playerReads: Reads[DatabaseList] = Json.reads[DatabaseList]
  implicit val playerWrites: OWrites[DatabaseList] = Json.writes[DatabaseList]
}