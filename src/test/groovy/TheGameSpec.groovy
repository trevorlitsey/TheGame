import com.company.app.Direction
import com.company.app.DiscardPile
import com.company.app.Game
import spock.lang.Specification

class TheGameSpec extends Specification {
    def "test findBestPlayPossible()"() {
        when:
        def game = new Game()
        game.hand = ArrayList.of(5, 10)
        game.discardPiles = ArrayList.of(new DiscardPile(Direction.UP), new DiscardPile(Direction.DOWN))
        def bestPlayPossible = game.findBestPlayPossible()

        then:
        bestPlayPossible != null
        bestPlayPossible.getCardIndex() == 0
        bestPlayPossible.getClosestTo() == 0
    }

}
