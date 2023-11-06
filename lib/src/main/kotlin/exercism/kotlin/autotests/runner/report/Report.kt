package exercism.kotlin.autotests.runner.report

/** Report summary */
data class Report(
    val version: Int,
    val status: Status,
    val message: String?,
    val tests: List<TestEntry>
) {

    /** Testing process status. [id] is used as value for `results.json`. */
    enum class Status(val id: String) {
        Pass("pass"), Fail("fail"), Error("error");

        companion object {
            fun fromId(value: String?): Status? {
                value ?: return null

                return values().find { it.id == value }
            }
        }
    }

    data class TestEntry(
        val name: String,
        val status: Status,
        val message: String,
        val output: String
    )
}
