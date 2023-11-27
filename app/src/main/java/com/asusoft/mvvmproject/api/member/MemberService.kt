package com.asusoft.mvvmproject.api.member

import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface MemberService {
    @POST("member/login")
    suspend fun login(
        @QueryMap map: Map<String, String>
    ): Response<MemberDto>

    @POST("member/signup")
    suspend fun signUp(
        @QueryMap map: Map<String, String>
    ): Response<Long>

}