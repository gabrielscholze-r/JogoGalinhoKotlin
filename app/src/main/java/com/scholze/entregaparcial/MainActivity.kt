package com.scholze.entregaparcial

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        game = Game(this)
        setContentView(game)

        game.actualScreen = FirstScreen(game)
    }

    override fun onPause() {
        super.onPause()
        game.pause()
    }

    override fun onResume() {
        super.onResume()
        game.resume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        game.backPressed()
    }
}