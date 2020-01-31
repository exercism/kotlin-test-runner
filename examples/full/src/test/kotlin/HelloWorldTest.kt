import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class HelloWorldTest {

    @Test
    fun `simple test`() {
        assertEquals("Hello, World!", hello())
    }

    @Test
    @Disabled
    fun `passing test`() {
        println("I'm always passing! <3")
    }
}
