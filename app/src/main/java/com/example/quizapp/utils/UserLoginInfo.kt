package com.example.quizapp.utils

import com.google.firebase.auth.FirebaseAuth

object UserLoginInfo {
    val auth = FirebaseAuth.getInstance()

    /**
    Firebase creates a currentUser object if user is logged in
    Thus, by checking if such a object exists or not, we can
    know if user is already logged in
     */

    fun userLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}