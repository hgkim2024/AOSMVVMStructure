# Sample MVVM Architecture Project
> 2023 년에 Java 로 된 구조없는 레거시 프로젝트를 Kotlin 과 MVVM 구조를 적용하여 신규 개발이 진행되었다. 이때 모두 MVVM 을 처음 적용했었고 현재 MVVM 구조 개발이 잘되는지 확인하고 싶어 [구글 공식 Architecture](https://github.com/android/architecture-samples) 를 참고하여 Sample MVVM Architecture 를 진행하게 되었다.
<br>

## MVVM 구조 기본 설명
- 가장 대표적인 REST API 와 UI 를 적용시킨 MVVM 구조를 구상하며 개발했다.
- MVVM 은 Model, View, View Model 이다
- Model 은 REST API 를 통해 Data 를 가져와 DB 에 저장하고 이를 Model 로 부르고 View Model 에 Model(데이터) 을 넘겨준다.
- View Model 은 Model 를 통해 View 를 보여줄 때 중간 과정을 처리하는 역할을 한다.
- View 는 실제 화면을 의미한다.
<br>

## Model 샘플 코드
- REST API 호출을 통해 Model 을 받는다.
```kotlin
@ViewModelScoped
class MemberRepository @Inject constructor(
    private val memberService: MemberService
) {
    private val objectMapper = ObjectMapper()

    suspend fun login(dto: MemberModel): Response<MemberModel> {
        val map = objectMapper.convertValue<Map<String, String>>(dto)
        return memberService.login(map)
    }

    suspend fun signup(dto: MemberModel): Response<Long> {
        val map = objectMapper.convertValue<Map<String, String>>(dto)
        return memberService.signUp(map)
    }
}
```
<br>

## View Model 샘플 코드
- Repository 에서 Model 데이터를 가져와 처리한다.
- LiveData 를 사용해 XML 과 상호작용하여 UI 가 변하도록 처리한다.
```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {
    ...

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
}
```
<br>

## View 샘플 코드
- xml binding 하여 View Model 과 View 를 연결한다.
- Activity 에서 처리되어야 하는 로직이 있는 경우 Event 를 받아 처리한다.
```kotlin
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setUpObserve()
    }

    private fun setUpObserve() {
        viewModel.startSignUpEvent.observe(
            this,
            EventObserver {
                Logger.t(TAG.SIGN_UP).d("go to SignUpActivity screen")
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        )
    }
}
```
