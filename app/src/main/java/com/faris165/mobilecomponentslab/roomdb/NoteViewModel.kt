package com.faris165.mobilecomponentslab.roomdb


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val repo = NoteRepository(db.noteDao())

    val notes = repo.notes.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(text: String) = viewModelScope.launch { repo.add(text) }
}