package com.example.mobile_development_project_group_1

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NoteViewModel: ViewModel() {
    var notes = mutableStateOf(listOf<Note>())



    fun addNote(note: Note) {
        var newNotes = notes.value.toMutableList()
        newNotes.add(note)
        notes.value = newNotes
    }

    fun getFromFirestore() {
        Firebase.firestore
            .collection("")
        //.addSnapshotListener()
    }

}