package com.eriksencosta.percentage

import java.math.RoundingMode

sealed class Rounding {
    abstract val precision: Int
    abstract val mode: RoundingMode

    companion object {
        fun default(mode: RoundingMode = RoundingMode.HALF_UP) = ImpreciseRounding(mode)

        fun of(precision: Int, mode: RoundingMode = RoundingMode.HALF_UP) = PreciseRounding(precision, mode)
    }

    fun with(precision: Int) = if (this.precision == precision) this else of(precision, mode)

    abstract fun round(value: Double): Double

    internal abstract fun roundingFormat(): String

    override fun equals(other: Any?): Boolean = this === other ||
        other is Rounding && precision == other.precision && mode == other.mode

    override fun hashCode(): Int = precision.hashCode()

    override fun toString(): String = "precision=%d mode=%s".format(precision, mode)
}
class ImpreciseRounding(override val mode: RoundingMode) : Rounding() {
    override val precision: Int = 0

    override fun round(value: Double): Double = value

    override fun roundingFormat(): String = "%.0f"

    override fun toString(): String = "mode=%s".format(mode)
}

class PreciseRounding(override val precision: Int, override val mode: RoundingMode) : Rounding() {
    override fun round(value: Double): Double = value.toBigDecimal().setScale(precision, mode).toDouble()

    override fun roundingFormat(): String = if (0 >= precision) "%.0f" else "%.${precision}f"
}
