package com.asusoft.mvvmproject.api.member

import io.reactivex.Flowable
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface MemberService {
    @POST("member/login")
    fun login(
        @QueryMap map: Map<String, String>
    ): Flowable<MemberDto>

}