package com.asusoft.mvvmproject.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.asusoft.mvvmproject.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.databinding.DataBindingUtil
import com.asusoft.mvvmproject.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }

}