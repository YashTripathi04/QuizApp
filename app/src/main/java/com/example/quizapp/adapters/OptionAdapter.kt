package com.example.quizapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.databinding.OptionItemBinding
import com.example.quizapp.models.Question


class OptionAdapter(val context: Context, val question: Question) :
    RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    private val optionList = listOf(question.option1, question.option2, question.option3, question.option4)

    inner class OptionViewHolder(private val optionBinding: OptionItemBinding) :
        RecyclerView.ViewHolder(optionBinding.root) {

        fun bind(option: String) {
            optionBinding.apply {
                quizOption.text = option
                quizOption.setOnClickListener {
//                    Toast.makeText(context, option, Toast.LENGTH_SHORT).show()
                    question.userAnswer = option
                    notifyDataSetChanged()
                }
                if(question.userAnswer == option){
                    quizOption.setBackgroundResource(R.drawable.option_item_selected_bg)
                }else{
                    quizOption.setBackgroundResource(R.drawable.option_item_bg)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = OptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(optionList[position])
    }
}