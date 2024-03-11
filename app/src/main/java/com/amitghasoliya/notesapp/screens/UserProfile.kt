package com.amitghasoliya.notesapp.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amitghasoliya.notesapp.AuthViewModel
import com.amitghasoliya.notesapp.MainActivity
import com.amitghasoliya.notesapp.models.UserDelete
import com.amitghasoliya.notesapp.ui.theme.GreyLight
import com.amitghasoliya.notesapp.ui.theme.RedLight
import com.amitghasoliya.notesapp.utils.TokenManager

@Composable
fun UserProfile(viewModel:AuthViewModel,tokenManager: TokenManager){

    val context = LocalContext.current
    val name= tokenManager.getUsername().toString()
    val email = tokenManager.getEmail().toString()
    val id = tokenManager.getUserId().toString()

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(40.dp))

            Text("Profile",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            TextField(value = name,
                onValueChange = {},
                placeholder = {Text(text = "Enter title")},
                colors = TextFieldDefaults.colors(focusedContainerColor = GreyLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = GreyLight
                ),
                readOnly = true,
                maxLines = 1,
                shape = RoundedCornerShape(6.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(value = email,
                onValueChange = {},
                readOnly = true,
                colors = TextFieldDefaults.colors(focusedContainerColor = GreyLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = GreyLight
                ),
                maxLines = 1,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.height(50.dp))

            FilledTonalButton(
                onClick = {
                    tokenManager.logOut()
                    context.startActivity(Intent(context, MainActivity::class.java))
//                    navController.navigate("loginScreen"){
//                        popUpTo("loginScreen"){inclusive=true}
//                        launchSingleTop = true
//                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(0.dp, 48.dp)
            ) {
                Text(text = "Log Out", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            FilledTonalButton(
                onClick = {
                    val UserId = UserDelete(id)
                    viewModel.deleteUser(UserId)
                    tokenManager.logOut()
                    context.startActivity(Intent(context, MainActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedLight,
                    contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(0.dp, 48.dp)
            ) {
                Text(text = "Delete Account", fontSize = 18.sp)
            }
        }
    }

}