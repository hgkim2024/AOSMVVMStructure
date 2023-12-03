package com.asusoft.mvvmproject.api.error

data class ErrorResult (
    val code: String,
    val message: String
) {
    override fun toString(): String {
        return "[$code] $message"
    }
}