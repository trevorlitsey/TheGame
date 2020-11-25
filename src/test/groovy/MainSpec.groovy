import com.company.app.Main
import spock.lang.Specification

class MainSpec extends Specification {
    def "test main should run without crashing"() {
        when:
        Main.main()

        then:
        noExceptionThrown()
    }
}
