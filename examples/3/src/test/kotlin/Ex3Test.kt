import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Ex3Test {

    @Test
    fun `two plus two is four`() {
        assertEquals(twoPlusTwo(), 4)
    }

    @Test
    fun `two plus two is five`() {
        assertEquals(twoPlusTwo(), 5)
    }
}
