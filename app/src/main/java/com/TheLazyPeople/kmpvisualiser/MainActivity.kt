package com.TheLazyPeople.kmpvisualiser

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.style.BackgroundColorSpan
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Integer.max


class MainActivity : AppCompatActivity() {
    var sourceString:String=""
    var targetString:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        btnSearch.setOnClickListener {
            sourceString=etSourceString.text.toString()
            targetString=etTargetString.text.toString()
            tvLPSFakeResultString.text=""
            tvLPSResultString.text=""
            tvResultString.text=""
            KMPSearch(targetString, sourceString)
        }
    }
    fun KMPSearch(pat: String, txt: String) {
        GlobalScope.launch(Dispatchers.Main){
            val str = SpannableString(sourceString)
            val strLPS=SpannableString(targetString)
            var strFakeLPS=""
            val M = pat.length
            val N = txt.length

            // create lps[] that will hold the longest
            // prefix suffix values for pattern
            val lps = IntArray(M)
            var j = 0 // index for pat[]
            var q=0
            var w=0
            // Preprocess the pattern (calculate lps[]
            // array)
            computeLPSArray(pat, M, lps)
            var i = 0 // index for txt[]
            var c=-1
            while (i < N) {
                strFakeLPS=sourceString.substring(0,i-j)
                tvLPSFakeResultString.text=strFakeLPS
                if (pat[j] == txt[i]) {
                    if(j==0){
                        if(i>0)
                            strFakeLPS=sourceString.substring(0,i)
                        else
                            strFakeLPS=sourceString.substring(0,0)
                    }
                    strFakeLPS=sourceString.substring(0,i-j)
                    str.setSpan(BackgroundColorSpan(Color.GREEN), i-j, i+1, 0)
                    strLPS.setSpan(BackgroundColorSpan(Color.GREEN),0,j+1,0)
                    tvResultString.text = str
                    tvLPSResultString.text=strLPS
                    tvLPSFakeResultString.text=strFakeLPS
                    delay(500)
                    j++
                    i++
                }
                if (j == M) {
                    // print("Found pattern " + "at index " + (i - j))
                    //Toast.makeText(this, "FOUND AT INDEX ${i - j}", Toast.LENGTH_SHORT).show()
                    if(c+1<i-j)
                    str.setSpan(BackgroundColorSpan(Color.WHITE), c+1, i-j, 0)
                    str.setSpan(BackgroundColorSpan(Color.YELLOW), i - j, i - j + targetString.length, 0)
                    q=i-j
                    w=i - j + targetString.length
                    strLPS.setSpan(BackgroundColorSpan(Color.YELLOW), 0, targetString.length, 0)
                    tvResultString.text = str
                    tvLPSResultString.text=strLPS
                    delay(500)
                    strLPS.setSpan(BackgroundColorSpan(Color.WHITE), 0, targetString.length, 0)
                    c=i-j+targetString.length-1;
                    j = lps[j - 1]


                }
                else if (i < N && pat[j] != txt[i]) {
                    strLPS.setSpan(BackgroundColorSpan(Color.WHITE), 0, targetString.length, 0)
                    // Do not match lps[0..lps[j-1]] characters,
                    // they will match anyway
                    if (j != 0) {
                        //str.setSpan(BackgroundColorSpan(Color.WHITE), i, i+1, 0)
                        //delay(500)
                        str.setSpan(BackgroundColorSpan(Color.WHITE), c+1, i, 0)
                        str.setSpan(BackgroundColorSpan(Color.RED), i, i+1, 0)
                        strLPS.setSpan(BackgroundColorSpan(Color.RED), j, j+1, 0)
                        tvResultString.text = str
                        tvLPSResultString.text=strLPS
                        delay(500)
                        strLPS.setSpan(BackgroundColorSpan(Color.WHITE), j, j+1, 0)
                        str.setSpan(BackgroundColorSpan(Color.WHITE), i, i+1, 0)
                        j = lps[j - 1]
                        strFakeLPS=sourceString.substring(0,i-1)
                        //delay(500)
                    }
                    else {
                        str.setSpan(BackgroundColorSpan(Color.RED), i, i+1, 0)
                        strLPS.setSpan(BackgroundColorSpan(Color.RED), j, j+1, 0)
                        tvResultString.text = str
                        tvLPSResultString.text=strLPS
                        delay(500)
                        strLPS.setSpan(BackgroundColorSpan(Color.WHITE), j, j+1, 0)
                        str.setSpan(BackgroundColorSpan(Color.WHITE), i, i+1, 0)
                        i = i + 1
                        strFakeLPS=sourceString.substring(0,i-1)
                    }
                }
                tvResultString.text = str
                tvLPSResultString.text=strLPS
                tvLPSFakeResultString.text=strFakeLPS

            }
            str.setSpan(BackgroundColorSpan(Color.WHITE), c+1, N, 0)
            str.setSpan(BackgroundColorSpan(Color.YELLOW), q,w, 0)
            strLPS.setSpan(BackgroundColorSpan(Color.WHITE), 0, targetString.length, 0)
            tvResultString.text=str
            tvLPSResultString.text=strLPS

        }
    }

    fun computeLPSArray(pat: String, M: Int, lps: IntArray) {
        // length of the previous longest prefix suffix
        var len = 0
        var i = 1
        lps[0] = 0 // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M) {
            if (pat[i] == pat[len]) {
                len++
                lps[i] = len
                i++
            } else  // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0) {
                    len = lps[len - 1]

                    // Also, note that we do not increment
                    // i here
                } else  // if (len == 0)
                {
                    lps[i] = len
                    i++
                }
            }
        }
    }
}