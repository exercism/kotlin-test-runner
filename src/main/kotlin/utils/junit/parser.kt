package utils.junit

import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.File

fun parseJUnit4Results(file: File): TestSuit {
    val document = SAXReader().read(file)

    return document.asTestSuit()
}

private fun Document.asTestSuit(): TestSuit {
    val suit = rootElement

    return TestSuit(
        name = suit["name"].value,
        tests = suit["tests"].value.toInt(),
        skipped = suit["skipped"].value.toInt(),
        failures = suit["failures"].value.toInt(),
        errors = suit["errors"].value.toInt(),
        timestamp = suit["timestamp"].value,
        hostname = suit["hostname"].value,
        time = suit["time"].value,
        systemOut = suit.elementText("system-out"),
        systemErr = suit.elementText("system-err"),
        testCases = suit.elements("testcase").map(Element::asTestCase)
    )
}

private fun Element.asTestCase(): TestCase {
    return TestCase(
        name = get("name").text,
        className = get("classname").text,
        time = get("time").text,
        failure = element("failure")?.asFailure()
    )
}

private fun Element.asFailure(): TestCase.Failure {
    return TestCase.Failure(
        message = get("message").text,
        type = get("type").text
    )
}

data class TestSuit(
    val name: String,
    val tests: Int,
    val skipped: Int,
    val failures: Int,
    val errors: Int,
    val timestamp: String,
    val hostname: String,
    val time: String,
    val systemOut: String,
    val systemErr: String,

    //val properties

    val testCases: List<TestCase>
)

data class TestCase(
    val name: String,
    val className: String,
    val time: String,
    val failure: Failure?
) {

    data class Failure(
        val message: String,
        val type: String
    )
}

fun main() {
    val testSuit = parseJUnit4Results(
        File("out/__autotest_workingdir/build/test-results/test/TEST-HelloWorldTest.xml"))

    println(testSuit)
}

private operator fun Element.get(name: String): Attribute = attribute(name)
