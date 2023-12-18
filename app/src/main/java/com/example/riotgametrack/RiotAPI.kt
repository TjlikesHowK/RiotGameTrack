package com.example.riotgametrack

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import okhttp3.OkHttpClient
import okhttp3.Request

const val apiKey = "RGAPI-1e183a63-53e0-4302-89ab-5ec075b5ae64"

fun getUserData(nickname: String, tag: String): JsonObject {
    return runBlocking {
        return@runBlocking userData(nickname, tag)
    }
}
fun getUserStats(puuid: String): JsonObject {
    return runBlocking {
        return@runBlocking userStats(puuid)
    }
}
fun getChallengesData(puuid: String): JsonObject {
    return runBlocking {
        return@runBlocking challengesData(puuid)
    }
}
fun getChallengesNameData(id: String): JsonObject {
    return runBlocking {
        return@runBlocking challengesNameData(id)
    }
}

suspend fun userData(nickname: String, tag: String): JsonObject {
    val apiUrl = "https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/$nickname/$tag?api_key=$apiKey"

    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val response = client.newCall(request).execute()

        val data = response.body()?.string()
        val json = Json.parseToJsonElement(data.toString()).jsonObject

        return@withContext json
    }
}

suspend fun userStats(puuid: String): JsonObject {
    val apiUrl_1 = "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/$puuid/ids?start=0&count=20&api_key=$apiKey"

    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request_1 = Request.Builder()
            .url(apiUrl_1)
            .build()

        val response_1 = client.newCall(request_1).execute()
        val data_1 = response_1.body()?.string()

        val json_tmpUrl = Json.parseToJsonElement(data_1.toString()).jsonArray[0].toString().replace("\"","")

        val apiUrl_2 = "https://europe.api.riotgames.com/lol/match/v5/matches/${json_tmpUrl}?api_key=$apiKey"

        val request_2 = Request.Builder()
            .url(apiUrl_2)
            .build()

        val response_2 = client.newCall(request_2).execute()
        val data_2 = response_2.body()?.string()
        val json = Json.parseToJsonElement(data_2.toString()).jsonObject

        return@withContext json
    }
}

suspend fun challengesData(puuid: String): JsonObject {
    val apiUrl = "https://ru.api.riotgames.com/lol/challenges/v1/player-data/$puuid?api_key=$apiKey"

    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val response = client.newCall(request).execute()

        val data = response.body()?.string()
        val json = Json.parseToJsonElement(data.toString()).jsonObject

        return@withContext json
    }
}
suspend fun challengesNameData(id: String): JsonObject {
    val apiUrl = "https://ru.api.riotgames.com/lol/challenges/v1/challenges/$id/config?api_key=$apiKey"
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val response = client.newCall(request).execute()

        val data = response.body()?.string()
        val json = Json.parseToJsonElement(data.toString()).jsonObject

        return@withContext json
    }
}