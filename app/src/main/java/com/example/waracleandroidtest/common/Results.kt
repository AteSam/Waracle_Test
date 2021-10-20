package com.example.waracleandroidtest.common

sealed class Results<out T> {

    /**
     * Represents an operation that has completed successfully with payload of [data]
     */
    data class Ok<T>(
        val data:T
    ):Results<T>()

    /**
     * Represents an operation that has completed in failure state
     */
    data class Error<T>(
        val exception: Throwable
    ):Results<T>()
}
