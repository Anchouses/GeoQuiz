package com.bignerdranch.android.geoquiz

import android.annotation.SuppressLint
import android.content.Intent
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
private const val KAY_INDEX = "index"

class MainActivity : AppCompatActivity() {   //подкласс Activity
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private var countCorrectAns = 0
    private val quizViewModel: QuizViewModel by lazy {
      ViewModelProvider(this)[QuizViewModel::class.java]  //
    }

        //@SuppressLint("MissingInflatedId")
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) { //метод жизненного цикла, принимает сохраненное состояние активити (в этом случае сохраняет интекс текущего вопроса)
        super.onCreate(savedInstanceState)  //
        Log.d(TAG, "onCreate(Bundle?) called")  // выводим в логи, что  onCreate вызвана
        setContentView(R.layout.activity_main)  //заполнение виджетов и их вывод на экран

        val currentIndex = savedInstanceState?.getInt(KAY_INDEX, 0) ?: 0 // индекс вопроса берем из сохраненного состояния, либо присваиваем ноль
        quizViewModel.currentIndex = currentIndex // номеру индекса из класса quizViewModel присваиваем индекс сохраненный

        trueButton = findViewById(R.id.true_button)  //
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)

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

        cheatButton.setOnClickListener {
        // начало CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivity(intent) //
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

    override fun onSaveInstanceState(savedInstanceState: Bundle){
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KAY_INDEX, quizViewModel.currentIndex)
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
        //Log.d(TAG, "Updating question text", Exception())
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