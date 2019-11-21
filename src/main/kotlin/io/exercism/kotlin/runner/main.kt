package io.exercism.kotlin.runner

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

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

fun main() {
    val executionResult = ExecutionResult(
        status = ExecutionStatus.Success,
        message = "",
        tests = emptyList()
    )

    moshi.adapter(ExecutionResult::class.java)
        .toJson(executionResult)
        .let(::println)
}
