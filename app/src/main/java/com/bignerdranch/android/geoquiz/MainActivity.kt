package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {   //подкласс Activity
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentIndex = 0
    private var previousIndex = 0
    private var countClick = 0

    //private fun <T>List<T>.rand() = shuffled().first()
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
            countClick++
            trueButton.isEnabled = countClick < 1
            checkAnswer(true) }

        falseButton.setOnClickListener {
            countClick++
            falseButton.isEnabled = countClick < 1
            checkAnswer(false)

        }

        nextButton.setOnClickListener {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            countClick = 0
            previousIndex = currentIndex
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()

        }

        previousButton.setOnClickListener {
            currentIndex = previousIndex
            updateQuestion()
        }

        questionTextView.setOnClickListener{
            currentIndex = (currentIndex + 1) % questionBank.size
           updateQuestion()
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
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer){
            R.string.correct_toast } else { R.string.incorrect_toast }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).run {
            show() }
    }

    private fun countClicker(){}

    private fun countAnswers(){}
}