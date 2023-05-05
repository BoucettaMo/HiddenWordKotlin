package com.example.hiddenwordkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Objects
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var submit: Button
    private lateinit var choose: Button
    private lateinit var addWord: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var editText: EditText
    private lateinit var timer: TextView
    private lateinit var dbManager: DbManager
    private var hiddenWord = ""
    private var remainingSeconds = 120


    private val countDownTimer = object : CountDownTimer(120000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            remainingSeconds--
            progressBar.progress = 120 - remainingSeconds
            timer.text = "$remainingSeconds sc"
        }

        override fun onFinish() {
            val alertDialog = AlertDialog.Builder(this@MainActivity)
            with(alertDialog) {
                setTitle("Game is over")
                setMessage("Sorry the time is over: the hidden word was ${hiddenWord.uppercase()} , Choose a new hidden word")
                setPositiveButton("Yes") { _, _ ->
                    adapter.list.clear()
                    adapter.notifyDataSetChanged()

                }
                show()

            }
            remainingSeconds = 120
            progressBar.progress = 0


        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Hidden Word Game"

        submit = findViewById(R.id.submit)
        addWord = findViewById(R.id.addWord)
        choose = findViewById(R.id.choose)
        progressBar = findViewById(R.id.progressBar)
        editText = findViewById(R.id.editText)
        timer = findViewById(R.id.timer)

        dbManager = DbManager(this)
        timer.text = "0 sc"
        recyclerView = findViewById(R.id.recyclerview)

        choose.setOnClickListener {
             if (timer.text!="0 sc") {return@setOnClickListener}

            countDownTimer.start()
            hiddenWord = dbManager.readdb().random()
            val list = MutableList(hiddenWord.length) { "" }
            adapter = MyAdapter(this, list)
            recyclerView.layoutManager = GridLayoutManager(this, 5)
            recyclerView.adapter = adapter
            timer.text = hiddenWord
        }

        submit.setOnClickListener {

            for (i in hiddenWord.indices) {
                if (hiddenWord[i].toString().uppercase() == editText.text.toString().uppercase()) {
                    adapter.list[i] = hiddenWord[i].toString().uppercase()
                    adapter.notifyDataSetChanged()
                }
            }
            editText.setText("")
            if (hiddenWord.uppercase() == jointToString(adapter.list)) {
                val builder = AlertDialog.Builder(this)

                with(builder) {
                    setTitle("Congratulation")
                    setMessage("You guessed the hidden word, try another one")
                    setPositiveButton("Ok") { _, _ ->
                        adapter.list.clear()
                        adapter.notifyDataSetChanged()
                        countDownTimer.cancel()
                        remainingSeconds=120
                        progressBar.progress = 0
                        timer.text="0 sc"

                    }

                    show()
                }
            }
        }




        addWord.setOnClickListener {
            if (timer.text!="0 sc") {return@setOnClickListener}
            else if (editText.text.toString().isEmpty()) {
                Toast.makeText(this,"Please enter a word to add",Toast.LENGTH_LONG).show()
            }
             else if (!dbManager.readdb().contains(editText.text.toString())) {
                dbManager.addWord(editText.text.toString())
                val builder = AlertDialog.Builder(this)
                with(builder) {
                    setTitle("Word added")
                    setMessage("The word ${editText.text.toString().uppercase()} was added to the DB," +
                            "the DB contains ${dbManager.readdb().size} words")
                    setPositiveButton("Ok") {_,_,->
                    }
                    show()
                }
             }
        }

    }

    private fun jointToString(list: MutableList<String>): String {
        var word = ""
        for (i in list) {
            word += i
        }
        return word
    }


}