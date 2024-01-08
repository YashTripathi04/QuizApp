package com.example.quizapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }
        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            /**
            finish() will destroy current activity i.e. SignUp activity
            This way it is removed from backstack & clicking back button on
            login activity wont bring us back to it
             */
            finish()
        }
    }

    private fun signUpUser() {
        val email = binding.etSignUpEmailAddress.text.toString()
        val password = binding.etSignUpPassword.text.toString()
        val confirmPassword = binding.etSignUpConfirmPassword.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        /**
        below function call is a network call, thus it returns if call was successfully made or not
        thus we add a onCompleteListener which reads the response and based on the response
        performs the specified task
         * */
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Couldn't log in", Toast.LENGTH_SHORT).show()
                }
            }
    }
}