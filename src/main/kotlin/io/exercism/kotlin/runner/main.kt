package io.exercism.kotlin.runner

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Test
import org.junit.runner.JUnitCore

private val moshi = Moshi.Builder()
    .add(object : JsonAdapter<ExecutionStatus>() {

        @FromJson
        override fun fromJson(reader: JsonReader): ExecutionStatus? =
            null
        //ExecutionStatus.fromValue(reader.nextString())

        @ToJson
        override fun toJson(writer: JsonWriter, value: ExecutionStatus?) {
            writer.value(value?.value)
        }
    })
    .add(KotlinJsonAdapterFactory())
    .build()

class TestTest {

    @Test
    fun a() {
        Assert.assertEquals(2, 1 + 1)
    }

    @Test
    fun b() {
        Assert.assertEquals(5, 2 + 2)
    }
}

fun main(args: Array<String>) {
    val arguments = parseArguments(args)

    val result = executeTests(arguments)
    publishResult(result, arguments)
}

private fun executeTests(args: ExecutionArguments) {
    val junit = JUnitCore()
    //junit.addListener(TextListener(System.out))
    val result = junit.run(TestTest::class.java)

    println("""
        Executed tests.
        Succeeded: ${result.runCount - result.failureCount}
        Failed: ${result.failureCount}
        Total: ${result.runCount}
        
        Time: ${result.runTime}ms
    """.trimIndent())
}

private fun publishResult(result: Unit, args: ExecutionArguments) {
    val executionResult = ExecutionResult(
        status = ExecutionStatus.Success,
        message = "",
        tests = emptyList()
    )

    moshi.adapter(ExecutionResult::class.java)
        .toJson(executionResult)
        .let(::println)
}
