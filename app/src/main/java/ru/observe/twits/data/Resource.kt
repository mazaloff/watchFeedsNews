package ru.observe.twits.data

data class Resource<T>(
    val status: Status,
    val data: T?,
    val exception: Exception?
) {
    enum class Status {
        LOADING,
        COMPLETED
    }
}