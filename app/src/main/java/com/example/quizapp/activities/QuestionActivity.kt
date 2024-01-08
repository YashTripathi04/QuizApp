package com.example.quizapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.adapters.OptionAdapter
import com.example.quizapp.databinding.ActivityQuestionBinding
import com.example.quizapp.models.Question
import com.example.quizapp.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding
    private var quizzes: MutableList<Quiz>? = null
    private var questions: MutableMap<String, Question>? = null
    private var index = 1
    private lateinit var actionBarTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpFirebase()
        setUpEventListener()
    }

    private fun setUpQuestionActivity() {
        binding.apply {
            btnPrevious.visibility = View.GONE
            btnNext.visibility = View.GONE
            btnSubmit.visibility = View.GONE

            if (index == 1 && questions!!.size != 1) { // first question
                btnNext.visibility = View.VISIBLE
            } else if (index == questions!!.size) { // last question
                if (index != 1) btnPrevious.visibility = View.VISIBLE
                btnSubmit.visibility = View.VISIBLE
            } else { // middle
                btnPrevious.visibility = View.VISIBLE
                btnNext.visibility = View.VISIBLE
            }
        }

        val question = questions!!["question$index"]

        /** let : The code inside the let expression is executed only when the property is not null.
        This is one of the use case of let */

        question?.let {
            val optionAdapter = OptionAdapter(this, it)
            binding.apply {
                description.text = it.description
                optionsListRecycleView.layoutManager = LinearLayoutManager(this@QuestionActivity)
                optionsListRecycleView.adapter = optionAdapter
                optionsListRecycleView.setHasFixedSize(true) // improves performance as we know this rv has fixed size = 4
            }
        }
        supportActionBar?.title = "Quiz: $actionBarTitle"
    }

    private fun setUpFirebase() {
        val firestore = FirebaseFirestore.getInstance()
        val date = intent.getStringExtra("DATE")
        actionBarTitle = date!!
        Log.d("YperDate", "$date")
        if (date != null) {
            firestore.collection("Quizzes")
                .whereEqualTo("title", date)
                .get()
                .addOnSuccessListener {
                    if (it != null && !it.isEmpty) {
                        quizzes = it.toObjects(Quiz::class.java)
                        Log.d("YperQuiz", it.toString())
                        /** we know for sure that quizzes wont be null, thus we proceed further using !! (non-null asserted call) */
                        questions = quizzes!![0].questions
                        val s = questions!!.size
                        Log.d("YperLength", "$s")
                        setUpQuestionActivity()
                    } else {
                        Toast.makeText(this, "No quiz available", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

        }
    }

    private fun setUpEventListener() {
        with(binding) {
            btnNext.setOnClickListener {
                index += 1
                setUpQuestionActivity()
            }
            btnPrevious.setOnClickListener {
                index -= 1
                setUpQuestionActivity()
            }
            btnSubmit.setOnClickListener {
                Log.d("YperSubmit", questions.toString())

                /** Quiz serialized to string & then sent to ResultActivity*/
                val intent = Intent(this@QuestionActivity, ResultActivity::class.java)
                val json = Gson().toJson(quizzes!![0])
                intent.putExtra("QUIZ", json)
                startActivity(intent)
                finish()
            }
        }

        /**
        Question object has all data including userAnswer & correct answer.
        We pass this Question object to the ResultActivity for generation of result.
        To pass data from one activity to another we use "putExtra" methods. But this
        can be used only for primitive datatypes.
        For objects we must make that class: 1) Parcelable or 2) Serializable

        In method 2, object is serialized to a string, sent, then deserialized on receiving.

         */
    }
}