package com.amitghasoliya.notesapp.repository

import android.util.Log
import com.amitghasoliya.notesapp.api.NoteAPI
import com.amitghasoliya.notesapp.models.NoteRequest
import com.amitghasoliya.notesapp.models.NoteResponse
import com.amitghasoliya.notesapp.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteAPI: NoteAPI) {

    private val _notesFlow = MutableStateFlow<NetworkResult<List<NoteResponse>>>(NetworkResult.Loading())
    val notesFlow : StateFlow<NetworkResult<List<NoteResponse>>>
        get() = _notesFlow

    private val _statusFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Loading())
    val statusFlow : StateFlow<NetworkResult<String>>
        get() = _statusFlow

    suspend fun getNote(){
        _statusFlow.emit(NetworkResult.Loading())
        val response = noteAPI.getNotes()
        Log.d("test156",response.body().toString())

        if (response.isSuccessful && response.body() != null) {
            _notesFlow.emit(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesFlow.emit(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            _notesFlow.emit(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _statusFlow.emit(NetworkResult.Loading())
        val response = noteAPI.createNotes(noteRequest)
        handleResponse(response, "Note Created")
    }

    suspend fun deleteNotes(noteId:String){
        val response = noteAPI.deleteNote(noteId)
        handleResponse(response,"Note Deleted")
    }

    suspend fun updateNotes(noteId: String, noteRequest: NoteRequest){
        _statusFlow.emit(NetworkResult.Loading())
        val response = noteAPI.updateNotes(noteId, noteRequest)
        handleResponse(response, "Note Updated")
    }

    private suspend fun handleResponse(response: Response<NoteResponse>, message:String) {
        if (response.isSuccessful && response.body() != null) {
            _statusFlow.emit(NetworkResult.Success(message))
        }else{
            _statusFlow.emit(NetworkResult.Error("Something Went Wrong"))
        }
    }
}