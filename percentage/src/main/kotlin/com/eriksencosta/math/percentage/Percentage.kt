package com.eriksencosta.math.percentage

import com.eriksencosta.math.common.NoRounding
import com.eriksencosta.math.common.Rounding
import java.util.*
import kotlin.math.abs
import kotlin.math.round

/**
 * Represents a [percentage](https://en.wikipedia.org/wiki/Percentage) number, a numerical value divided by 100.
 *
 * To create a `Percentage`, just pass the desired percentage value to its factory method. For example, to create a
 * `Percentage` for 25%, do:
 *
 *     val percentage = Percentage.of(25)
 *
 * Then you can do percentage calculations like multiplication, increase, and decrease:
 *
 *     val number = 100
 *     percentage * number        // Results: 25.0
 *     percentage increase number // Results: 125.0
 *     percentage decrease number // Results: 75.0
 *
 * You can also query the `Percentage` value (the original number you passed to the factory method) and its decimal
 * value (the value used for the calculations):
 *
 *     percentage.value   // Results: 25.0
 *     percentage.decimal // Results: 0.25
 *
 * You can also round the percentage calculations results. Just pass the number of desired decimal places as the
 * `precision` argument:
 *
 *     val noRoundingPercentage = Percentage.of(23)
 *     val roundingPercentage = Percentage.of(23, 2)
 *
 *     val number = 57
 *     println(noRoundingPercentage * number)  // Prints: 13.110000000000001
 *     println(roundingPercentage * number)    // Prints: 13.11
 *
 * By default, the rounding mode used to round the value is [java.math.RoundingMode.HALF_UP]. If you need to use
 * another mode, use the factory method which receives a [Rounding] as an argument:
 *
 *     Percentage.of(50, Rounding.to(2, RoundingMode.HALF_DOWN))
 *
 * The `Percentage` class is immutable and thread-safe.
 *
 * @param[value]    The numerical value of the [Percentage].
 * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
 */
@Suppress("TooManyFunctions")
public class Percentage private constructor(value: Number, private val rounding: Rounding) : Comparable<Percentage> {
    /**
     * The percentage value.
     */
    public val value: Double = value.toDouble()

    /**
     * The percentage decimal value.
     */
    public val decimal: Double = this.value / PERCENT

    /**
     * true if the `Percentage` is zero.
     */
    public val isZero: Boolean = 0.0 == decimal

    /**
     * true if the `Percentage` is not zero.
     */
    public val isNotZero: Boolean = !isZero

    /**
     * true if the `Percentage` is positive.
     */
    public val isPositive: Boolean = 0 < decimal

    /**
     * true if the `Percentage` is positive or zero.
     */
    public val isPositiveOrZero: Boolean = isPositive || isZero

    /**
     * true if the `Percentage` is negative.
     */
    public val isNegative: Boolean = 0 > decimal

    /**
     * true if the `Percentage` is negative or zero.
     */
    public val isNegativeOrZero: Boolean = isNegative || isZero

    public companion object {
        private const val PERCENT: Double = 100.0
        private val noRounding: NoRounding = Rounding.no()

        /**
         * Creates a `Percentage` based on a number. Calculations using it won't be rounded.
         *
         * @return A [Percentage].
         */
        public fun of(value: Number): Percentage = of(value, noRounding)

        /**
         * Creates a `Percentage` based on a number. Calculations using it will be rounded.
         *
         * @param[precision] The precision scale to round percentage calculations. The rounding is done using
         *   the [com.eriksencosta.math.common.PreciseRounding] policy (i.e. rounds using
         *   [java.math.RoundingMode.HALF_UP] mode).
         *
         * @return A [Percentage].
         */
        public fun of(value: Number, precision: Int): Percentage = of(value, Rounding.to(precision))

        /**
         * Creates a `Percentage` based on a number. Calculations using it will be rounded.
         *
         * @param[rounding] The [Rounding] strategy to round the percentage calculations.
         *
         * @return A [Percentage].
         */
        public fun of(value: Number, rounding: Rounding): Percentage = Percentage(value, rounding)

        /**
         * Creates a `Percentage` based on the ratio of two numbers. Calculations using it won't be rounded.
         *
         * Example:
         *
         *     val x = Percentage.ratioOf(1, 5)
         *     println(x) // Prints: 20%
         *
         * @param[number] The first number.
         * @param[other]  The second number.
         *
         * @throws[IllegalArgumentException] When `other` is zero.
         *
         * @return A [Percentage] that represents the ratio of [number] and [other].
         */
        public fun ratioOf(number: Number, other: Number): Percentage = ratioOf(number, other, noRounding)

        /**
         * Creates a `Percentage` based on the ratio of two numbers. Calculations using it will be rounded.
         *
         * Example:
         *
         *     val x = Percentage.ratioOf(1, 5, 2)
         *     println(x) // Prints: 20%
         *
         * @param[number]    The first number.
         * @param[other]     The second number.
         * @param[precision] The precision scale to round percentage calculations. The rounding is done using
         *   the [com.eriksencosta.math.common.PreciseRounding] policy (i.e. rounds using
         *   [java.math.RoundingMode.HALF_UP] mode).
         *
         * @throws[IllegalArgumentException] When `other` is zero.
         *
         * @return A [Percentage] that represents the ratio of [number] and [other].
         */
        public fun ratioOf(number: Number, other: Number, precision: Int): Percentage =
            ratioOf(number, other, Rounding.to(precision))

        /**
         * Creates a `Percentage` based on the ratio of two numbers. Calculations using it will be rounded.
         *
         * Example:
         *
         *     val x = Percentage.ratioOf(1, 5, Rounding.to(2, RoundingMode.HALF_DOWN))
         *     println(x) // Prints: 20%
         *
         * @param[number]   The first number.
         * @param[other]    The second number.
         * @param[rounding] The [Rounding] strategy to round the percentage calculations.
         *
         * @return A [Percentage] that represents the ratio of [number] and [other].
         */
        public fun ratioOf(number: Number, other: Number, rounding: Rounding): Percentage =
            require(0 != other) { "The argument \"other\" can not be zero" }.run {
                of(number.toDouble() / other.toDouble() * PERCENT, rounding)
            }

        /**
         * Creates a `Percentage` which represents the relative change of an initial and ending numbers. Calculations
         * using it won't be rounded.
         *
         * Example:
         *
         *     val x = Percentage.relativeChange(1, 5)
         *     println(x) // Prints: 400%
         *
         * When the initial number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for
         * this case [is not defined](https://en.wikipedia.org/wiki/Relative_change).
         *
         * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
         * `Percentage(NaN)` (which would happen as a result of dividing 0 by 0, both computationally and
         * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
         *
         * @param[initial] The initial number.
         * @param[ending]  The ending number.
         *
         * @throws[IllegalArgumentException] When `initial` number is zero.
         *
         * @return A [Percentage] that represents the percentage change of an initial and ending numbers.
         */
        public fun relativeChange(initial: Number, ending: Number): Percentage =
            relativeChange(initial, ending, noRounding)

        /**
         * Creates a `Percentage` which represents the relative change of an initial and ending numbers. Calculations
         * using it will be rounded.
         *
         * Example:
         *
         *     val x = Percentage.relativeChange(1, 5, 2)
         *     println(x) // Prints: 400%
         *
         * When the initial number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for
         * this case [is not defined](https://en.wikipedia.org/wiki/Relative_change).
         *
         * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
         * `Percentage(NaN)` (which would happen as a result of dividing 0 by 0, both computationally and
         * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
         *
         * @param[initial]   The initial number.
         * @param[ending]    The ending number.
         * @param[precision] The precision scale to round percentage calculations. The rounding is done using
         *   the [com.eriksencosta.math.common.PreciseRounding] policy (i.e. rounds using
         *   [java.math.RoundingMode.HALF_UP] mode).
         *
         * @throws[IllegalArgumentException] When `initial` is zero.
         *
         * @return A [Percentage] that represents the percentage change of an initial and ending numbers.
         */
        public fun relativeChange(initial: Number, ending: Number, precision: Int): Percentage =
            relativeChange(initial, ending, Rounding.to(precision))

        /**
         * Creates a `Percentage` which represents the relative change of an initial and ending numbers. Calculations
         * using it will be rounded.
         *
         * Example:
         *
         *     val x = Percentage.relativeChange(1, 5, Rounding.to(2, RoundingMode.HALF_DOWN))
         *     println(x) // Prints: 400%
         *
         * When the initial number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for
         * this case [is not defined](https://en.wikipedia.org/wiki/Relative_change).
         *
         * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
         * `Percentage(NaN)` (which would happen as a result of dividing 0 by 0, both computationally and
         * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
         *
         * @param[initial]  The initial number.
         * @param[ending]   The ending number.
         * @param[rounding] The [Rounding] strategy to round the percentage calculations.
         *
         * @throws[IllegalArgumentException] When `initial` is zero.
         *
         * @return A [Percentage] that represents the percentage change of an initial and ending numbers.
         */
        public fun relativeChange(initial: Number, ending: Number, rounding: Rounding): Percentage = when {
            0 == initial && 0 == ending -> of(0, rounding)
            else -> {
                require(0 != initial) { "The argument \"initial\" can not be zero" }

                val initialValue = initial.toDouble()
                of((ending.toDouble() - initialValue) / abs(initialValue) * PERCENT, rounding)
            }
        }
    }

    /**
     * Creates a `Percentage` based on this one with a new precision. Calculations using it will be rounded.
     *
     * @param[precision] The precision scale to round percentage calculations.
     *
     * @return A [Percentage] with the precision scale.
     */
    public infix fun with(precision: Int): Percentage = with(rounding with precision)

    /**
     * Creates a `Percentage` based on this one with a new rounding strategy. Calculations using it will be rounded.
     *
     * @param[rounding] The [Rounding] strategy to round the percentage calculations.
     *
     * @return A [Percentage] with the rounding strategy.
     */
    public infix fun with(rounding: Rounding): Percentage = of(value, rounding)

    /**
     * Calculates the base value of a number for the current `Percentage`. This method helps to answer the question:
     * "5 is 20% of what number?" Example:
     *
     *     Percentage.of(20).valueWhen(5) // Results: 25.0 as 5 is 20% of 25
     *
     * @param[number] The number to find its base value when representing this [Percentage].
     *
     * @throws[IllegalStateException] When this `Percentage` value is zero.
     *
     * @return The number that the given number represents as the current [Percentage].
     */
    public infix fun valueWhen(number: Number): Double =
        check(0.0 != decimal) { "This operation can not execute when Percentage is zero" }.run {
            round { number.toDouble() / decimal }
        }

    /**
     * Creates a positive `Percentage` based on this one.
     *
     * @return A positive [Percentage] object.
     */
    public operator fun unaryPlus(): Percentage = if (isPositive) this else of(value * -1, rounding)

    /**
     * Creates a `Percentage` after negating this one.
     *
     * @return A [Percentage] object with the negation applied.
     */
    public operator fun unaryMinus(): Percentage = of(value * -1, rounding)

    /**
     * Multiplies this `Percentage` by a number.
     *
     * @param[number] A number.
     *
     * @return The resulting value.
     */
    public operator fun times(number: Number): Double = round { number.toDouble() * decimal }

    /**
     * Increases a number by this `Percentage`.
     *
     * @param[number] A number.
     *
     * @return The resulting value.
     */
    public infix fun increase(number: Number): Double = number.toDouble().let { whole ->
        round { whole + whole * decimal }
    }

    /**
     * Decreases a number by this `Percentage`.
     *
     * @param[number] A number.
     *
     * @return The resulting value.
     */
    public infix fun decrease(number: Number): Double = number.toDouble().let { whole ->
        round { whole - whole * decimal }
    }

    override fun compareTo(other: Percentage): Int = if (decimal != other.decimal)
        decimal.compareTo(other.decimal)
    else
        this.rounding.compareTo(other.rounding)

    override fun equals(other: Any?): Boolean = this === other ||
        (other is Percentage && decimal == other.decimal && rounding == other.rounding)

    override fun hashCode(): Int = Objects.hash(decimal, rounding)

    override fun toString(): String = (if (abs(value) - abs(round(value)) == 0.0) "%.0f%%" else "%.2f%%").format(value)

    private inline fun <T> T.round(block: T.() -> Double): Double = rounding.round(block(this))
}
