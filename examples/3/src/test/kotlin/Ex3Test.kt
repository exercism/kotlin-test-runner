import org.junit.Test
import kotlin.test.assertEquals

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
