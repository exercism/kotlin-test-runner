package exercism.kotlin.autotests.runner.args

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File

class LaunchArgumentsKtTest : StringSpec({

    "no arguments throws error" {
        shouldThrow<IllegalStateException> {
            LaunchArguments.parseAndValidate(emptyArray())
        }
    }

    "less than three arguments throws error" {
        shouldThrow<IllegalStateException> {
            LaunchArguments.parseAndValidate(arrayOf("1"))
        }
        shouldThrow<IllegalStateException> {
            LaunchArguments.parseAndValidate(arrayOf("1", "2"))
        }
    }

    "more than three arguments throws error" {
        shouldThrow<IllegalStateException> {
            LaunchArguments.parseAndValidate(arrayOf("1", "2", "3", "4"))
        }
        shouldThrow<IllegalStateException> {
            LaunchArguments.parseAndValidate(arrayOf("1", "2", "3", "4", "5"))
        }
    }

    "arguments should be parsed and resolved" {
        val args = LaunchArguments.parseAndValidate(
            arrayOf("my-slug", "path/to/solutions", "out/"))

        args.exerciseSlug shouldBe "my-slug"
        args.solutionsDir shouldBe File(".", "path/to/solutions")
        args.resultFile shouldBe File(".", "out/results.json")
    }
})
