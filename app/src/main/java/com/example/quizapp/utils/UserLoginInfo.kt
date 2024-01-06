package com.example.quizapp.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.quizapp.activities.LoginActivity
import com.example.quizapp.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth

object UserLoginInfo {
    val auth = FirebaseAuth.getInstance()
    /**
    Firebase creates a currentUser object if user is logged in
    Thus, by checking if such a object exists or not, we can
    know if user is already logged in
     */

    fun userLoggedIn(): Boolean{
        return auth.currentUser != null
    }

}