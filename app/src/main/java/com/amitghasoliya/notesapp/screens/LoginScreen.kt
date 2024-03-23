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
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.amitghasoliya.notesapp.AuthViewModel
import com.amitghasoliya.notesapp.R
import com.amitghasoliya.notesapp.api.ProgressBarButton
import com.amitghasoliya.notesapp.models.UserRequest
import com.amitghasoliya.notesapp.ui.theme.GreyLight
import com.amitghasoliya.notesapp.ui.theme.RedLight
import com.amitghasoliya.notesapp.utils.TokenManager

@Composable
fun LoginScreen(navController: NavController, tokenManager:TokenManager){

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val authViewModel:AuthViewModel = hiltViewModel()
    val userData by authViewModel.user.collectAsState()
    val errorMessage by authViewModel.errorMessage

    var buttonLoading by remember { mutableStateOf(false) }

    if (userData != null){
        tokenManager.saveToken(userData!!.token)
        tokenManager.saveUserId(userData!!.user._id)
        tokenManager.saveUsername(userData!!.user.username)
        tokenManager.saveEmail(userData!!.user.email)
        navController.popBackStack(0,true)
        navController.navigate("mainScreen")
    }else{
        buttonLoading = false
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            authViewModel.clearErrorMessage()
        }
    }

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(50.dp))

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

            var email by remember{ mutableStateOf("") }
            var password by remember{mutableStateOf("")}
            var showPassword by remember{mutableStateOf(false)}

            val customCursor = TextSelectionColors(handleColor = RedLight, backgroundColor = Color.Black)
            CompositionLocalProvider(LocalTextSelectionColors provides customCursor) {
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
                        .requiredWidthIn(max = 420.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))

                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(value = password,
                    onValueChange = {
                        password  = it
                    },
                    visualTransformation = if(showPassword){
                        VisualTransformation.None
                    }else{
                        PasswordVisualTransformation() },
                    trailingIcon = {if (showPassword){
                        IconButton(onClick = {showPassword=false}) {
                            Icon(painter = painterResource(id = R.drawable.visibility_off), contentDescription = "", modifier = Modifier.size(22.dp), tint = Color.Black)
                        }
                    }else{
                        IconButton(onClick = {showPassword=true}) {
                            Icon(painter = painterResource(id = R.drawable.visibility_on), contentDescription = "", modifier = Modifier.size(20.dp), tint = Color.Black)
                        }
                    }},
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
                        .requiredWidthIn(max = 420.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            FilledTonalButton(
                onClick = {
                    val userRequest = UserRequest(email, password, "")
                    val result = authViewModel.validateCredential(email, password, "", true)
                    if (result.first){
                        buttonLoading = true
                        authViewModel.loginUser(userRequest)
                    }else{
                        Toast.makeText(context, result.second, Toast.LENGTH_SHORT).show()
                    }},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredWidthIn(max = 420.dp)
                    .defaultMinSize(0.dp, 48.dp)
                ) {
                if (buttonLoading){
                    ProgressBarButton()
                }else{
                    Text(text = "Login", fontSize = 18.sp)
                }
            }
        }
    }
}
