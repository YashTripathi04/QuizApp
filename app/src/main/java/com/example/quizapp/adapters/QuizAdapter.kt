package com.example.quizapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.activities.QuestionActivity
import com.example.quizapp.databinding.QuizItemBinding
import com.example.quizapp.models.Question
import com.example.quizapp.models.Quiz
import com.example.quizapp.utils.ColorPicker
import com.example.quizapp.utils.IconPicker



class QuizAdapter(val context: Context, val quizzes: List<Quiz>) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(private val binding: QuizItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: Quiz) {
            binding.apply {
                Log.d("YperTitle", quiz.title)
                quizTitle.text = quiz.title
                quizIcon.setImageResource(IconPicker.getIcon())
                cardContainer.setCardBackgroundColor(Color.parseColor(ColorPicker.getColor()))
                root.setOnClickListener {
                    Toast.makeText(context, quizzes[adapterPosition].title, Toast.LENGTH_SHORT)
                        .show()

                    val intent = Intent(context, QuestionActivity::class.java)
                    intent.putExtra("DATE", quizzes[adapterPosition].title)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = QuizItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.bind(quiz)
    }
}