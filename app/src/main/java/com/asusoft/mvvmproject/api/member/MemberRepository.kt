package com.asusoft.mvvmproject.api.member

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import javax.inject.Inject

@ViewModelScoped
class MemberRepository @Inject constructor(
    private val memberService: MemberService
) {
    private val objectMapper = ObjectMapper()

    suspend fun login(dto: MemberDto): Response<MemberDto> {
        val map = objectMapper.convertValue<Map<String, String>>(dto)
        return memberService.login(map)
    }

    suspend fun signup(dto: MemberDto): Response<Long> {
        val map = objectMapper.convertValue<Map<String, String>>(dto)
        return memberService.signUp(map)
    }
}