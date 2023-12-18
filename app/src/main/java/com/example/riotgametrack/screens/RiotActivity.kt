package com.example.riotgametrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riotgametrack.PieChart
import com.example.riotgametrack.getChallengesData
import com.example.riotgametrack.getChallengesNameData
import com.example.riotgametrack.getUserData
import com.example.riotgametrack.getUserStats
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

@Composable
fun RiotScreen(nickname: String, tag: String) {
    var old = true
    var matchState = "WIN"
    val raw_puuid = getUserData(nickname, tag).getValue("puuid")
    val puuid = raw_puuid.toString().replace("\"","")
    val meta_usersStats = getUserStats(puuid)
    val matchStats = meta_usersStats.getValue("metadata").jsonObject.getValue("participants").jsonArray
    val index = matchStats.indexOf(raw_puuid)
    val userMatchStats = meta_usersStats.getValue("info").jsonObject.getValue("participants").jsonArray[index]

    if (userMatchStats.jsonObject.getValue("win").toString() == "false"){
        matchState = "LOSE"
    }

    val challenges = getChallengesData(puuid)
    val totalChallenger = challenges.getValue("totalPoints").jsonObject

    var challenge_1: JsonObject? = null
    var challenge_2: JsonObject? = null

    if (totalChallenger.getValue("level").toString().replace("\"","") != "NONE"){
        old = false
        challenge_1 = challenges.getValue("challenges").jsonArray[0].jsonObject
        challenge_2 = challenges.getValue("challenges").jsonArray[1].jsonObject
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp - 50.dp)
                .height(LocalConfiguration.current.screenHeightDp.dp - 25.dp)
                .padding(top = 25.dp)
                .background(Color(0xFFFF8383))
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 5.dp, start = 10.dp),
                text = "puuid: $puuid",
                lineHeight = 18.sp,
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier
                    .padding(top = 65.dp, start = 10.dp),
                text = "nickname: $nickname",
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier
                    .padding(top = 95.dp, start = 10.dp),
                text = "tag: $tag",
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier
                    .padding(top = 125.dp, start = 10.dp),
                text = "last match stats:",
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier
                    .padding(top = 155.dp, start = 10.dp),
                text = "Game result: $matchState",
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier
                    .padding(top = 185.dp, start = 10.dp),
                text = "Total gold spent: ${userMatchStats.jsonObject.getValue("goldSpent")}",
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier
                    .padding(top = 215.dp, start = 10.dp),
                text = "Total damage dealt: ${userMatchStats.jsonObject.getValue("totalDamageDealt")}",
                fontSize = 10.sp
            )
            Box (
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp - 65.dp)
                    .padding(top = 245.dp, start = 10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFFFA083))
                    .height(300.dp)
            ){
                Text(
                    modifier = Modifier
                        .padding(top = 25.dp, start = 10.dp),
                    text = "Total kills: ${userMatchStats.jsonObject.getValue("kills")}",
                    fontSize = 10.sp,
                    color = Color(0xFF425FFF)
                )
                Text(
                    modifier = Modifier
                        .padding(top = 55.dp, start = 10.dp),
                    text = "Total deaths: ${userMatchStats.jsonObject.getValue("deaths")}",
                    fontSize = 10.sp,
                    color = Color(0xFFFF4249)
                )
                PieChart(
                    data = mapOf(
                        Pair(
                            "kills",
                            userMatchStats.jsonObject.getValue("kills").toString().toInt()
                        ),
                        Pair(
                            "deaths",
                            userMatchStats.jsonObject.getValue("deaths").toString().toInt()
                        )
                    ),
                    paddingTop = 95
                )
            }

            Box (
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp - 65.dp)
                    .padding(top = 575.dp, start = 10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFFFA083))
                    .height(100.dp)
            ){
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 10.dp),
                    text = if (old) "Challenger level: " else "Challenger level: ${totalChallenger.getValue("level")}",
                    fontSize = 10.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 40.dp, start = 10.dp),
                    text = if (old) "Challenger points: " else "Challenger points: ${totalChallenger.getValue("current")}",
                    fontSize = 10.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 70.dp, start = 10.dp),
                    text = if (old) "Top of challengers: " else "Top of challengers: ${totalChallenger.getValue("percentile").toString().toFloat()*100f}%",
                    fontSize = 10.sp
                )
            }

            Box (
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp - 65.dp)
                    .padding(top = 705.dp, start = 10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFFFA083))
                    .height(100.dp)
            ){
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 10.dp),
                    text = if (old) "Challenge name: " else "Challenge name: ${getChallengesNameData(challenge_1!!.getValue("challengeId").toString().replace("\"","")).getValue("localizedNames").jsonObject.getValue("ru_RU").jsonObject.getValue("name")}",
                    fontSize = 10.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 40.dp, start = 10.dp),
                    text = if (old) "Challenge level: " else "Challenge level: ${challenge_1!!.getValue("level")}",
                    fontSize = 10.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 70.dp, start = 10.dp),
                    text = if (old) "Challenge points: " else "Challenge points: ${challenge_1!!.getValue("value")}",
                    fontSize = 10.sp
                )
            }

            Box (
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp - 65.dp)
                    .padding(top = 835.dp, start = 10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFFFA083))
                    .height(100.dp)
            ){
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 10.dp),
                    text = if (old) "Challenge name: " else "Challenge name: ${getChallengesNameData(challenge_2!!.getValue("challengeId").toString().replace("\"","")).getValue("localizedNames").jsonObject.getValue("ru_RU").jsonObject.getValue("name")}",
                    fontSize = 10.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 40.dp, start = 10.dp),
                    text = if (old) "Challenge level: " else "Challenge level: ${challenge_2!!.getValue("level")}",
                    fontSize = 10.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 70.dp, start = 10.dp),
                    text = if (old) "Challenge points: " else "Challenge points: ${challenge_2!!.getValue("value")}",
                    fontSize = 10.sp
                )
            }
        }
    }
}


@Composable
@Preview
fun RiotScreenPreview(){
    RiotScreen("TjlikesHowK", "5411")
}