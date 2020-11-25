import com.company.app.Direction
import com.company.app.DiscardPile
import com.company.app.Game
import spock.lang.Specification
import spock.lang.Unroll

class TheGameSpec extends Specification {
    def "test initDeck"() {
        when:
        def game = new Game()

        then:
        game.deck.size() == 98
        game.deck.contains(2)
        game.deck.contains(3)
        game.deck.contains(31)
        game.deck.contains(51)
        game.deck.contains(78)
        game.deck.contains(98)
        game.deck.contains(99)
    }

    def "test initHand"() {
        when:
        def game = new Game()
        game.drawCards()

        then:
        game.deck.size() == 90
        game.hand.size() == 8
        game.hand.get(0) > 1
        game.hand.get(0) < 100
        game.hand.get(1) > 1
        game.hand.get(1) < 100
        game.hand.get(2) > 1
        game.hand.get(2) < 100
        game.hand.get(3) > 1
        game.hand.get(3) < 100
        game.hand.get(4) > 1
        game.hand.get(4) < 100
        game.hand.get(5) > 1
        game.hand.get(5) < 100
        game.hand.get(6) > 1
        game.hand.get(6) < 100
        game.hand.get(7) > 1
        game.hand.get(7) < 100
    }

    def "init discard piles"() {
        when:
        def game = new Game()

        then:
        game.discardPiles.size() == 4
        game.discardPiles.get(0).getDirection() == Direction.UP
        game.discardPiles.get(1).getDirection() == Direction.DOWN
        game.discardPiles.get(2).getDirection() == Direction.UP
        game.discardPiles.get(3).getDirection() == Direction.DOWN
        game.discardPiles.get(0).getTopCard() == 1
        game.discardPiles.get(1).getTopCard() == 100
        game.discardPiles.get(2).getTopCard() == 1
        game.discardPiles.get(3).getTopCard() == 100
    }

    @Unroll
    def "test findBestPlayPossible - #scenario"(scenario, hand, cardOnTop1, cardOnTop2, expectedCardIndex, expectedDeckIndexClosestTo) {
        when:
        def game = new Game()
        game.hand = hand
        def discardPile1 = new DiscardPile(Direction.UP)
        discardPile1.discard(cardOnTop1)
        def discardPile2 = new DiscardPile(Direction.DOWN)
        discardPile2.discard(cardOnTop2)
        game.discardPiles = ArrayList.of(discardPile1, discardPile2)
        def bestPlayPossible = game.findBestPlayPossible()

        then:
        bestPlayPossible != null
        bestPlayPossible.getCardIndex() == expectedCardIndex
        bestPlayPossible.getClosestTo() == expectedDeckIndexClosestTo

        where:
        scenario                                                | hand               | cardOnTop1 | cardOnTop2 | expectedCardIndex | expectedDeckIndexClosestTo
        "closest card to UP - card in asc order"                | List.of(5, 10)     | 1          | 100        | 0                 | 0
        "closest card to UP - card in desc order"               | List.of(10, 5)     | 1          | 100        | 1                 | 0
        "closest card to DOWN - card in desc order"             | List.of(98, 92)    | 1          | 100        | 0                 | 1
        "closest card to DOWN - card in asc order"              | List.of(92, 98)    | 1          | 100        | 1                 | 1
        "reverse on UP - choose over closest card to UP"        | List.of(21, 10)    | 20         | 100        | 1                 | 0
        "reverse on DOWN - choose over closest card to DOWN"    | List.of(90, 79)    | 20         | 80         | 0                 | 1
        "reverse on UP - choose over closest card to DOWN"      | List.of(98, 10)    | 20         | 99         | 1                 | 0
        "reverse on DOWN - choose over closest card to UP"      | List.of(90, 11)    | 10         | 80         | 0                 | 1
        "closest to UP - choose card less than reverse plus 10" | List.of(12, 6, 16) | 11         | 80         | 0                 | 0
    }

    def "test findBestPlayPossible - no play found"() {
        // TODO
    }

    def "test takeTurn when more than one card left in deck"() {
        setup:
        def game = new Game()
        game.hand = List.of(14, 15, 16, 17, 18, 19, 20, 85)
        game.deck = List.of(11, 12)
        def discardPile1 = new DiscardPile(Direction.UP)
        discardPile1.discard(8)
        def discardPile2 = new DiscardPile(Direction.DOWN)
        discardPile2.discard(84)
        game.discardPiles = List.of(discardPile1, discardPile2)

        when:
        game.takeTurn()

        then:
        game.hand == List.of(16, 17, 18, 19, 20, 85)
        game.discardPiles.get(0).cards.size() == 4
        game.discardPiles.get(0).getTopCard() == 15
        game.discardPiles.get(1).cards.size() == 2
    }

    def "test takeTurn when one card left in deck"() {
        setup:
        def game = new Game()
        game.hand = List.of(14, 15, 16, 17, 18, 19, 20, 85)
        game.deck = List.of()
        game.deck.add(13)
        def discardPile1 = new DiscardPile(Direction.UP)
        discardPile1.discard(8)
        def discardPile2 = new DiscardPile(Direction.DOWN)
        discardPile2.discard(84)
        game.discardPiles = List.of(discardPile1, discardPile2)

        when:
        game.takeTurn()

        then:
        game.hand == List.of(16, 17, 18, 19, 20, 85)
        game.discardPiles.get(0).cards.size() == 4
        game.discardPiles.get(0).getTopCard() == 15
        game.discardPiles.get(1).cards.size() == 2
    }

    def "test takeTurn when no cards left in deck"() {
        setup:
        def game = new Game()
        game.hand = List.of(14, 15, 16, 17, 18, 19, 20, 85)
        game.deck = List.of()
        def discardPile1 = new DiscardPile(Direction.UP)
        discardPile1.discard(8)
        def discardPile2 = new DiscardPile(Direction.DOWN)
        discardPile2.discard(84)
        game.discardPiles = List.of(discardPile1, discardPile2)

        when:
        game.takeTurn()

        then:
        game.hand == List.of(15, 16, 17, 18, 19, 20, 85)
        game.discardPiles.get(0).cards.size() == 3
        game.discardPiles.get(0).getTopCard() == 14
        game.discardPiles.get(1).cards.size() == 2
    }

    def "test playGame"() {
        // TODO
    }
}
