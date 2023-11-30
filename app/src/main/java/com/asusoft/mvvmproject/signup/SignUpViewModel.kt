package com.asusoft.mvvmproject.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusoft.mvvmproject.api.ApiResult
import com.asusoft.mvvmproject.api.member.MemberDto
import com.asusoft.mvvmproject.api.member.MemberRepository
import com.asusoft.mvvmproject.result
import com.asusoft.mvvmproject.util.TAG
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
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
        // TODO: - 데이터 유효성 체크
        viewModelScope.launch {
            val createMemberDto = MemberDto(-1, nickname.value, id.value, pw.value)
            val signUpInfo = "sign up -> name: ${nickname.value} id: ${id.value}, pw: ${pw.value}\n"

            memberRepository.signup(createMemberDto).result()
                .catch { throwable ->
                    Logger.t(TAG.LOGIN).e("${signUpInfo}exception sign up -> ${throwable.localizedMessage}")
                }.collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            Logger.t(TAG.LOGIN).d("${signUpInfo}success sign up -> ${result.data}")
                        }

                        is ApiResult.Error -> {
                            Logger.t(TAG.LOGIN).e("${signUpInfo}error sign up -> ${result.data}")
                        }

                        is ApiResult.Loading -> {
                            Logger.t(TAG.LOGIN).e("${signUpInfo}loading sign up -> ${result.data}")
                        }
                    }
                }
//            memberRepository.signup(createMemberDto)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { response ->
//                        if (response.isSuccessful) {
//                            Logger.t(TAG.LOGIN).d("${signUpInfo}success sign up -> ${response.body()}")
//                        } else {
//                            Logger.t(TAG.LOGIN).e("${signUpInfo}error sign up -> ${response.errorBody()?.string()}")
//                        }
//                    }, { throwable ->
//                        Logger.t(TAG.LOGIN).e("${signUpInfo}exception sign up -> ${throwable.localizedMessage}")
//                    }
//                )
        }

    }
}