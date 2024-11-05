package com.example.bottomnavigationhw1.ui.notes

import androidx.lifecycle.ViewModel
import com.example.bottomnavigationhw1.models.Note

class NoteViewModel: ViewModel() {
    val noteList: MutableList<Note> = mutableListOf()
}