package com.eriksencosta.percentage

/**
 * Thrown to indicate that an argument can not be zero.
 */
class ArgumentCanNotBeZero internal constructor(private val argumentName: String) :
    IllegalArgumentException("The argument \"$argumentName\" can not be zero")

/*internal fun requireNonZero(value: Number, argumentName: String) {
    if (0.0 == value.toDouble()) throw ArgumentCanNotBeZero(argumentName)
}*/