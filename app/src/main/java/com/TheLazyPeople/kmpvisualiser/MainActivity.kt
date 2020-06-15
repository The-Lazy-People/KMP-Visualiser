package com.TheLazyPeople.kmpvisualiser

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


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
            KMPSearch(targetString, sourceString)
        }
    }
    fun KMPSearch(pat: String, txt: String) {
        val str = SpannableString(sourceString)
        val M = pat.length
        val N = txt.length

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        val lps = IntArray(M)
        var j = 0 // index for pat[]

        // Preprocess the pattern (calculate lps[]
        // array)
        computeLPSArray(pat, M, lps)
        var i = 0 // index for txt[]
        while (i < N) {
            if (pat[j] == txt[i]) {
                j++
                i++
            }
            if (j == M) {
               // print("Found pattern " + "at index " + (i - j))
                Toast.makeText(this,"FOUND AT INDEX ${i-j}",Toast.LENGTH_SHORT).show()

                str.setSpan(BackgroundColorSpan(Color.YELLOW), i-j, i-j+targetString.length, 0)

                j = lps[j - 1]
            } else if (i < N && pat[j] != txt[i]) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0) j = lps[j - 1] else i = i + 1
            }
        }
        tvResultString.text=str
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