package com.asusoft.mvvmproject.api.member

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Flowable
import javax.inject.Inject

@ViewModelScoped
class MemberRepository @Inject constructor(
    private val memberService: MemberService
) {
    private val objectMapper = ObjectMapper()

    fun login(dto: MemberDto): Flowable<MemberDto> {
        val map = objectMapper.convertValue<Map<String, String>>(dto)
        return memberService.login(map)
    }
}