package com.example.riotgametrack

const val nicknameKey = "nickname"
const val tagKey = "tag"

sealed class screen(val route: String) {
    object Login: screen(route = "login")
    object Riot: screen(route = "riot/{$nicknameKey}/{$tagKey}"){
        fun passArgs(nickname: String, tag: String): String{
            return "riot/$nickname/$tag"
        }
    }
}