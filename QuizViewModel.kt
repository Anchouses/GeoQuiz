package com.bignerdranch.android.geoquiz

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import sun.jvm.hotspot.debugger.win32.coff.DebugVC50X86RegisterEnums.TAG

class QuizViewModel: ViewModel() {

    var currentIndex = 0
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))


    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}