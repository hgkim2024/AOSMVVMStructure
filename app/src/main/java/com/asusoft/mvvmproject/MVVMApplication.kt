package com.asusoft.mvvmproject

import android.app.Application
import android.content.res.Resources
import android.widget.Toast
import androidx.annotation.StringRes
import com.asusoft.mvvmproject.util.TAG
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MVVMApplication: Application() {

    init {
        instance = this
    }

    private var toast: Toast? = null

    companion object {
        lateinit var instance: MVVMApplication

        fun showToast(@StringRes stringId: Int, vararg args: Any?) {
            instance.showToast(stringId, args)
        }

        fun showErrorToast(message: String) {
            instance.showErrorToast(message)
        }
    }

    override fun onCreate() {
        super.onCreate()
        settingLogger()
    }

    private fun settingLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .tag("ASU")
            .build()

        Logger.addLogAdapter(DiskLogAdapter(formatStrategy))
    }


    fun showToast(@StringRes stringId: Int, vararg args: Any?) {
        val message: String = try {
            getString(stringId).format(*args)
        } catch (e: Resources.NotFoundException) {
            e.localizedMessage?.let { Logger.t(TAG.TOAST).e("toast error -> $it") }

            return
        }

        if (message.isEmpty()) {
            Logger.t(TAG.TOAST).e("toast error -> toast message is empty")
        } else {
            toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT);
            toast?.show()
        }
    }

    fun showErrorToast(message: String) {
        if (message.isEmpty()) {
            Logger.t(TAG.TOAST).e("toast error -> toast message is empty")
            return
        }

        if (BuildConfig.DEBUG) {
            CoroutineScope(Dispatchers.Main).launch {
                toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT);
                toast?.show()
            }
        }

        Logger.t(TAG.TOAST).e(message)
    }
}