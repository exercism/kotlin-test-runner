import org.junit.Test
import org.junit.Ignore
import kotlin.test.*

class HelloWorldTest {

    @Test
    fun helloWorldTest() {
        assertEquals("Hello, World!", hello())
    }

    @Test
    @Ignore
    fun `passing test`() {
        println("I'm always passing! <3")
    }
}
