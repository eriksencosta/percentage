package com.eriksencosta.percentage

import java.util.Objects
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
 * @param[value]    The numerical value of the [Percentage].
 * @param[rounding] The strategy to round the decimal (value / 100) representation of the [Percentage].
 */
@Suppress("TooManyFunctions")
class Percentage private constructor(value: Number, private val rounding: Rounding) : Comparable<Percentage> {
    /**
     * The percentage value.
     */
    val value: Double = value.toDouble()

    /**
     * The percentage value divided by 100 and optionally rounded using [rounding].
     */
    val decimal: Double = rounding.round(this.value / PERCENT)

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
        private const val PERCENT: Double = 100.0

        fun of(value: Number): Percentage = of(value, Rounding.default())

        fun of(value: Number, precision: Int): Percentage = of(value, Rounding.of(precision))

        fun of(value: Number, rounding: Rounding): Percentage = Percentage(value, rounding)

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
         *
         * @throws[IllegalArgumentException] When the second number is zero.
         *
         * @return A [Percentage] that represents the ratio of [number] and [other].
         */
        fun ratioOf(number: Number, other: Number): Percentage =
            ratioOf(number, other, Rounding.default())

        fun ratioOf(number: Number, other: Number, precision: Int): Percentage =
            ratioOf(number, other, Rounding.of(precision))

        fun ratioOf(number: Number, other: Number, rounding: Rounding): Percentage =
            require(0 != other) { "The argument \"other\" can not be zero" }.run {
                of(number.toDouble() / other.toDouble() * PERCENT, rounding)
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
         * '@param precision' The precision scale to round the decimal (value / 100) representation of the [Percentage].
         *
         * @throws[IllegalArgumentException] When the initial number is zero.
         *
         * @return A [Percentage] that represents the percentage change of an initial and ending numbers.
         */
        fun relativeChange(initial: Number, ending: Number): Percentage =
            relativeChange(initial, ending, Rounding.default())

        fun relativeChange(initial: Number, ending: Number, precision: Int): Percentage =
            relativeChange(initial, ending, Rounding.of(precision))

        fun relativeChange(initial: Number, ending: Number, rounding: Rounding): Percentage = when {
            0 == initial && 0 == ending -> of(0, rounding)
            else -> {
                require(0 != initial) { "The argument \"initial\" can not be zero" }

                val initialValue = initial.toDouble()
                of((ending.toDouble() - initialValue) / abs(initialValue) * PERCENT, rounding)
            }
        }
    }

    /**
     * Returns a new `Percentage` based on this one with a new precision.
     *
     * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
     *
     * @return A [Percentage] with the precision scale.
     *
     * TODO: replace with precision: Int with Rounding
     */
    infix fun with(precision: Int): Percentage = with(rounding.with(precision))

    infix fun with(rounding: Rounding): Percentage = of(value, rounding)

    /**
     * Calculates the number that the passed number represents as the current `Percentage`.
     *
     * Example:
     *
     *     Percentage(20).numberOf(5) // Results: 25 as 5 is 20% of 25
     *
     * @param[number] The number represented by this [Percentage].
     *
     * @throws[IllegalStateException] When the [Percentage] value is zero.
     *
     * @return The number that the passed number represents as the current [Percentage].
     */
    infix fun valueWhen(number: Number): Double =
        check(0.0 != decimal) { "This operation can not execute when Percentage is zero" }.run {
            number.toDouble() / decimal
        }

    /**
     * Returns this `Percentage` as a positive value.
     *
     * @return A positive [Percentage] object.
     */
    operator fun unaryPlus(): Percentage = if (isPositive) this else of(value * -1, rounding)

    /**
     * Returns this `Percentage` after applying a negation.
     *
     * @return A [Percentage] object with the negation applied.
     */
    operator fun unaryMinus(): Percentage = of(value * -1, rounding)

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

    override fun compareTo(other: Percentage): Int = decimal.compareTo(other.decimal) // we disregard the precision

    override fun equals(other: Any?): Boolean = this === other ||
        (other is Percentage && value == other.value && decimal == other.decimal && rounding == other.rounding)

    override fun hashCode(): Int = Objects.hash(decimal, rounding)

    override fun toString(): String = "${rounding.roundingFormat()}%%".format(value)
}
