@file:Suppress("TooManyFunctions")

package com.eriksencosta.percentage

/**
 * Returns the value of this number as a `Percentage`.
 *
 * @receiver[Number]
 *
 * @return The [Percentage] value of this number.
 */
fun Number.percent(): Percentage = Percentage(this)

/**
 * Returns the value of this number as a `Percentage`.
 *
 * @receiver[Number]
 *
 * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
 *
 * @return The [Percentage] value of this number.
 */
infix fun Number.percent(precision: Int): Percentage = Percentage(this, precision)

/**
 * Returns the value of this number as a `Percentage`.
 *
 * @receiver[Number]
 *
 * @return The [Percentage] value of this number.
 */
fun Number.toPercentage(): Percentage = percent()

/**
 * Returns the value of this number as a `Percentage`.
 *
 * @receiver[Number]
 *
 * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
 *
 * @return The [Percentage] value of this number.
 */
infix fun Number.toPercentage(precision: Int): Percentage = percent(precision)

/**
 * Creates a `Percentage` based on the ratio of this number and other number.
 *
 * Example:
 *
 *     val x = 1.ratioOf(5)
 *     println(x)           // Prints: 20%
 *
 * Or using the infix notation:
 *
 *     1 ratioOf 5
 *
 * @receiver[Number]
 *
 * @param[other] The other number.
 *
 * @throws[IllegalArgumentException] When the `other` number is zero.
 *
 * @return A [Percentage] that represents the ratio of this number and the `other` number.
 *
 * @see Percentage.ratioOf
 */
infix fun Number.ratioOf(other: Number): Percentage = Percentage.ratioOf(this, other)

/**
 * Creates a `Percentage` based on the ratio of this number and other number.
 *
 * Example:
 *
 *     val x = 1.ratioOf(5)
 *     println(x)           // Prints: 20%
 *
 * Or using the infix notation:
 *
 *     1 ratioOf 5
 *
 * @receiver[Number]
 *
 * @param[other]     The other number.
 * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
 *
 * @throws[IllegalArgumentException] When the other number is zero.
 *
 * @return A [Percentage] that represents the ratio of this number and the `other` number.
 *
 * @see Percentage.ratioOf
 */
fun Number.ratioOf(other: Number, precision: Int): Percentage = Percentage.ratioOf(this, other, precision)

/**
 * Creates a `Percentage` which represents the percentage change of this number and other number.
 *
 * Example:
 *
 *     val x = 1.changeOf(5)
 *     println(x)            // Prints: 400%
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
 *
 * TODO: I'm undecided if this should throw the OperationUndefinedForZero exception (invariant unsatisfied when
 *       `this = 0 && other != 0`) or just throw the Percentage's ArgumentCanNotBeZero exception (currently implemented).
 */
infix fun Number.relativeChange(other: Number): Percentage = Percentage.relativeChange(this, other)

/**
 * Creates a `Percentage` which represents the percentage change of this number and other number.
 *
 * Example:
 *
 *     val x = 1.changeOf(5)
 *     println(x)            // Prints: 400%
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
 * @param[other]     The other number.
 * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
 *
 * @throws[IllegalArgumentException] When this number is zero.
 *
 * @return A [Percentage] that represents the percentage change of this number and the other number.
 *
 * TODO: I'm undecided if this should throw the OperationUndefinedForZero exception (invariant unsatisfied when
 *       `this = 0 && other != 0`) or just throw the Percentage's ArgumentCanNotBeZero exception (currently implemented).
 */
fun Number.relativeChange(other: Number, precision: Int): Percentage = Percentage.relativeChange(this, other, precision)

/**
 * Calculates the number that this number represents as the given `Percentage`.
 *
 * Example:
 *
 *     val x = 5
 *     x.of(Percentage(20)) // Results: 25 as 5 is 20% of 25
 *
 * Or using the infix notation:
 *
 *     x of 20.percent()
 *
 * @receiver[Number]
 *
 * @param[percentage] The [Percentage] value of this number.
 *
 * @throws[IllegalStateException] When the [Percentage] value is zero.
 *
 * @return The number that represents the [Percentage] of this number.
 *
 * TODO: I'm undecided if this should throw the ArgumentCanNotBeZero exception (invariant unsatisfied when
 *       `percentage = 0`) or just throw the Percentage's OperationUndefinedForZero exception (currently implemented).
 */
infix fun Number.valueWhen(percentage: Percentage): Double = percentage.valueWhen(this)

/**
 * Calculates the proportional part of this number according to a `Percentage`.
 *
 * @receiver[Number]
 *
 * @param[percentage] The [Percentage] to calculate the proportional part of this number.
 *
 * @return The proportional part of this number.
 *
 * @see Percentage.times
 */
operator fun Number.times(percentage: Percentage): Double = percentage * this

/**
 * Sums this number with the calculated proportional part according to a `Percentage`.
 *
 * @receiver[Number]
 *
 * @param[percentage] The [Percentage] to calculate the proportional part of this number.
 *
 * @return The sum of this number with its calculated proportional part.
 *
 * @see Percentage.plus
 */
operator fun Number.plus(percentage: Percentage): Number = percentage + this

/**
 * Subtracts this number with the calculated proportional part according to a `Percentage`.
 *
 * @receiver[Number]
 *
 * @param[percentage] The [Percentage] to calculate the proportional part of this number.
 *
 * @return The subtraction of this number with its calculated proportional part.
 *
 * @see Percentage.minus
 */
operator fun Number.minus(percentage: Percentage): Number = percentage - this
