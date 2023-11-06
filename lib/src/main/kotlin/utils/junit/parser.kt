package utils.junit

import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.File

// JUnit4 xml-report parser

fun parseJUnit4Results(file: File): TestSuit {
    val document = SAXReader().read(file)

    return document.asTestSuit()
}

private fun Document.asTestSuit(): TestSuit {
    val suit = rootElement

    return TestSuit(
        name = suit["name"].value.removeSuffix("()"),
        tests = suit["tests"].value.toInt(),
        skipped = suit["skipped"].value.toInt(),
        failures = suit["failures"].value.toInt(),
        errors = suit["errors"].value.toInt(),
        testCases = suit.elements("testcase").map(Element::asTestCase)
    )
}

private fun Element.asTestCase(): TestCase {
    return TestCase(
        name = get("name").text,
        className = get("classname").text,
        time = get("time").text,
        isSkipped = element("skipped") != null,
        failure = element("failure")?.asFailure(),
        error = element("error")?.asError()
    )
}

private fun Element.asFailure(): TestCase.Failure {
    return TestCase.Failure(
        message = get("message").text,
        type = get("type").text,
        stackTrace = text
    )
}

private fun Element.asError(): TestCase.Error {
    return TestCase.Error(
        message = get("message").text,
        type = get("type").text,
        stackTrace = text
    )
}

data class TestSuit(
    val name: String,
    val tests: Int,
    val skipped: Int,
    val failures: Int,
    val errors: Int,

    val testCases: List<TestCase>
)

data class TestCase(
    val name: String,
    val className: String,
    val time: String,
    val isSkipped: Boolean,
    val failure: Failure?,
    val error: Error?
) {

    data class Failure(
        val message: String,
        val type: String,
        val stackTrace: String
    )

    data class Error(
        val message: String,
        val type: String,
        val stackTrace: String
    )
}

// Method for debug purposes
fun main() {
    val testSuit = parseJUnit4Results(
        File("out/__autotest_workingdir/build/test-results/test/TEST-HelloWorldTest.xml"))

    println(testSuit)
}

private operator fun Element.get(name: String): Attribute = attribute(name)
