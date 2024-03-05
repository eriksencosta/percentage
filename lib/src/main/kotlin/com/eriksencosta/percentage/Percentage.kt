package com.eriksencosta.percentage

import java.io.Serializable
import java.math.RoundingMode
import kotlin.math.abs

/**
 * Represents a [percentage](https://en.wikipedia.org/wiki/Percentage) number, a numerical value divided by 100.
 *
 * When creating a `Percentage`, just pass the desired percentage value. Example:
 *
 *     val percentage = Percentage(50) // Which is equivalent to 50%
 *     val number = 1000
 *     percentage * number             // Results: 500.0
 *
 * You can multiply the `Percentage` with another numerical (Number) value. You can also query the percentage value
 * and its decimal value using the public immutable values:
 *
 *     percentage.value    // Results: 50.0
 *     percentage.decimal  // Results: 0.5
 *     println(percentage) // Prints:  50%
 *
 * @param[value]     The numerical value of the [Percentage].
 * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
 *                   The rounding is always done using `RoundingMode.HALF_UP`.
 *
 * TODO: expose rounding option? Create a type with precision and rounding?
 */
class Percentage(value: Number, private val precision: Int? = null) : Comparable<Percentage>, Serializable {
    /**
     * The percentage value.
     */
    val value: Double = value.toDouble()

    /**
     * The percentage value divided by 100 and optionally rounded using the [precision] scale.
     */
    val decimal: Double = (this.value / 100).let { result ->
        precision?.let { result.toBigDecimal().setScale(it, RoundingMode.HALF_UP).toDouble() } ?: result
    }

    /**
     * true if the `Percentage` is zero.
     */
    val isZero: Boolean = 0.0 == decimal

    /**
     * true if the `Percentage` is not zero.
     */
    val isNotZero: Boolean = !isZero

    /**
     * true if the `Percentage` is positive.
     */
    val isPositive: Boolean = 0 < decimal

    /**
     * true if the `Percentage` is positive or zero.
     */
    val isPositiveOrZero: Boolean = isPositive || isZero

    /**
     * true if the `Percentage` is negative.
     */
    val isNegative: Boolean = 0 > decimal

    /**
     * true if the `Percentage` is negative or zero.
     */
    val isNegativeOrZero: Boolean = isNegative || isZero

    companion object {
        /**
         * Creates a `Percentage` based on the ratio of two numbers.
         *
         * Example:
         *
         *     val x = Percentage.of(1, 5)
         *     println(x)                  // Prints: 20%
         *
         * @param[number]    The first number.
         * @param[other]     The second number.
         * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
         *
         * @throws[ArgumentCannotBeZero] When the second number is zero.
         *
         * @return A [Percentage] that represents the ratio of [number] and [other].
         */
        fun ratioOf(number: Number, other: Number, precision: Int? = null): Percentage =
           requireNonZero(other, "other").run {
               Percentage(number.toDouble() / other.toDouble() * 100, precision)
           }

        /**
         * Creates a `Percentage` which represents the percentage change of an initial and ending numbers.
         *
         * Example:
         *
         *     val x = Percentage.changeOf(1, 5)
         *     println(x)                        // Prints: 400%
         *
         * When the initial number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for
         * this case [is not defined](https://en.wikipedia.org/wiki/Relative_change).
         *
         * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
         * `Percentage(NaN)` (which would happen as a result o dividing 0 by 0, both computationally and
         * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
         *
         * @param[initial]   The initial number.
         * @param[ending]    The ending number.
         * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
         *
         * @throws[ArgumentCannotBeZero] When the initial number is zero.
         *
         * @return A [Percentage] that represents the percentage change of an initial and ending numbers.
         */
        fun relativeChange(initial: Number, ending: Number, precision: Int? = null): Percentage =
            when {
                0 == initial && 0 == ending -> Percentage(0, precision)
                else -> {
                    requireNonZero(initial, "initial")

                    val initialValue = initial.toDouble()
                    Percentage((ending.toDouble() - initialValue) / abs(initialValue) * 100, precision)
                }
            }
    }

    /**
     * Returns a new `Percentage` based on this one with a new precision.
     *
     * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
     *
     * @return A [Percentage] with the precision scale.
     */
    infix fun with(precision: Int): Percentage = if (this.precision == precision) this
        else Percentage(value, precision)

    /**
     * Calculates the number that the passed number represents as the current `Percentage`.
     *
     * Example:
     *
     *     Percentage(20).numberOf(5) // Results: 25 as 5 is 20% of 25
     *
     * @param[number] The number represented by this [Percentage].
     *
     * @throws[OperationUndefinedForZero] When the [Percentage] value is zero.
     *
     * @return The number that the passed number represents as the current [Percentage].
     */
    infix fun valueWhen(number: Number): Double
        =
        checkNonZero(decimal) { "This operation cannot execute when Percentage is zero" }.run {
            number.toDouble() / decimal
        }

    /**
     * Returns this `Percentage` as a positive value.
     *
     * @return A positive [Percentage] object.
     */
    operator fun unaryPlus(): Percentage = if (isPositive) this
        else Percentage(value * -1, precision)

    /**
     * Returns this `Percentage` after applying a negation.
     *
     * @return A [Percentage] object with the negation applied.
     */
    operator fun unaryMinus(): Percentage = Percentage(value * -1, precision)

    /**
     * Multiplies this `Percentage` by a number.
     *
     * @param[number] A number.
     *
     * @return The resulting product.
     */
    operator fun times(number: Number): Double = number.toDouble() * decimal

    /**
     * Sums a number with its calculated proportional part according to this `Percentage`.
     *
     * @param[number] A number.
     *
     * @return The sum of the number with its calculated proportional part.
     */
    operator fun plus(number: Number): Double = number.toDouble().let { whole ->
        whole + whole * decimal
    }

    /**
     * Subtracts a number with its calculated proportional part according to this `Percentage`.
     *
     * @param[number] A number.
     *
     * @return The subtraction of the number with its calculated proportional part.
     */
    operator fun minus(number: Number): Double = number.toDouble().let { whole ->
        whole - whole * decimal
    }

    override fun compareTo(other: Percentage): Int = decimal.compareTo(other.decimal)

    override fun equals(other: Any?): Boolean = this === other ||
        (other is Percentage && value == other.value && decimal == other.decimal && precision == other.precision)

    override fun hashCode(): Int = decimal.hashCode()

    override fun toString(): String = when {
        null == precision || 0 > precision -> "%.0f%%"
        else -> "%.${precision}f%%"
    }.format(value)
}