package com.example.quizapp.models

/** name of document(in firestore) fields must be exactly same as data class fields/properties */
data class Quiz(
    var id: String = "",
    var title: String = "",
    var questions: MutableMap<String, Question> = mutableMapOf()
)