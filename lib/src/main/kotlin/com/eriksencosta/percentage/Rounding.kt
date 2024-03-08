package com.eriksencosta.percentage

import java.math.RoundingMode

/**
 * Strategy to round a `Percentage` decimal value to a given precision using a specified [java.math.RoundingMode]
 * policy. See [significant figures](https://en.wikipedia.org/wiki/Significant_figures) on Wikipedia for an arithmetic
 * background.
 */
sealed class Rounding {
    /**
     * The precision to round the decimal value (i.e., number of decimal places to keep).
     */
    abstract val precision: Int

    /**
     * The rounding mode used to round the decimal value.
     */
    abstract val mode: RoundingMode

    companion object {
        /**
         * Returns an `ImpreciseRounding`.
         *
         * @param[mode] The rounding mode policy to round the number.
         *
         * @return An [ImpreciseRounding] object.
         */
        fun default(mode: RoundingMode = RoundingMode.HALF_UP) = ImpreciseRounding(mode)

        /**
         * Returns a [PreciseRounding].
         *
         * @param[precision] The precision scale to round a value.
         * @param[mode]      The rounding mode policy to round the number.
         *
         * @return An [ImpreciseRounding] object.
         */
        fun to(precision: Int, mode: RoundingMode = RoundingMode.HALF_UP) = PreciseRounding(precision, mode)
    }

    /**
     * Returns a new `Rounding` with the given precision, keeping the current rounding [mode].
     *
     * @param[precision] The precision scale to round a value.
     *
     * @return A [Rounding] object.
     */
    infix fun with(precision: Int): Rounding = if (this.precision == precision) this else to(precision, mode)

    /**
     * Rounds the given value.
     *
     * @param[value] A value.
     *
     * @return A rounded value.
     */
    abstract fun round(value: Double): Double

    /**
     * Returns a template string to convert a value using the appropriate significant digits based on the current
     * [precision].
     *
     * @return A template string compatible with the [String.format] syntax.
     */
    internal abstract fun roundingFormat(): String

    override fun equals(other: Any?): Boolean = this === other ||
        other is Rounding && precision == other.precision && mode == other.mode

    override fun hashCode(): Int = precision.hashCode()

    override fun toString(): String = "precision=%d mode=%s".format(precision, mode)
}

/**
 * Strategy that does not round a value, keeping it imprecise.
 */
class ImpreciseRounding(override val mode: RoundingMode) : Rounding() {
    override val precision: Int = 0

    override fun round(value: Double): Double = value

    override fun roundingFormat(): String = "%f"

    override fun toString(): String = "mode=%s".format(mode)
}

/**
 * Strategy to round a value with precision.
 */
class PreciseRounding(override val precision: Int, override val mode: RoundingMode) : Rounding() {
    override fun round(value: Double): Double = value.toBigDecimal().setScale(precision, mode).toDouble()

    override fun roundingFormat(): String = if (0 >= precision) "%.0f" else "%.${precision}f"
}
