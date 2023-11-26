package com.asusoft.mvvmproject.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asusoft.mvvmproject.api.member.MemberDto
import com.asusoft.mvvmproject.api.member.MemberRepository
import com.asusoft.mvvmproject.util.TAG
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        val memberDto = MemberDto(-1, "", id.value, pw.value)
        memberRepository.login(memberDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ memberDto ->
                autoLogin.value = !(autoLogin.value as Boolean)
                Logger.t(TAG.LOGIN).d("success login -> $memberDto")
            }, { thowable ->
                Logger.t(TAG.LOGIN).d("error -> ${thowable.localizedMessage}")
            }, { // complete

            }, { subscription ->
                subscription.request(Long.MAX_VALUE)
            })
    }

    fun signUp() {
        // TODO: - 개발
    }

}