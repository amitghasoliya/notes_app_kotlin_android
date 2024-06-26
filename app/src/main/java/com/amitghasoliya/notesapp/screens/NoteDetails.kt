package com.amitghasoliya.notesapp.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.amitghasoliya.notesapp.NoteViewModel
import com.amitghasoliya.notesapp.R
import com.amitghasoliya.notesapp.models.NoteRequest
import com.amitghasoliya.notesapp.ui.theme.GreyLight
import com.amitghasoliya.notesapp.ui.theme.RedLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetails(
    navController:NavController,
    id: String,
    title: String,
    des: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val noteViewsModel : NoteViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    var description by remember{ mutableStateOf(des.trim()) }
    var newTitle by remember{ mutableStateOf(title) }
    val maxWidth = remember { mutableStateOf(440.dp) }

    BackHandler {
        if (title != newTitle || des != description){
            val noteRequest = NoteRequest(newTitle, description)
            val result = noteViewsModel.validateNote(title)
            if (result.first){
                noteViewsModel.updateNote(id,noteRequest)
                noteViewsModel.isLoading.value = true
                onClick()
            }else{
                Toast.makeText(context, result.second, Toast.LENGTH_SHORT).show()
            }
        }else{
            navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { TopAppBar(colors = TopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.White,
            scrolledContainerColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.Black),
            navigationIcon = {
                IconButton(onClick = {
                    if (title != newTitle || des != description){
                        val noteRequest = NoteRequest(newTitle, description)
                        val result = noteViewsModel.validateNote(title)
                        if (result.first){
                            noteViewsModel.updateNote(id,noteRequest)
                            noteViewsModel.isLoading.value = true
                            onClick()
                        }else{
                            Toast.makeText(context, result.second, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        modifier = Modifier.size(20.dp),
                        contentDescription = ""
                    )
                }
            }, title = { Text(text = "") })
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(it)
            .padding(15.dp, 0.dp)
            .verticalScroll(scrollState)) {
            Spacer(modifier = Modifier.height(25.dp))

            Text("Your Note",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredWidthIn(max = 420.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            val customCursor = TextSelectionColors(handleColor = RedLight, backgroundColor = Color.Black)
            CompositionLocalProvider(LocalTextSelectionColors provides customCursor) {
                TextField(value = newTitle,
                    onValueChange = {newTitle  = it},
                    placeholder = {Text(text = "Enter title")},
                    colors = TextFieldDefaults.colors(focusedContainerColor = GreyLight,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        unfocusedContainerColor = GreyLight,
                        unfocusedPlaceholderColor = Color.Gray,
                        unfocusedTextColor = Color.Black
                    ),
                    maxLines = 2,
                    shape = RoundedCornerShape(6.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .width(maxWidth.value)
                        .align(Alignment.CenterHorizontally)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(value = description,
                    onValueChange = {
                        description  = it
                    },
                    placeholder = {Text(text = "Enter Description")},
                    colors = TextFieldDefaults.colors(focusedContainerColor = GreyLight,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        unfocusedContainerColor = GreyLight,
                        unfocusedPlaceholderColor = Color.Gray,
                        unfocusedTextColor = Color.Black
                    ),
                    maxLines = 12,
                    shape = RoundedCornerShape(6.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .width(maxWidth.value)
                        .align(Alignment.CenterHorizontally)
                        .defaultMinSize(0.dp, 260.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            FilledTonalButton(
                onClick = {
                    noteViewsModel.deleteNote(id)
                    onClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedLight,
                    contentColor = Color.Black),
                modifier = Modifier
                    .width(maxWidth.value)
                    .align(Alignment.CenterHorizontally)
                    .defaultMinSize(0.dp, 48.dp)
            ) {
                Text(text = "Delete Note", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}