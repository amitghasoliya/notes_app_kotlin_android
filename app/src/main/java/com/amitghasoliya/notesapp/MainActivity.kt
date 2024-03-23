package com.amitghasoliya.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amitghasoliya.notesapp.api.UserAPI
import com.amitghasoliya.notesapp.screens.AddNoteScreen
import com.amitghasoliya.notesapp.screens.LoginScreen
import com.amitghasoliya.notesapp.screens.MainScreen
import com.amitghasoliya.notesapp.screens.NoteDetails
import com.amitghasoliya.notesapp.screens.RegisterScreen
import com.amitghasoliya.notesapp.ui.theme.NotesAppTheme
import com.amitghasoliya.notesapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.amitghasoliya.notesapp.screens.UserProfile
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userAPI: UserAPI
    private val noteViewsModel by viewModels<NoteViewModel>()
    private lateinit var navController: NavController

    @Inject
    lateinit var tokenManager: TokenManager

    private var startDestination =mutableStateOf("splashScreen")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotesAppTheme{
                App()
            }
        }
    }

    @Composable
    fun SplashScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Image(modifier = Modifier
                .background(Color.Black, shape = RoundedCornerShape(32.dp))
                .size(150.dp), painter = painterResource(id = R.drawable.notes_icon), contentDescription = "")
        }
    }

    @Composable
    fun App() {
        navController = rememberNavController()
        LaunchedEffect(key1 = tokenManager.getToken()) {
            delay(500)
            startDestination.value = if (tokenManager.getToken() != null) {
                "mainScreen"
            } else {
                "loginScreen"
            }
        }

        NavHost(navController = navController as NavHostController, startDestination = startDestination.value){
            composable(route= "splashScreen"){
                SplashScreen()
            }
            composable(route= "loginScreen"){
                LoginScreen( navController, tokenManager)
            }
            composable(route= "registerScreen"){
                RegisterScreen(navController, tokenManager)
            }
            composable(route= "mainScreen"){
                MainScreen(navController){
                    navController.navigate("noteDetailScreen/${it}")
                }
            }
            composable(route= "addNoteScreen"){
                AddNoteScreen(navController,noteViewsModel){
                    navController.popBackStack()
                }
            }
            composable(route= "noteDetailScreen/{id}/{title}/{des}", arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                },
                navArgument("title"){
                    type = NavType.StringType
                },
                navArgument("des"){
                    type = NavType.StringType
                }
            )){
                NoteDetails(navController,it.arguments?.getString("id")!!,it.arguments?.getString("title")!!,it.arguments?.getString("des")!!){
                    navController.popBackStack()
                }
            }
            composable(route= "userProfile"){
                UserProfile(navController,tokenManager)
            }
        }
    }
}

