package com.asusoft.mvvmproject

import com.asusoft.mvvmproject.api.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T> Response<T>.result(): Flow<ApiResult<T & Any>> {
    return flow {
        try {
            val response = this@result
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(ApiResult.Success(body))
            } else {
                val errorBody = response.errorBody()?.string() ?: "api fail"
                // TODO: - error msg 가공하기 -> spring 에서 보내준 ErrorResult 객체 파싱해서 msg 로 변환 하기
                emit(ApiResult.Error(errorBody))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.localizedMessage ?: "api fail"))
        }
    }.flowOn(Dispatchers.IO)
}