package com.amitghasoliya.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amitghasoliya.notesapp.api.UserAPI
import com.amitghasoliya.notesapp.models.NoteResponse
import com.amitghasoliya.notesapp.screens.AddNoteScreen
import com.amitghasoliya.notesapp.screens.LoginScreen
import com.amitghasoliya.notesapp.screens.MainScreen
import com.amitghasoliya.notesapp.screens.NoteDetails
import com.amitghasoliya.notesapp.screens.RegisterScreen
import com.amitghasoliya.notesapp.ui.theme.NotesAppTheme
import com.amitghasoliya.notesapp.utils.NetworkResult
import com.amitghasoliya.notesapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.amitghasoliya.notesapp.screens.UserProfile

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userAPI: UserAPI
    private val viewsModel by viewModels<AuthViewModel>()
    private val noteViewsModel by viewModels<NoteViewModel>()
    lateinit var navController: NavController
    var loading: Boolean = true
    var startDestination: String = "loginScreen"
    var items : List<NoteResponse> = listOf()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindObservers()
        NoteObservers()

        startDestination = if(tokenManager.getToken() != null){
            noteViewsModel.getNotes()
            "mainScreen"
        }else{
            "loginScreen"
        }
        setContent {
            NotesAppTheme{
                App()
            }
        }
    }

    @Composable
    fun App() {
        navController = rememberNavController()
        NavHost(navController = navController as NavHostController, startDestination = startDestination){
            composable(route= "loginScreen"){
                LoginScreen( navController, viewsModel)
            }
            composable(route= "registerScreen"){
                RegisterScreen(navController, viewsModel)
            }
            composable(route= "mainScreen"){
                MainScreen(navController,items, loading){
                    navController.navigate("noteDetailScreen/${it}")
                }
            }
            composable(route= "addNoteScreen"){
                AddNoteScreen(noteViewsModel){
                    navController.popBackStack()
                    NoteCreationObservers()
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
                NoteDetails(it.arguments?.getString("id")!!,it.arguments?.getString("title")!!,it.arguments?.getString("des")!!){
                    noteViewsModel.getNotes()
                    NoteCreationObservers()
                    navController.popBackStack()
                }
            }

            composable(route= "userProfile"){
                UserProfile(viewsModel,navController, tokenManager)
            }
        }
    }

    private fun bindObservers() {
        viewsModel.userResponseLiveData.observe(this, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    loading=false
                    tokenManager.saveToken(it.data!!.token)
                    tokenManager.saveUserId(it.data.user._id)
                    tokenManager.saveUsername(it.data.user.username)
                    tokenManager.saveEmail(it.data.user.email)
                    navController.popBackStack(0,true)
                    navController.navigate("mainScreen")
                    noteViewsModel.getNotes()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    loading=true
                }
            }
        } )
    }

    private fun NoteObservers() {
        noteViewsModel.notesLiveData.observe(this, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    loading=false
                    items= it.data!!.reversed()
                    navController.navigate("mainScreen")
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    loading=true
                }
            }
        } )
    }

    private fun NoteCreationObservers() {
        noteViewsModel.statusLiveData.observe(this, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    noteViewsModel.getNotes()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    loading=true
                }
            }
        } )
    }

}

