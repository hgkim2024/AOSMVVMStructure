package com.asusoft.mvvmproject

import com.asusoft.mvvmproject.api.ApiResult
import com.asusoft.mvvmproject.api.error.ErrorResult
import com.google.gson.Gson
import com.orhanobut.logger.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T> Response<T>.result(): Flow<ApiResult<T & Any>> {
    val DEFAULT_FAIL_MSG = "api fail"

    return flow {
        try {
            val response = this@result
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(ApiResult.Success(body))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMsg = try {
                    Gson().fromJson(errorBody, ErrorResult::class.java).toString()
                } catch (e: Exception) {
                    errorBody ?: DEFAULT_FAIL_MSG
                }
                MVVMApplication.showErrorToast(errorMsg)
                emit(ApiResult.Error(errorMsg))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.localizedMessage ?: DEFAULT_FAIL_MSG))
        }
    }.flowOn(Dispatchers.IO)
}