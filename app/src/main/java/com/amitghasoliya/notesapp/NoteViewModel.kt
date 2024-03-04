package com.amitghasoliya.notesapp

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitghasoliya.notesapp.models.NoteRequest
import com.amitghasoliya.notesapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {

    val notesLiveData get() = noteRepository.noteLiveData
    val statusLiveData get() = noteRepository.statusLiveData

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

    fun validateCredential(title:String,description:String): Pair<Boolean, String>{
        var result = Pair(true, "")
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)){
            return Pair(false, "Please Provide Credentials")
        }
        return result
    }

    fun validateNote(title:String,description:String): Pair<Boolean, String>{
        var result = Pair(true, "")
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)){
            return Pair(false, "Please Provide Credentials")
        }
        return result
    }

}