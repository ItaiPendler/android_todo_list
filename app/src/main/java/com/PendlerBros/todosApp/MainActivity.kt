package com.PendlerBros.todosApp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val helloWorldTextView = findViewById<TextView>(R.id.hello_world_text)
        val editText = findViewById<EditText>(R.id.edittext)
        val enterButton = findViewById<Button>(R.id.enterButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val markTodoButton = findViewById<Button>(R.id.markTodoButton)

        val preferences = this.getPreferences(Context.MODE_PRIVATE)
        val prefString = preferences.getString(getString(R.string.string_set_key), "") ?: ""
        val stringArr = mutableListOf<String>()
        stringArr.addAll(0, prefString.split(','))

        //removing all blank todos
        stringArr.removeIf { todo -> todo.isBlank() }

        nextButton.isEnabled = stringArr.isNotEmpty()
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 500
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 500
        var index = 0

        fun goToNextTodo() {
            if (stringArr.size == 0) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.no_todos),
                    Toast.LENGTH_SHORT
                ).show()
                helloWorldTextView.text = getString(R.string.starting_text)
                return
            }
            if (index == stringArr.size) {
                index = 0
            }
            helloWorldTextView.startAnimation(fadeOut)
        }

        fun deleteList() {
            index = 0
            stringArr.clear()
            preferences.edit().remove(getString(R.string.string_set_key)).apply()
            nextButton.isEnabled = false
            helloWorldTextView.text = getString(R.string.starting_text)
        }

        fun updateSavedList() {
            with(preferences.edit()) {
                val stringToSave = stringArr.toTypedArray().joinToString(",")
                putString(getString(R.string.string_set_key), stringToSave)
                apply()
            }
        }

        fun addTodoToList(todo: String) {
            stringArr.add(todo)
            updateSavedList()
        }

        fun deleteATodo() {
            if (stringArr.size == 0 || helloWorldTextView.text == getString(R.string.starting_text)) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.no_todos),
                    Toast.LENGTH_SHORT
                ).show()
                helloWorldTextView.text = getString(R.string.starting_text)
                return
            }
            stringArr.removeAt(--index)
            updateSavedList()
            goToNextTodo()
        }


        enterButton.setOnClickListener {

            val tempText = editText.text.toString()
            if (tempText.isNotEmpty()) {
                addTodoToList(tempText)
                nextButton.isEnabled = true
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.no_blank_todo),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            editText.text.clear()
        }

        fadeOut.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val stringToShow = stringArr[index++]
                helloWorldTextView.text = stringToShow

                helloWorldTextView.startAnimation(fadeIn)
            }
        })
        fadeIn.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                helloWorldTextView.visibility = View.VISIBLE
            }
        })
        helloWorldTextView.startAnimation(fadeIn)

        helloWorldTextView.setOnClickListener {
            goToNextTodo()
        }



        nextButton.setOnClickListener {
            goToNextTodo()
        }
        clearButton.setOnClickListener {
            deleteList()
        }

        markTodoButton.setOnClickListener { deleteATodo() }
    }
}