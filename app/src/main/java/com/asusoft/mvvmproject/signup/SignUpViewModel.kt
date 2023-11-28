package com.asusoft.mvvmproject.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusoft.mvvmproject.api.member.MemberDto
import com.asusoft.mvvmproject.api.member.MemberRepository
import com.asusoft.mvvmproject.util.TAG
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    // TODO: - 테스트 용 데이터, 테스트 완료 후 제거
    val idString = "asukim20200"
    val pwString = "1234"
    val nicknameString = "AsuTest"

    val id = MutableLiveData<String>(idString)
    val pw = MutableLiveData<String>(pwString)
    val nickname = MutableLiveData<String>(nicknameString)

    fun signUp() {
        Logger.t(TAG.SIGN_UP).d("signUp -> id: ${id.value}, pw: ${pw.value}")

        // TODO: - 데이터 유효성 체크
        viewModelScope.launch {
            val createMemberDto = MemberDto(-1, nickname.value, id.value, pw.value)
            val response = memberRepository.signup(createMemberDto)
            val body = response.body()

            if (response.isSuccessful) {
                Logger.t(TAG.SIGN_UP).d("success sign up -> $body")
            } else {
                Logger.t(TAG.SIGN_UP).e("error sign up -> $body")
            }
        }

    }
}