package com.xperiencelabs.armenu


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.xperiencelabs.armenu.ui.theme.Brown
import com.xperiencelabs.armenu.ui.theme.DarkBrown
import com.xperiencelabs.armenu.ui.theme.LightBrown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient


class MLPredictorScreen() : ComponentActivity() {



      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val public = null
            val final = null
//            public static final MediaType JSON = MediaType.get("application/json");
//
//            OkHttpClient client = new OkHttpClient();
//
//            String post(String url, String json) throws IOException {
//                  RequestBody body = RequestBody.create(json, JSON);
//                  val url = "https://inf-4bba18a4-67ac-4269-8c23-ea2132e2b690-no4xvrhsfq-uc.a.run.app/detect"
//                  Request request = new Request.Builder()
//                        .url(url)
//                        .post(body)
//                        .build();
//                  try (Response response = client.newCall(request).execute()) {
//                        return response.body().string();
//                  }
//                  }
            setContent {
                  UploadImageScreen()
            }
      }
}

@Composable
fun UploadImageScreen() {
      var context = LocalContext.current
      var scroll = rememberScrollState(0)
      var name by remember { mutableStateOf("") }

      var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
      var outputText by remember { mutableStateOf("") }

      Box(modifier = Modifier.fillMaxSize()) {
            Image(
                  painterResource(id = R.drawable.background),
                  contentDescription = null,
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.FillBounds
            )


            Column(
                  modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                  verticalArrangement = Arrangement.Center,
                  horizontalAlignment = Alignment.CenterHorizontally
            ) {
                  if (selectedImageBitmap != null) {
                        Spacer(modifier=Modifier.height(25.dp) )
                        Image(
                              //bitmap = selectedImageBitmap!!.asImageBitmap(),
                              bitmap = selectedImageBitmap!!.asImageBitmap(),
                              contentDescription = "Image box",
                              modifier = Modifier
                                    .size(350.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp,Color.White,),
                              contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                              value = name,
                              onValueChange = { name = it },
                              label = { Text("Your Health issue") },
                              modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                              colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = DarkBrown,
                                    unfocusedBorderColor = Brown,
                                    placeholderColor = Brown
                              )
                        )
                        UploadButton(selectedImageBitmap!!, onUploadComplete = { output ->
                              outputText = output
                        },name)

                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                              modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(scroll)
                                    .padding(16.dp),
                              elevation = 4.dp,
                              shape = RoundedCornerShape(8.dp),
                              backgroundColor = Color.White
                        ) {
                              Text(
                                    text = outputText,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp) // Adjust padding inside the card
                              )
                        }

                  } else {
                        Image(
                              //bitmap = selectedImageBitmap!!.asImageBitmap(),
                              painter = painterResource(id = R.drawable.how_to_use_), // Replace "your_gif" with the name of your GIF file

                              contentDescription = "Image box",
                              modifier = Modifier
                                    .size(350.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                              contentScale = ContentScale.Crop
                        )

                        PickImageButton(onImagePicked = { bitmap ->
                              selectedImageBitmap = bitmap
                        })
                  }
            }
      }
}

@Composable
fun PickImageButton(onImagePicked: (Bitmap) -> Unit) {
      val context = LocalContext.current
      val getContent = rememberLauncherForActivityResult(contract = GetContent()) { uri ->
            uri?.let {
                  val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                  onImagePicked(bitmap)
            }
      }

      Button(
            onClick = {
                  getContent.launch("image/*")


            },
            modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            border = BorderStroke(1.dp, color = LightBrown)

      ) {
            Text("Select Image")
      }
}

@Composable
fun UploadButton(bitmap: Bitmap, onUploadComplete: (String) -> Unit,name:String) {
      var isLoading by remember {
            mutableStateOf(false)
      }
      Button(
            onClick = {
                  isLoading = true
                  // Launch a coroutine to perform the upload operation
                  GlobalScope.launch(Dispatchers.IO) {
                        val output = uploadImage(bitmap,name)
                        withContext(Dispatchers.Main) {
                              isLoading =
                                    false // Set loading state to false when upload is complete
                              onUploadComplete(output) // Call the lambda when upload is complete
                        }
                  }
            },
            modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            border = BorderStroke(1.dp, color = LightBrown)

      ) {
            Text("Upload Image")
      }
      if (isLoading) {
            LinearProgressIndicator(
                  modifier = Modifier.fillMaxWidth(), // Take up full width
                  color = Color.Blue // Customize color if needed
            )
      }
}


private suspend fun uploadImage(bitmap: Bitmap,name: String): String {
      println("Uploading image...")
      val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = "AIzaSyA4UYVM5b5L4QG0TIxJZd93CjOdK8Ki-uo"
      )

      val image1: Bitmap = bitmap

      val inputContent = content {
            image(image1)
            text("")

            text(
                 "You are an expert in nutritionist where you need to see the food items from the image,first provide the name of the food in given format\n" +
                     "           \"The food in the image is (Food-Name)\"\n" +
                     "\n" +
                     "and then provide very very short and crisp respone, don't tell how it is made and any other details of the food, tell the details of the\n" +
                     "      allergy causing ingredients, mention at most 3 main ingredients if present, in the format given below under the heading Allergy information:\n" +
                     "               \n" +
                     "               1. allergy associated with (Ingredient-1) is (one word description)\n" +
                     "               2. allergy associated with (Ingredient-2) is (one word description)\n" +
                     "               3. allergy associated with (Ingredient-3) is (one word description)\n" +
                     "                \n" +
                     "               and  display the minimum and maximum number of Nutritional content of the detected food and Display it under the heading Nutritional information:\n" +
                     "               \n" +
                     "               display like this contents like calories, fat content, protien content, carbohydrates content, vitamin content and sugar content in bullets points."
                  +"Also suggest the precaution for a person with this health issue = ${name}"
            )
      }

      val response = generativeModel.generateContent(inputContent)
      val output = response.text
      println("Image uploaded successfully!")
      return output.toString() // Return the output string
}
