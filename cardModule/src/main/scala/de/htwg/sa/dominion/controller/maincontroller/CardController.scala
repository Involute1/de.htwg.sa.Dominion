package de.htwg.sa.dominion.controller.maincontroller

import com.google.inject.Inject
import de.htwg.sa.dominion.controller.ICardController
import de.htwg.sa.dominion.model.cardFileIoComponent.ICardFileIO
import de.htwg.sa.dominion.model.cardComponent.ICard
import de.htwg.sa.dominion.model.cardComponent.cardBaseImpl.Card
import de.htwg.sa.dominion.model.cardDatabaseComponent.ICardDatabase

class CardController @Inject()(var card: ICard, fileIO: ICardFileIO, cardDbInterface: ICardDatabase) extends ICardController {

  cardDbInterface.create

  override def save(playingDecks: Option[List[List[Card]]], trash: Option[List[Card]], handCards: Option[List[Card]],
                    stackerCards: Option[List[Card]], deckCards: Option[List[Card]], playerId: Option[Int]): Unit = {
    //fileIO.save(card, "cardModule")
    cardDbInterface.update(playingDecks, trash, handCards, stackerCards, deckCards, playerId)
  }

  override def load(playerId: Option[Int]): (List[List[Card]], List[Card], List[Card], List[Card], List[Card]) = {
    /*card = fileIO.load(card, "cardModule") match {
      case Failure(_) => return
      case Success(value) => value
    }*/
    cardDbInterface.read(playerId)
  }

  override def constructCardNameString(): String = card.constructCardNameString()

  override def constructCardInfoString(): String = card.constructCardInformationString
}
