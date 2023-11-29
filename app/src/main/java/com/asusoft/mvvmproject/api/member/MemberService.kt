package com.asusoft.mvvmproject.api.member

import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface MemberService {
    @POST("member/login")
    fun login(
        @QueryMap map: Map<String, String>
    ): Flowable<Response<MemberDto>>

    @POST("member/signup")
    fun signUp(
        @QueryMap map: Map<String, String>
    ): Flowable<Response<Long>>

}