package com.example.ecoidler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost)

        val button: Button = findViewById(R.id.restart)

        button.setOnClickListener {
            val intent = Intent(this@LostActivity, MainActivity::class.java)
            finish()
            startActivity(intent)
        }

    }
}