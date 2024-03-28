package com.amitghasoliya.notesapp.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.amitghasoliya.notesapp.NoteViewModel
import com.amitghasoliya.notesapp.api.ProgressBar
import com.amitghasoliya.notesapp.models.NoteResponse
import com.amitghasoliya.notesapp.ui.theme.GreyLight
import com.amitghasoliya.notesapp.ui.theme.RedLight
import com.amitghasoliya.notesapp.utils.KeyboardUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController,onClick: (id:String) -> Unit) {

    val keyboardUtils = KeyboardUtils()
    val notesViewModel : NoteViewModel = hiltViewModel()
    val context = LocalContext.current

    val data by notesViewModel.notes.collectAsState()
    val isLoading by notesViewModel.isLoading

    val errorMessage by notesViewModel.errorMessage
    errorMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        notesViewModel.clearErrorMessage()
    }

    // unfocused on keyboard close
    val isKeyboardOpen by keyboardUtils.keyboardAsState()
    val focusManager = LocalFocusManager.current
    if (!isKeyboardOpen) focusManager.clearFocus()

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (data.isEmpty()){
            notesViewModel.isLoading.value = true
            try {
                notesViewModel.getNotes()
            }catch (e:Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }else{
            notesViewModel.isLoading.value = false
            try {
                notesViewModel.getNotes()
            }catch (e:Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
        ExtendedFloatingActionButton(modifier = Modifier.padding(6.dp), onClick = { navController.navigate("addNoteScreen") }, containerColor = Color.Black, contentColor = Color.White) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
            Text(text = "New", modifier = Modifier.padding(6.dp,0.dp))
        }
    },
        containerColor = Color.White
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(8.dp, 0.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp, 20.dp, 10.dp, 0.dp), Arrangement.Absolute.SpaceBetween) {
                Text(text = "Notes",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(0.dp,40.dp,0.dp,0.dp)
                )
                Image(imageVector = Icons.Default.AccountCircle, contentDescription = "", modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("userProfile")
                    })
            }

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 0.dp, 4.dp, 14.dp),
                active = false,
                placeholder = {Text(text = "Search")},
                onActiveChange = {},
                query = searchText,
                colors = SearchBarDefaults.colors(containerColor = GreyLight,
                    inputFieldColors = TextFieldDefaults.colors(
                        unfocusedContainerColor = GreyLight,
                        focusedContainerColor = GreyLight,
                        focusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTrailingIconColor = RedLight,
                        unfocusedTrailingIconColor = RedLight,
                        unfocusedTextColor = Color.Black,
                        unfocusedPlaceholderColor = Color.Gray,
                        unfocusedLeadingIconColor = Color.Gray,
                        focusedLeadingIconColor = RedLight
                )),
                onQueryChange = {
                    searchText = it
                },
                onSearch = { query ->
                    searchText = query
                },
                leadingIcon = { Icon(imageVector =  Icons.Default.Search, contentDescription = "") },
                trailingIcon = { if(searchText.isNotEmpty()){
                    IconButton(onClick = { searchText =""}) {
                        Icon(imageVector =  Icons.Default.Close, contentDescription = "") }
                    } },
            ){ }

            if (isLoading){
                ProgressBar()
            }else if (data.isEmpty()){
                Text(text = "Notes not available", textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                )
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Adaptive(160.dp)
            ){
                items(data.filter {
                    it.title.contains(searchText, ignoreCase = true) ||
                            it.description.contains(searchText, ignoreCase = true)
                }) { data ->
                    Item(noteResponse = data, onClick)
                }
            }
        }
    }
}

@Composable
fun Item(noteResponse: NoteResponse, onClick: (id:String)-> Unit) {
    Column(modifier = Modifier
        .padding(3.dp, 0.dp, 3.dp, 6.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(GreyLight)
        .height(184.dp)
        .clickable {
            val description = noteResponse.description.ifEmpty { " " }
            onClick("${noteResponse._id}/${noteResponse.title}/${description}")
        }
    ) {
        Text(text = noteResponse.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp, 6.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = noteResponse.description,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(8.dp, 0.dp),
            overflow = TextOverflow.Ellipsis
        )
    }
}

