package com.xperiencelabs.armenu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xperiencelabs.armenu.ui.theme.ARMenuTheme
import com.xperiencelabs.armenu.ui.theme.bg
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARMenuTheme {
                SplashScreen()
            }
        }
    }

    @Preview
    @Composable
    private fun SplashScreen(){
        var alpha= remember {
            Animatable(0f)
        }
        LaunchedEffect(key1 = true) {
            alpha.animateTo(1f,
                animationSpec = tween(1300))
            delay(1500)
            startActivity(Intent(this@SplashActivity, HomeScreen::class.java))
            finish()
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                bg
            ),
            contentAlignment = Alignment.Center ){
            Image(modifier=Modifier.alpha(alpha.value)
                .clip(CircleShape)
                .size(300.dp),
                painter = painterResource(id = R.drawable.nutrifit), contentDescription =null )
//            Spacer(modifier = Modifier.size(12.dp))
//            Text(
//                text = "NutriFit",
//                fontWeight = FontWeight.Bold
//            )
        }
    }
}