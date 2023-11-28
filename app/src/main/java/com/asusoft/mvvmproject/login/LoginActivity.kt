package com.asusoft.mvvmproject.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.asusoft.mvvmproject.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.databinding.DataBindingUtil
import com.asusoft.mvvmproject.databinding.ActivityLoginBinding
import com.asusoft.mvvmproject.signup.SignUpActivity
import com.asusoft.mvvmproject.util.EventObserver

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
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        )
    }
}