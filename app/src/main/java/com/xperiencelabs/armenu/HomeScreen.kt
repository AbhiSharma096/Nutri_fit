package com.xperiencelabs.armenu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xperiencelabs.armenu.ui.theme.ARMenuTheme
import com.xperiencelabs.armenu.ui.theme.Brown
import com.xperiencelabs.armenu.ui.theme.Cream


class HomeScreen : ComponentActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                  ARMenuTheme {
                        Home_Screen()
                  }
            }
      }
}



@Composable
fun Home_Screen() {
      val context= LocalContext.current
      Box(modifier = Modifier.fillMaxSize()) {
            // Load image from resources and set it as background
            Image(
                  painterResource(id = R.drawable.background),
                  contentDescription = null,
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.FillBounds
            )
                  TextWithShadow(value = " NutriFit ")
                  // Column with cards
                  Column(
                        modifier = Modifier
                              .fillMaxSize()
                              .padding(bottom = 200.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                        Card(shape = RoundedCornerShape(8.dp),
                              backgroundColor = Color.White,
                              border = BorderStroke(2.dp, Brown),
                              modifier = Modifier
                                    .padding(12.dp)
                                    .alpha(0.8f)) {
                              Column {
                                    CardOption("Visualise Menu",R.drawable.ar) {
                                          val intent = Intent(context, MainActivity::class.java)
                                          context.startActivity(intent)
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    CardOption("Food Scanner",R.drawable.chatbot) {
                                          val intent = Intent(context, MLPredictorScreen::class.java)
                                          context.startActivity(intent)
                                          // Start ML screen activity or do whatever you want
                                    }
                              }
                        }

                  }
      }
}

@Composable
fun CardOption(title: String,resource_id:Int, onClick: () -> Unit) {
      Card(
            shape = RoundedCornerShape(8.dp),
            //backgroundColor = Cream,
            border = BorderStroke(2.dp, Cream),
            modifier = Modifier
                  .fillMaxWidth()
                  .clickable(onClick = onClick)
                  .padding(16.dp)
                  .shadow(8.dp)

      ) {
            Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier=Modifier.padding(8.dp)
            ) {
                  Image(
                        painter = painterResource(id = resource_id),
                        contentDescription = "Icon",
                        modifier = Modifier.size(48.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                        text = title,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                              .padding(8.dp)
                              .weight(1f)
                              .fillMaxWidth(),
                        textAlign = TextAlign.Center
                  )
                  //arrow
                  Spacer(modifier = Modifier.width(8.dp))
                  Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription =null )
                  }
      }
}
@Composable
fun TextWithShadow(value: String) {
      val shadowOffset = Offset(x = 1f, y = 2f)
      Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
      ) {
            Spacer(modifier = Modifier.height(172.dp))
            Image(
                  painter = painterResource(id = R.drawable.nutrifit), // Replace R.drawable.logo with your image resource
                  contentDescription = null,
                  modifier = Modifier
                        .clip(CircleShape)
                        .size(200.dp)

            )
//            Text(
//                  text = value,
//                  style = TextStyle(
//                        fontSize = 46.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Brown, // Brown color
//                        textDecoration = TextDecoration.Underline,
//                        shadow = Shadow(color = Color.Gray, offset = shadowOffset, blurRadius = 2f)
//                  ),
//                  modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp),
//                  textAlign = TextAlign.Center
//            )
      }

}
