package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {   //подкласс Activity
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView

    private var countCorrectAns = 0
    private val quizViewModel: QuizViewModel by lazy {
      ViewModelProvider(this)[QuizViewModel::class.java]
    }

        //@SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) { //метод жизненного цикла
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)  //заполнение виджетов и их вывод на экран

        trueButton = findViewById(R.id.true_button)  //
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
       previousButton = findViewById(R.id.previous_button)


        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {

            falseButton.isEnabled = false
            trueButton.isEnabled = false
            checkAnswer(true) }

        falseButton.setOnClickListener {

            falseButton.isEnabled = false
            trueButton.isEnabled = false
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            if (quizViewModel.currentIndex == quizViewModel.questionBank.lastIndex) {
                nextButton.isEnabled = false
                previousButton.isEnabled = true
                val finalMassage = getString(R.string.final_toast, countCorrectAns, quizViewModel.questionBank.size)
                Toast.makeText(this, finalMassage, Toast.LENGTH_LONG).run {
                    show() }
            } else {
                quizViewModel.moveToNext()
                updateQuestion()
            }
        }

        previousButton.setOnClickListener {
            if (quizViewModel.currentIndex == 0) {
                previousButton.isEnabled = false
            } else {
                quizViewModel.currentIndex -= 1
                updateQuestion()
            }
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        trueButton.isEnabled = true
        falseButton.isEnabled = true
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId: Int
        if (userAnswer == correctAnswer){
            messageResId = R.string.correct_toast
            countCorrectAns++
            } else { messageResId = R.string.incorrect_toast }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).run {
            show() }
    }

}