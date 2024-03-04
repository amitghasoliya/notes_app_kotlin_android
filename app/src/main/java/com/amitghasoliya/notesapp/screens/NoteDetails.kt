package com.amitghasoliya.notesapp.screens

import android.widget.Toast
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amitghasoliya.notesapp.NoteViewModel
import com.amitghasoliya.notesapp.models.NoteRequest
import com.amitghasoliya.notesapp.ui.theme.GreyLight
import com.amitghasoliya.notesapp.ui.theme.RedLight

@Composable
fun NoteDetails(
    id: String,
    title: String,
    des: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val noteViewsModel : NoteViewModel = hiltViewModel()

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(30.dp))

            Text("Edit Note",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            var title by remember{
                mutableStateOf(title)
            }
            OutlinedTextField(value = title,
                onValueChange = {title  = it},
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
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))

            var description by remember{
                mutableStateOf(des)
            }
            OutlinedTextField(value = description,
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
                    .fillMaxWidth()
                    .defaultMinSize(0.dp,200.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.height(30.dp))

            FilledTonalButton(
                onClick = {
                    val noteRequest = NoteRequest(title, description)
                    val result = noteViewsModel.validateNote(title,description)
                    if (result.first){
                        noteViewsModel.updateNote(id,noteRequest)
                        onClick()
                    }else{
                        Toast.makeText(context, result.second, Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(0.dp, 48.dp)
            ) {
                Text(text = "Update Note", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            FilledTonalButton(
                onClick = {
                    noteViewsModel.deleteNote(id)
                    onClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedLight,
                    contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(0.dp, 48.dp)
            ) {
                Text(text = "Delete Note", fontSize = 18.sp)
            }
        }
    }

}