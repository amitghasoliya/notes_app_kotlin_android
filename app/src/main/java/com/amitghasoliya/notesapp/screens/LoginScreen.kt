package com.amitghasoliya.notesapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.amitghasoliya.notesapp.AuthViewModel
import com.amitghasoliya.notesapp.models.UserRequest
import com.amitghasoliya.notesapp.ui.theme.GreyLight
import com.amitghasoliya.notesapp.ui.theme.RedLight
import com.amitghasoliya.notesapp.utils.NetworkResult

@Composable
fun LoginScreen(navController: NavController, viewsModel: AuthViewModel){

    val context = LocalContext.current
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(30.dp))

            Text("Login",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text("Welcome back",
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text("Don't have an account? ",
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("Create new account",
                    color = RedLight,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        navController.navigate("registerScreen")
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            var email by remember{
                mutableStateOf("")
            }
            TextField(value = email,
                onValueChange = {email  = it},
                placeholder = {Text(text = "Email")},
                colors = TextFieldDefaults.colors(focusedContainerColor = GreyLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    unfocusedContainerColor = GreyLight,
                    unfocusedPlaceholderColor = Color.Gray,
                    unfocusedTextColor = Color.Black
                ),
                maxLines = 1,
                shape = RoundedCornerShape(6.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))

            )

            Spacer(modifier = Modifier.height(10.dp))

            var password by remember{
                mutableStateOf("")
            }
            TextField(value = password,
                onValueChange = {
                    password  = it
                },
                placeholder = {Text(text = "Password")},
                colors = TextFieldDefaults.colors(focusedContainerColor = GreyLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    unfocusedContainerColor = GreyLight,
                    unfocusedPlaceholderColor = Color.Gray,
                    unfocusedTextColor = Color.Black
                ),
                maxLines = 1,
                shape = RoundedCornerShape(6.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.height(30.dp))

            val owner = LocalLifecycleOwner.current

            var buttonText by remember {
                mutableStateOf("Login")
            }
            FilledTonalButton(
                onClick = {
                    val userRequest = UserRequest(email, password, "")
                    val result = viewsModel.validateCredential(email, password, "", true)
                    if (result.first){
                        buttonText = "Loading..."
                        viewsModel.loginUser(userRequest)
                        viewsModel.userResponseLiveData.observe(owner, Observer {
                            when (it) {
                                is NetworkResult.Success -> {
                                }
                                is NetworkResult.Error -> {
                                    buttonText = "Login"
                                }
                                is NetworkResult.Loading ->{
                                    buttonText = "Loading..."
                                }
                            }
                        } )
                    }else{
                        Toast.makeText(context, result.second, Toast.LENGTH_SHORT).show()
                    }},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(0.dp, 48.dp)
                ) {
                Text(text = buttonText, fontSize = 18.sp)
            }
        }
    }
}
