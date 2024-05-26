package com.asusoft.mvvmproject.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusoft.mvvmproject.api.ApiResult
import com.asusoft.mvvmproject.api.member.MemberModel
import com.asusoft.mvvmproject.api.member.MemberRepository
import com.asusoft.mvvmproject.result
import com.asusoft.mvvmproject.util.Event
import com.asusoft.mvvmproject.util.TAG
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
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

    private val _startSignUpEvent = MutableLiveData<Event<Unit>>()
    val startSignUpEvent: LiveData<Event<Unit>> = _startSignUpEvent

    @SuppressLint("CheckResult")
    fun login() {
        viewModelScope.launch {
            val loginMemberModel = MemberModel(-1, "", id.value, pw.value)
            val loginInfo = "login -> id: ${id.value}, pw: ${pw.value}\n"

            memberRepository.login(loginMemberModel).result()
                .catch { throwable ->
                    Logger.t(TAG.LOGIN).e("${loginInfo}error login -> ${throwable.localizedMessage}")
                }.collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            Logger.t(TAG.LOGIN).d("${loginInfo}success login -> ${result.data}")
                        }

                        is ApiResult.Error,
                        is ApiResult.Loading -> {
                            Logger.t(TAG.LOGIN).e("${loginInfo}error login -> ${result.message}")
                        }
                    }
                }
        }
    }

    fun signUp() {
        _startSignUpEvent.value = Event(Unit)
    }

}