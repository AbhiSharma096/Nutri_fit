package com.xperiencelabs.armenu

import android.app.Activity
import android.os.Bundle
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xperiencelabs.armenu.ui.theme.ARMenuTheme
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.xperiencelabs.armenu.ui.theme.Brown
import com.xperiencelabs.armenu.ui.theme.Cream
import com.xperiencelabs.armenu.ui.theme.PinkBackground
import com.xperiencelabs.armenu.ui.theme.Purple
import com.xperiencelabs.armenu.ui.theme.Yellow


private const val SEND_SMS_PERMISSION_REQUEST_CODE = 1

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ARMenuTheme() {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Column(verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            OrderScreen(context = LocalContext.current)
                        }
                }
            }
            }
        }
    }

@Composable
fun OrderScreen(context: Context){
    var name by remember { mutableStateOf("") }
    var tableNumber by remember { mutableStateOf("") }
    var selectedSize by remember { mutableStateOf("Small") }
    val sizeOptions = listOf("Small", "Medium", "Large")
    var isDropdownVisible by remember { mutableStateOf(false) }



    //Intent Handle && Remove this code for preview error
    var food by remember { mutableStateOf("Food") }
    val receivedIntent = (context as Activity).intent
    val receivedItemName = receivedIntent.getStringExtra("itemName")
    if (receivedItemName != null) {
        food = receivedItemName
    }

    val context = LocalContext.current



    ///////Permission Handle
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Permission is not granted, request it
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.SEND_SMS),
            SEND_SMS_PERMISSION_REQUEST_CODE
        )
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        Image(
            painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(8.dp),
            border = BorderStroke(1.dp, Brown),
            elevation = 18.dp
        ) {
            Box(
            ) {
                Column(
                    modifier = Modifier
                        .padding(18.dp, 18.dp)
                ) {

                    Text(
                        text = "Order: "+food.substring(0, 1)
                            .uppercase() + food.substring(1) ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.body1.copy(fontSize = 40.sp),
                        fontWeight = FontWeight.Bold
                    )

                    // Name TextField
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(" Your Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    // Table Number TextField
                    OutlinedTextField(
                        value = tableNumber,
                        onValueChange = { tableNumber = it },
                        label = { Text("Table Number / Address") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    // Size Dropdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Size: $selectedSize",
                            modifier = Modifier
                                .background(Cream)
                                .padding(16.dp)
                                .clickable {
                                    isDropdownVisible = !isDropdownVisible
                                }
                        )
                        DropdownMenu(
                            expanded = isDropdownVisible,
                            onDismissRequest = { isDropdownVisible = false },
                            modifier = Modifier
                                .background(Color.White)
                        ) {
                            sizeOptions.forEach { size ->
                                DropdownMenuItem(onClick = {
                                    selectedSize = size
                                    isDropdownVisible = false
                                }) {
                                    Text(text = size)
                                }
                            }
                        }
                    }
                    // Confirm Order Button
                    Button(
                        onClick = {
                            sendOrderConfirmationSms(food, name, tableNumber, selectedSize, context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFDA7E6D))
                    ) {
                        Text(text = "Confirm Order", color = Color.White)
                    }
                }
            }
        }
    }

    }

@Preview()
@Composable
fun OrderScreenPreview(){
    OrderScreen(context = LocalContext.current)
}

@Composable
private fun GradiendBrush(
    isverticalGradient:Boolean,
    colors:List<Color>
): Brush {
    val endoffset=if (isverticalGradient){
        Offset(0f,Float.POSITIVE_INFINITY)
    }
    else{
        Offset(Float.POSITIVE_INFINITY,0f)
    }

    return Brush.linearGradient(
        colors=colors,
        start= Offset.Zero,
        end=endoffset
    )
}

private fun sendOrderConfirmationSms(food:String,name: String, tableNumber: String, size: String, context: Context) {
    try {
        val smsManager = SmsManager.getDefault()
        val phoneNumber = "9045489926"
        val message = "Order confirmation:\n" +
                "Name: $name\nItem: $food\nSize: $size\nTable No: $tableNumber"
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(context," Order Placed ",Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Log.e("SMS_ERROR", "Error sending SMS: ${e.message}")
        e.printStackTrace()
    }
}



