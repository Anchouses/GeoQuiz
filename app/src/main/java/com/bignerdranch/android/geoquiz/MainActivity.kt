package com.bignerdranch.android.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
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
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {   //подкласс Activity
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button
    private lateinit var cheatMessage: TextView

    private var countCorrectAns = 0

    private val quizViewModel: QuizViewModel by lazy {
      ViewModelProvider(this)[QuizViewModel::class.java]  //
    }

    @SuppressLint("RestrictedAPI", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) { //метод жизненного цикла, принимает сохраненное состояние активити (в этом случае сохраняет интекс текущего вопроса)
        super.onCreate(savedInstanceState)  //
        Log.d(TAG, "onCreate(Bundle?) called")  // выводим в логи, что  onCreate вызвана
        setContentView(R.layout.activity_main)  //заполнение виджетов и их вывод на экран

        val currentIndex = savedInstanceState?.getInt(KAY_INDEX, 0) ?: 0 // индекс вопроса берем из сохраненного состояния, либо присваиваем ноль

        val what = quizViewModel.setCurrentIndex(currentIndex)
        quizViewModel.currentIndex = what // номеру индекса из класса quizViewModel присваиваем индекс сохраненный

        trueButton = findViewById(R.id.true_button)  //
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)
        cheatMessage= findViewById(R.id.count_cheats)

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

        cheatButton.setOnClickListener {view ->
        // начало CheatActivity
            cheatsCount()
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val options = ActivityOptions
                    .makeClipRevealAnimation(view, 0, 0, view.width, view.height)

                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }
        updateQuestion()
        cheatMessage.text = "You can use cheats:" + quizViewModel.countCheats
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

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).run {
            show() }
        quizViewModel.isCheater = false

        if (userAnswer == correctAnswer) countCorrectAns++
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK){
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?: false
        }
    }
    fun cheatsCount(){
        quizViewModel.countCheats --
        if (quizViewModel.countCheats == 0) cheatButton.isEnabled = true
        cheatMessage.text = "You can use cheats:" + quizViewModel.countCheats
    }

}