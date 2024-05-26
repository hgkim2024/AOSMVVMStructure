package com.asusoft.mvvmproject.api.member

data class MemberModel(
    var key: Long,
    var name: String?,
    var id: String?,
    var pw: String?
) {
    override fun toString(): String {
        return "MemberDto(key=$key, name=$name, id=$id, pw=$pw)"
    }
}