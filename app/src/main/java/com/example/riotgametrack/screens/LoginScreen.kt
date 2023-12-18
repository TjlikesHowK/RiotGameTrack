package com.example.riotgametrack.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.riotgametrack.getUserData
import com.example.riotgametrack.nicknameKey
import com.example.riotgametrack.screen
import com.example.riotgametrack.tagKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var nickname by rememberSaveable { mutableStateOf("") }
    var tag by rememberSaveable { mutableStateOf("") }
    val btnLabel = remember{mutableStateOf("Login")}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(280.dp)
                .height(380.dp)
                .clip(RoundedCornerShape(30.dp))
                .shadow(1.dp, RoundedCornerShape(30.dp))
                .background(Color(0xFF656565)),
            contentAlignment = Alignment.TopCenter
        ) {
            TextField(
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 30.dp)
                    .clip(RoundedCornerShape(18.dp)),
                value = nickname,
                onValueChange = {
                    nickname = it
                },
                singleLine = true,
                placeholder = { Text(text = "Riot nickname") },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFF63D0FF))
            )
            TextField(
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 90.dp)
                    .clip(RoundedCornerShape(18.dp)),
                value = tag,
                onValueChange = {
                    tag = it
                },
                singleLine = true,
                placeholder = { Text(text = "Riot tag") },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFF63D0FF))
            )
            Button(
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 300.dp)
                    .clip(RoundedCornerShape(18.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF63D0FF)),
                onClick = {login(navController, nickname, tag)}
            ) {
                Text(btnLabel.value, fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}

fun login(navController: NavHostController, nickname: String, tag: String){
    val user = getUserData(nickname, tag)

    try {
        user.getValue("puuid")
        navController.navigate(route = screen.Riot.passArgs(nickname = nickname, tag = tag))
    }catch (_: Exception){

    }
}

@Composable
fun StartNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = screen.Login.route)
    {
        composable(
            route = screen.Login.route
        ){
            LoginScreen(navController)
        }

        composable(
            route = screen.Riot.route,
            arguments = listOf(
                navArgument(nicknameKey){
                    type = NavType.StringType
                },
                navArgument(tagKey){
                    type = NavType.StringType
                })
        ){
            RiotScreen(nickname = it.arguments?.getString(nicknameKey).toString(), tag = it.arguments?.getString(tagKey).toString())
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview(){
    StartNavigation()
}