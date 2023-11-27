package com.asusoft.mvvmproject.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusoft.mvvmproject.api.member.MemberDto
import com.asusoft.mvvmproject.api.member.MemberRepository
import com.asusoft.mvvmproject.util.TAG
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    // TODO: - 테스트 용 데이터, 테스트 완료 후 제거
    val idString = "asukim2020"
    val pwString = "1234"

    val id = MutableLiveData<String>(idString)
    val pw = MutableLiveData<String>(pwString)
    val autoLogin = MutableLiveData<Boolean>(false)

    @SuppressLint("CheckResult")
    fun login() {
        Logger.t(TAG.LOGIN).d("click login id: ${id.value}, pw: ${pw.value}")

        viewModelScope.launch {
            val memberDto = MemberDto(-1, "", id.value, pw.value)
            val response = memberRepository.login(memberDto)
            if (response.isSuccessful) {
                autoLogin.value = !(autoLogin.value as Boolean)
                Logger.t(TAG.LOGIN).d("success login -> $memberDto")
            } else {
                Logger.t(TAG.LOGIN).e("error -> ${response.body()}")
            }
        }
    }

    fun signUp() {
        // TODO: - 개발
    }

}