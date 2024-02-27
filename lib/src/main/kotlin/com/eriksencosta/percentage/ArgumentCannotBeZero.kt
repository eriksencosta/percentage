package com.eriksencosta.percentage

/**
 * Thrown to indicate that an argument cannot be zero.
 */
class ArgumentCannotBeZero internal constructor(private val argumentName: String) :
    IllegalArgumentException("The argument \"$argumentName\" cannot be zero")