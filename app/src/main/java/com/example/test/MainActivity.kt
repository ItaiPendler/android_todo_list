package com.example.test

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.view.animation.Animation.AnimationListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        val preferences = this.getPreferences(Context.MODE_PRIVATE);

        val fadeIn = AlphaAnimation(0f, 1f);
        fadeIn.interpolator = DecelerateInterpolator(); //add this
        fadeIn.duration = 500;
        val fadeOut = AlphaAnimation(1f, 0f);
        fadeOut.interpolator = AccelerateInterpolator(); //and this
        fadeOut.duration = 500;
        var index = 0;
        val stringArr = mutableSetOf(
            getString(R.string.app_name),
            getString(R.string.appbar_scrolling_view_behavior),
            getString(R.string.fab_transformation_sheet_behavior)
        );
        val helloWorldTextView = findViewById<TextView>(R.id.hello_world_text);
        val editText = findViewById<EditText>(R.id.edittext);
        val enterButton = findViewById<Button>(R.id.enterButton);
        enterButton.setOnClickListener {
            val tempText = editText.text.toString();
            stringArr.add(tempText);
            preferences.edit().putStringSet(getString(R.string.string_set_key), stringArr).apply();
            editText.text.clear();
        }
        fadeOut.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (index == stringArr.size) {
                    index = 0
                }
                helloWorldTextView.text = stringArr.elementAt(index++);
                helloWorldTextView.startAnimation(fadeIn);
            }
        })
        fadeIn.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                helloWorldTextView.visibility = View.VISIBLE;
            }
        })
        helloWorldTextView.startAnimation(fadeIn);

        helloWorldTextView.setOnClickListener(View.OnClickListener {
            helloWorldTextView.startAnimation(fadeOut);
        });

    }
}