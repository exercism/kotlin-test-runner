import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class LeapTest {
    @Test
    fun `not divisible by 4`() = assertYearIsCommon(2015)

    @Disabled
    @Test
    fun `divisible by 2, not divisible by 4`() = assertYearIsCommon(1970)

    @Disabled
    @Test
    fun `divisible by 4, not divisible by 100`() = assertYearIsLeap(1996)

    @Disabled
    @Test
    fun `divisible by 4 and 5`() = assertYearIsLeap(1960)

    @Disabled
    @Test
    fun `divisible by 100, not divisible by 400`() = assertYearIsCommon(2100)

    @Disabled
    @Test
    fun `divisible by 100 but not by 3`() = assertYearIsCommon(1900)

    @Disabled
    @Test
    fun `divisible by 400`() = assertYearIsLeap(2000)

    @Disabled
    @Test
    fun `divisible by 400 but not by 125`() = assertYearIsLeap(2400)

    @Disabled
    @Test
    fun `divisible by 200, not divisible by 400`() = assertYearIsCommon(1800)
}

private fun assertYearIsLeap(year: Int) = assertTrue(Year(year).isLeap)

private fun assertYearIsCommon(year: Int) = assertFalse(Year(year).isLeap)
