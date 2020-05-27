package de.htwg.sa.dominion.model.cardComponent.cardBaseImpl

import de.htwg.sa.dominion.model.cardComponent.{ICard, cardBaseImpl}
import de.htwg.sa.dominion.model.cardComponent.cardBaseImpl.Cardtype.Cardtype
import play.api.libs.json.{JsValue, Json, Reads}

import scala.xml.Elem

case class Card(cardName: String, cardDescription: String, cardType: Cardtype, costValue: Int, moneyValue: Int, vpValue: Int
                         , cardDrawValue: Int, additionalBuysValue: Int, additionalActionsValue: Int, additionalMoneyValue: Int) extends ICard {

  override def constructCardNameString(): String = {
    this.cardName
  }

  override def constructCardInformationString: String = {
    val cardInfoString = "Cardname: " + this.cardName + "\nCarddescription: " + this.cardDescription + "\nCost: " +
      +this.costValue + " Money\n" + "Money worth: " + this.moneyValue + "\nVictory Points worth: " + this.vpValue + "\n" +
      "Allows you to have " + this.additionalActionsValue + " Actions, " + this.additionalBuysValue + " Buys," + this.additionalMoneyValue + " " +
      "Additional Money and draws " + this.cardDrawValue + " more cards"
    cardInfoString
  }

  override def toJson: JsValue = Json.toJson(this)

  override def fromJson(jsValue: JsValue): ICard = {jsValue.validate[Card].asOpt.get}
}

object Card {
  import play.api.libs.json._
  implicit val cardReads: Reads[Card] = Json.reads[Card]
  implicit val cardWrites: OWrites[Card] = Json.writes[Card]
}

object Cardtype extends Enumeration {
  import play.api.libs.json._
  type Cardtype = Value
  val MONEY: cardBaseImpl.Cardtype.Value = Value("Money")
  val VICTORYPOINT: cardBaseImpl.Cardtype.Value = Value("VP")
  val KINGDOM: cardBaseImpl.Cardtype.Value = Value("Kingdom")

  implicit val format: Format[cardBaseImpl.Cardtype.Value] = Json.formatEnum(this)
}

object Cards {
  val copper: Card = Card("Copper", "1 Money", Cardtype.MONEY, 0, 1, 0, 0, 0, 0, 0)
  val silver: Card = Card("Silver", "2 Money", Cardtype.MONEY, 3, 2, 0, 0, 0, 0, 0)
  val gold: Card = Card("Gold", "3 Money", Cardtype.MONEY, 6, 3, 0, 0, 0, 0, 0)

  val estate: Card = Card("Estate", "1 Victory Point", Cardtype.VICTORYPOINT, 2, 0, 1, 0, 0, 0, 0)
  val duchy: Card = Card("Duchy", "3 Victory Points", Cardtype.VICTORYPOINT, 5, 0, 3, 0, 0, 0, 0)
  val province: Card = Card("Province", "5 Victory Points", Cardtype.VICTORYPOINT, 8, 0, 6, 0, 0, 0, 0)

  val village: Card = Card("Village", "+1 Card, +2 Actions", Cardtype.KINGDOM, 3, 0, 0, 1, 0, 2, 0)
  val festival: Card = Card("Festival", "+2 Actions, +1 Buy, +2 Money", Cardtype.KINGDOM, 5, 0, 0, 0, 1, 2, 2)
  val cellar: Card = Card("Cellar", "+1 Action, Discard any number of cards, then draw that many", Cardtype.KINGDOM, 2, 0, 0, 0, 0, 1, 0)
  val mine: Card = Card("Mine", "You may trash a Treasure from your hand. Gain a Treasure to your hand costing up to 3 more than it"
    , Cardtype.KINGDOM, 5, 0, 0, 0, 0, 0, 0)
  val smithy: Card = Card("Smithy", "+3 Cards", Cardtype.KINGDOM, 4, 0, 0, 3, 0, 0, 0)
  val remodel: Card = Card("Remodel", "Trash a card from your hand. Gain a card costing up to 2 more than it"
    , Cardtype.KINGDOM, 4, 0, 0, 0, 0, 0, 0)
  val merchant: Card = Card("Merchant", "+1 Card, +1 Action, The first time you play a Silver this turn, +1 Money"
    , Cardtype.KINGDOM, 3, 0, 0, 1, 0, 1, 0)
  val workshop: Card = Card("Workshop", "Gain a card costing up to 4", Cardtype.KINGDOM, 3, 0, 0, 0, 0, 0, 0)
  val gardens: Card = Card("Gardens", "Worth 1 WinningPoint per 10 cards you have(round down)", Cardtype.VICTORYPOINT, 4, 0, 0, 0, 0, 0, 0)
  val market: Card = Card("Market", "+1 Card, +1 Action, +1 Buy, +1 Money", Cardtype.KINGDOM, 5, 0, 0, 1, 1, 1, 1)


  //toXML Funktion
  def cardsToXml(cards: Card): Elem = {
    <card>
      <cardName>{cards.cardName}</cardName>
      <cardDescription>{cards.cardDescription}</cardDescription>
      <cardType>{cards.cardType}</cardType>
      <costValue>{cards.costValue}</costValue>
      <moneyValue>{cards.moneyValue}</moneyValue>
      <vpValue>{cards.vpValue}</vpValue>
      <cardDrawValue>{cards.cardDrawValue}</cardDrawValue>
      <additionalBuysValue>{cards.additionalBuysValue}</additionalBuysValue>
      <additionalActionsValue>{cards.additionalActionsValue}</additionalActionsValue>
      <additionalMoneyValue>{cards.additionalMoneyValue}</additionalMoneyValue>
    </card>
  }

  //fromXml Funktion
  /*def fromXML(node: scala.xml.NodeSeq, i: Int): Card = {
    // TODO add cardType
    val cardName = (node \ "cardName")(i).text.trim
    val cardDescription = (node \ "cardDescription")(i).text.trim
    val cardType = (node \ "cardType")(i).text
    val costValue = (node  \ "costValue")(i).text.toInt
    val moneyValue = (node \ "moneyValue")(i).text.toInt
    val vpValue = (node \ "vpValue")(i).text.toInt
    val cardDrawValue = (node \ "cardDrawValue")(i).text.toInt
    val additionalBuysValue = (node \ "additionalBuysValue")(i).text.toInt
    val additionalActionsValue = (node \ "additionalActionsValue")(i).text.toInt
    val additionalMoneyValue = (node \ "additionalMoneyValue")(i).text.toInt
    Card(cardName, cardDescription, cardType, costValue, moneyValue,vpValue, cardDrawValue, additionalBuysValue, additionalActionsValue, additionalMoneyValue)
  }*/

  /*def ListfromXml(node: scala.xml.NodeSeq, i: Int): List[Cards] = {
    // TODO

  }*/

  /*import play.api.libs.json._
  implicit val cardsWrites: OWrites[Card] = Json.writes[Card]
  implicit val cardsReads: Reads[Card] = Json.reads[Card]
 */
}

object Deck {
  val startDeck: List[Card] = List(Cards.estate, Cards.copper, Cards.copper, Cards.copper, Cards.copper, Cards.copper,
    Cards.copper, Cards.estate, Cards.estate, Cards.estate)

  //val startDeck : List[Card] = List(Cards.village, Cards.festival, Cards.cellar, Cards.mine, Cards.smithy, Cards.remodel,
  //Cards.merchant, Cards.workshop, Cards.market, Cards.village)


  //val startDeck : List[Card] = List(Cards.workshop, Cards.workshop, Cards.workshop, Cards.workshop, Cards.silver, Cards.silver, Cards.silver, Cards.silver, Cards.silver, Cards.silver)
}
