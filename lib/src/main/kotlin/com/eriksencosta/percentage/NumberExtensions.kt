@file:Suppress("TooManyFunctions")

package com.eriksencosta.percentage

/**
 * Creates a `Percentage` based on this number.
 *
 * @receiver[Number]
 *
 * @return The [Percentage] value of this number.
 */
fun Number.percent(): Percentage = percent(Rounding.default())

/**
 * Creates a `Percentage` based on this number and rounded according to the given `precision`.
 *
 * @receiver[Number]
 *
 * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage]. The
 *   rounding is done using [PreciseRounding] policy (i.e. rounds using [java.math.RoundingMode.HALF_UP] policy).
 *
 * @return A [Percentage] value of this number rounded according to [precision].
 */
infix fun Number.percent(precision: Int): Percentage = percent(Rounding.to(precision))

/**
 * Creates a `Percentage` based on this number and rounded according to the given `rounding` strategy.
 *
 * @receiver[Number]
 *
 * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
 *
 * @return A [Percentage] value of this number rounded according to [rounding].
 */
infix fun Number.percent(rounding: Rounding): Percentage = Percentage.of(this, rounding)

/**
 * Alias to [Number.percent].
 */
fun Number.toPercentage(): Percentage = percent()

/**
 * Alias to [Number.percent].
 */
infix fun Number.toPercentage(precision: Int): Percentage = percent(precision)

/**
 * Alias to [Number.percent].
 */
infix fun Number.toPercentage(rounding: Rounding): Percentage = percent(rounding)

/**
 * Creates a `Percentage` based on the ratio of this number and other number.
 *
 * Example:
 *
 *     val x = 1.ratioOf(5)
 *     println(x) // Prints: 20.000000%
 *
 * Or using the infix notation:
 *
 *     1 ratioOf 5
 *
 * @receiver[Number]
 *
 * @param[other] The other number.
 *
 * @throws[IllegalArgumentException] When `other` is zero.
 *
 * @return A [Percentage] that represents the ratio of this number and the `other` number.
 *
 * @see Percentage.ratioOf
 */
infix fun Number.ratioOf(other: Number): Percentage = ratioOf(other, Rounding.default())

/**
 * Creates a `Percentage` based on the ratio of this number and other number. The `Percentage` is rounded according to
 * the given `precision`.
 *
 * Example:
 *
 *     val x = 1.ratioOf(5, 2)
 *     println(x) // Prints: 20.00%
 *
 * @receiver[Number]
 *
 * @param[other]     The other number.
 * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage]. The
 *   rounding is done using [PreciseRounding] policy (i.e. rounds using [java.math.RoundingMode.HALF_UP] policy).
 *
 * @throws[IllegalArgumentException] When `other` is zero.
 *
 * @return A [Percentage] that represents the ratio of this number and the `other` number, and rounded according to
 *   [precision].
 *
 * @see Percentage.ratioOf
 */
fun Number.ratioOf(other: Number, precision: Int): Percentage = ratioOf(other, Rounding.to(precision))

/**
 * Creates a `Percentage` based on the ratio of this number and other number. The `Percentage` is rounded according to
 * the given `rounding`.
 *
 * Example:
 *
 *     val x = 1.ratioOf(5, Rounding.to(2, RoundingMode.HALF_DOWN))
 *     println(x) // Prints: 20.00%
 *
 * @receiver[Number]
 *
 * @param[other]    The other number.
 * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
 *
 * @throws[IllegalArgumentException] When `other` is zero.
 *
 * @return A [Percentage] that represents the ratio of this number and the `other` number, and rounded according to
 *   [rounding].
 *
 * @see Percentage.ratioOf
 */
fun Number.ratioOf(other: Number, rounding: Rounding): Percentage = Percentage.ratioOf(this, other, rounding)

/**
 * Creates a `Percentage` which represents the relative change of this number to another number.
 *
 * Example:
 *
 *     val x = 1.changeOf(5)
 *     println(x) // Prints: 400.000000%
 *
 * Or using the infix notation:
 *
 *     1 changeOf 5
 *
 * When this Number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for this case
 * [is not defined](https://en.wikipedia.org/wiki/Relative_change).
 *
 * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
 * `Percentage(NaN)` (which would happen as a result o dividing 0 by 0, both computationally and
 * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
 *
 * @receiver[Number]
 *
 * @param[other] The other number.
 *
 * @throws[IllegalArgumentException] When this number is zero.
 *
 * @return A [Percentage] that represents the percentage change of this number and the other number.
 */
infix fun Number.relativeChange(other: Number): Percentage = relativeChange(other, Rounding.default())

/**
 * Creates a `Percentage` which represents the relative change of this number to another number. The `Percentage` is
 * rounded according to the given `precision`.
 *
 * Example:
 *
 *     val x = 1.changeOf(5, 2)
 *     println(x) // Prints: 400.00%
 *
 * When this Number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for this case
 * [is not defined](https://en.wikipedia.org/wiki/Relative_change).
 *
 * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
 * `Percentage(NaN)` (which would happen as a result o dividing 0 by 0, both computationally and
 * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
 *
 * @receiver[Number]
 *
 * @param[other]     The other number.
 * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage]. The
 *   rounding is done using [PreciseRounding] policy (i.e. rounds using [java.math.RoundingMode.HALF_UP] policy).
 *
 * @throws[IllegalArgumentException] When this number is zero.
 *
 * @return A [Percentage] that represents the percentage change of this number and the other number,  and rounded
 * according to [precision].
 */
fun Number.relativeChange(other: Number, precision: Int): Percentage = relativeChange(other, Rounding.to(precision))

/**
 * Creates a `Percentage` which represents the relative change of this number to another number. The `Percentage` is
 * rounded according to the given `precision`.
 *
 * Example:
 *
 *     val x = 1.changeOf(5, Rounding.to(2, RoundingMode.HALF_DOWN))
 *     println(x) // Prints: 400.00%
 *
 * When this Number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for this case
 * [is not defined](https://en.wikipedia.org/wiki/Relative_change).
 *
 * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
 * `Percentage(NaN)` (which would happen as a result o dividing 0 by 0, both computationally and
 * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
 *
 * @receiver[Number]
 *
 * @param[other]    The other number.
 * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
 *
 * @throws[IllegalArgumentException] When this number is zero.
 *
 * @return A [Percentage] that represents the percentage change of this number and the other number,  and rounded
 * according to [rounding].
 */
fun Number.relativeChange(other: Number, rounding: Rounding): Percentage = Percentage
    .relativeChange(this, other, rounding)

/**
 * Calculates the base value of this number for the given `Percentage`. This method helps to answer the question:
 * "5 is 20% of what number?" Example:
 *
 * Example:
 *
 *     val x = 5
 *     x.of(Percentage(20)) // Results: 25.0 as 5 is 20% of 25
 *
 * Or using the infix notation:
 *
 *     x of 20.percent()
 *
 * @receiver[Number]
 *
 * @param[percentage] The [Percentage] value of that this number represents.
 *
 * @throws[IllegalStateException] When the [Percentage] value is zero.
 *
 * @return The number that this number represents as the given [Percentage].
 */
infix fun Number.valueWhen(percentage: Percentage): Double = percentage valueWhen this

/**
 * Multiplies this number by the given `Percentage`.
 *
 * @receiver[Number]
 *
 * @param[percentage] The [Percentage] to multiply this number by.
 *
 * @return The resulting value.
 *
 * @see Percentage.times
 */
operator fun Number.times(percentage: Percentage): Double = percentage * this

/**
 * Increases this number by the given `Percentage`.
 *
 * @receiver[Number]
 *
 * @param[percentage] The [Percentage] to increase this number by.
 *
 * @return The resulting value.
 *
 * @see Percentage.increase
 */
infix fun Number.increaseBy(percentage: Percentage): Number = percentage increase this

/**
 * Decreases this number by the given `Percentage`.
 *
 * @receiver[Number]
 *
 * @param[percentage] The [Percentage] to decrease this number by.
 *
 * @return The resulting value.
 *
 * @see Percentage.decrease
 */
infix fun Number.decreaseBy(percentage: Percentage): Number = percentage decrease this
