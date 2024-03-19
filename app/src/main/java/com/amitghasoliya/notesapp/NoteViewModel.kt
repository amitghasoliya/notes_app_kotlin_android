package com.amitghasoliya.notesapp

import android.text.TextUtils
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitghasoliya.notesapp.models.NoteRequest
import com.amitghasoliya.notesapp.models.NoteResponse
import com.amitghasoliya.notesapp.repository.NoteRepository
import com.amitghasoliya.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {

    private val _notes = MutableStateFlow<List<NoteResponse>>(emptyList())
    val notes: StateFlow<List<NoteResponse>> = _notes

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    init {
        observeNotesStatus()
        observeNotesFlow()
    }

    private fun observeNotesFlow() {
        viewModelScope.launch {
            noteRepository.notesFlow.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        isLoading.value = false
                        _notes.value = it.data?.reversed()!!
                    }
                    is NetworkResult.Error -> {
                        isLoading.value = false
                        _errorMessage.value = it.message
                    }
                    is NetworkResult.Loading -> {
                        isLoading.value = true
                    }
                }
            }
        }
    }

    private fun observeNotesStatus(){
        viewModelScope.launch {
            noteRepository.statusFlow.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        isLoading.value = false
                    }
                    is NetworkResult.Error -> {
                        isLoading.value = false
                        _errorMessage.value = it.message
                    }
                    is NetworkResult.Loading -> {
                        isLoading.value = true
                    }
                }
            }
        }
    }

    fun getNotes(){
        viewModelScope.launch {
            noteRepository.getNote()
        }
    }
    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }
    fun updateNote(noteId:String, noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.updateNotes(noteId, noteRequest)
        }
    }
    fun deleteNote(noteId:String){
        viewModelScope.launch {
            noteRepository.deleteNotes(noteId)
        }
    }
    fun validateNote(title:String): Pair<Boolean, String>{
        val result = Pair(true, "")
        if (TextUtils.isEmpty(title.trim())){
            return Pair(false, "Enter correct inputs")
        }
        return result
    }
}