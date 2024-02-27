package com.eriksencosta.percentage

internal fun requireNonZero(value: Number, argumentName: String) {
    if (0.0 == value.toDouble()) throw ArgumentCannotBeZero(argumentName)
}

internal fun checkNonZero(value: Number, message: () -> String) {
    if (0.0 == value.toDouble()) throw OperationUndefinedForZero(message())
}