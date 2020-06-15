package com.TheLazyPeople.kmpvisualiser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var sourceString:String=""
    var targetString:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSearch.setOnClickListener {
            sourceString=etSourceString.text.toString()
            targetString=etTargetString.text.toString()
        }
    }
}