package com.example.buyonmars.models.repository

import com.example.buyonmars.models.dto.ApiResource
import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ApiResource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ApiResource.success(body)
            }

            return error(" ${response.code()} ${response.message()} ${response.errorBody()?.string()}", response.code())
        } catch (e: Exception) {
            return error(e.message ?: e.toString(), 0)
        }
    }

    private fun <T> error(message: String, code: Int? = null): ApiResource<T> {
        return ApiResource.error("Network call has failed for a following reason: $message", code = code)
    }

    private fun <T> error(code: Int, message: String): ApiResource<T> {
        return ApiResource.error(code, "Network call has failed for the following reason: $message")
    }
}