package com.example.quizapp.models

/** name document(in firestore) fields exact same as data class fields/properties */
data class Quiz(
    var id: String = "",
    var title: String = "",
    var questions: MutableMap<String, Question> = mutableMapOf()
)