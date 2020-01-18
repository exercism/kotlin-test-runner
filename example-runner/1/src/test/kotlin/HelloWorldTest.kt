import org.junit.Test
import kotlin.test.assertEquals

class HelloWorldTest {

    @Test
    fun helloWorldTest() {
        assertEquals("Hello, World!", hello())
    }

    @Test
    fun `passing test`() {
        println("I'm always passing! <3")
    }
}
