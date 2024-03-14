package com.amitghasoliya.notesapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amitghasoliya.notesapp.api.ProgressBar
import com.amitghasoliya.notesapp.models.NoteResponse
import com.amitghasoliya.notesapp.ui.theme.GreyLight

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController,data: List<NoteResponse>,loading:Boolean, onClick: (id:String) -> Unit) {

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate("addNoteScreen") }, containerColor = Color.Black, contentColor = Color.White) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    },
        containerColor = Color.White
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 20.dp, 10.dp, 15.dp), Arrangement.Absolute.SpaceBetween) {
                Text(text = "Notes",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(0.dp,50.dp,0.dp,0.dp)
                )
                Image(imageVector = Icons.Default.AccountCircle, contentDescription = "", modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("userProfile")
                    })
            }

            if (loading){
                ProgressBar()
            }else if (data.isEmpty()){
                Text(text = "Notes not available", textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                )
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Fixed(2)
            ){
                items(data){ data ->
                    Item(noteResponse = data, onClick)
                }
            }
        }
    }
}

@Composable
fun Item(noteResponse: NoteResponse, onClick: (id:String)-> Unit) {
    Column(modifier = Modifier
        .padding(3.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(GreyLight)
        .height(180.dp)
        .clickable {
            onClick("${noteResponse._id}/${noteResponse.title}/${noteResponse.description}")
        }
    ) {
        Text(text = noteResponse.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp, 6.dp),
            maxLines = 1
        )
        Text(text = noteResponse.description,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(8.dp, 0.dp))
    }
}
