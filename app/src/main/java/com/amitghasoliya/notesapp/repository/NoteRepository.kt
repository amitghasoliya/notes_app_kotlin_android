package com.amitghasoliya.notesapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amitghasoliya.notesapp.api.NoteAPI
import com.amitghasoliya.notesapp.models.NoteRequest
import com.amitghasoliya.notesapp.models.NoteResponse
import com.amitghasoliya.notesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteAPI: NoteAPI) {

    private val _noteLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val noteLiveData : LiveData<NetworkResult<List<NoteResponse>>>
        get() = _noteLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData : LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getNote(){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteAPI.getNotes()
        Log.d("test156",response.body().toString())

        if (response.isSuccessful && response.body() != null) {
            _noteLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _noteLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            _noteLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteAPI.createNotes(noteRequest)
        handleResponse(response, "Note Created")
    }

    suspend fun deleteNotes(noteId:String){
        val response = noteAPI.deleteNote(noteId)
        handleResponse(response,"Note Deleted")
    }

    suspend fun updateNotes(noteId: String, noteRequest: NoteRequest){
        val response = noteAPI.updateNotes(noteId, noteRequest)
        handleResponse(response, "Note Updated")
    }

    private fun handleResponse(response: Response<NoteResponse>, message:String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        }else{
            _statusLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}