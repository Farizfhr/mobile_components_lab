package com.faris165.mobilecomponentslab.roomdb


class NoteRepository(private val dao: NoteDao) {
    val notes = dao.getAll()
    suspend fun add(text: String) = dao.insert(Note(text = text))
}