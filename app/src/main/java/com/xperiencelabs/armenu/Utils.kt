package com.xperiencelabs.armenu

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object Utils {

    fun generateRandomColor(): Color {
        return Color(
            red = Random.nextFloat(),
            green= Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
        )

    }
}

//style = TextStyle(
//shadow = Shadow(Utils.generateRandomColor(),shadowoffset,2f)
//)
//
//Image(painter = painterResource(id = resource_id), contentDescription = null ,
//modifier = Modifier
//.fillMaxSize()
//.padding(4.dp))