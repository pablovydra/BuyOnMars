package com.example.buyonmars.models.dto

data class ApiResource<out T>(val status: Status, val data: T?, val message: String?, val code: Int? = null) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): ApiResource<T> {
            return ApiResource(Status.SUCCESS, data, null, 0)
        }

        fun <T> error(message: String, data: T? = null, code: Int? = null): ApiResource<T> {
            return ApiResource(Status.ERROR, data, message, code)
        }

        fun <T> error(code:Int, message: String, data: T? = null): ApiResource<T> {
            return ApiResource(Status.ERROR, data, message, code)
        }

        fun <T> loading(data: T? = null): ApiResource<T> {
            return ApiResource(Status.LOADING, data, null, 0)
        }
    }
}