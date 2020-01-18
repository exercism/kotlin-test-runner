package exercism.kotlin.autotests.runner.report

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

private val moshi = Moshi.Builder()
    .add(Report.Status::class.java, Report.Status.moshiAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

fun Report.asJson(): String {
    return moshi.adapter(Report::class.java)
        .indent("  ")
        .toJson(this)
}

private fun Report.Status.Companion.moshiAdapter() = object : JsonAdapter<Report.Status>() {

    override fun fromJson(reader: JsonReader): Report.Status? =
        fromId(reader.nextString())

    override fun toJson(writer: JsonWriter, status: Report.Status?) {
        status ?: return

        writer.value(status.id)
    }
}
