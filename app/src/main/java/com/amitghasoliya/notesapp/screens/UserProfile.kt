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
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.amitghasoliya.notesapp.AuthViewModel
import com.amitghasoliya.notesapp.MainActivity
import com.amitghasoliya.notesapp.R
import com.amitghasoliya.notesapp.models.UserDelete
import com.amitghasoliya.notesapp.ui.theme.GreyLight
import com.amitghasoliya.notesapp.ui.theme.RedLight
import com.amitghasoliya.notesapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(navController: NavController,tokenManager: TokenManager){

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val authViewModel:AuthViewModel = hiltViewModel()
    val name= tokenManager.getUsername().toString()
    val email = tokenManager.getEmail().toString()
    val id = tokenManager.getUserId().toString()

    Scaffold(
        containerColor = Color.White,
        topBar = { TopAppBar(colors = TopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.White,
            scrolledContainerColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.Black),
            navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        modifier = Modifier.size(20.dp),
                        contentDescription = ""
                    )
                }
            }, title = { Text(text = "") })}
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(it)
            .padding(15.dp, 5.dp)
            .verticalScroll(scrollState)) {
            Spacer(modifier = Modifier.height(20.dp))

            Text("Profile",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredWidthIn(max = 420.dp)
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
                    .requiredWidthIn(max = 420.dp)
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
                    .requiredWidthIn(max = 420.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.height(50.dp))

            val logOutDialog = remember {
                mutableStateOf(false)
            }
            if (logOutDialog.value){
                AlertDialog(shape = RoundedCornerShape(18.dp), containerColor = Color.White, titleContentColor = Color.Black, title = { Text(text = "Confirm")}, text = { Text(text = "Are you sure to log out of your account ?", color = Color.Gray)},
                    onDismissRequest = {logOutDialog.value=false},
                    dismissButton = {
                        TextButton(onClick = {
                        logOutDialog.value = false
                    }) {
                        Text(text = "Cancel", color = Color.Black)
                    }
                    },
                    confirmButton = {
                    TextButton(onClick = {
                        logOutDialog.value = false
                        tokenManager.logOut()
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }) {
                        Text(text = "Log Out", color = RedLight)
                    }
                })

            }
            FilledTonalButton(
                onClick = {
                    logOutDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredWidthIn(max = 420.dp)
                    .defaultMinSize(0.dp, 48.dp)
            ) {
                Text(text = "Log Out", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            val deleteAccountDialog = remember {
                mutableStateOf(false)
            }
            if (deleteAccountDialog.value){
                AlertDialog(shape = RoundedCornerShape(18.dp),containerColor = Color.White, titleContentColor = Color.Black, title = { Text(text = "Confirm")}, text = { Text(text = "Are you sure to delete your account permanently?", color = Color.Gray)},
                    onDismissRequest = {deleteAccountDialog.value=false},
                    dismissButton = {
                        TextButton(onClick = {
                            deleteAccountDialog.value = false
                        }) {
                            Text(text = "Cancel", color = Color.Black)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            deleteAccountDialog.value = false
                            val userId = UserDelete(id)
                            authViewModel.deleteUser(userId)
                            tokenManager.logOut()
                            context.startActivity(Intent(context, MainActivity::class.java))
                        }) {
                            Text(text = "Delete Account", color = RedLight)
                        }
                    })
            }

            FilledTonalButton(
                onClick = {
                    deleteAccountDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedLight,
                    contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredWidthIn(max = 420.dp)
                    .defaultMinSize(0.dp, 48.dp)
            ) {
                Text(text = "Delete Account", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

        }
    }

}
