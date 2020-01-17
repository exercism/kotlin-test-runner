import org.junit.Test
import org.junit.Ignore
import kotlin.test.assertEquals

class HelloWorldTest {

    @Test
    fun helloWorldTest() {
        assertEquals("Hello, World!", hello())
    }

    @Test
    @Ignore
    fun ignoredTest() {
        println("Executing ignored test")
    }
}
