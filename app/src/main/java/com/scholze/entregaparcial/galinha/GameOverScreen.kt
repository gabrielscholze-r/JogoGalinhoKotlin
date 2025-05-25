package com.scholze.entregaparcial.galinha

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import com.scholze.entregaparcial.Game
import com.scholze.entregaparcial.Screen

class GameOverScreen(game: Game, private val score: Int) : Screen(game) {
    private val highScore: Int

    init {
        val sharedPref = game.context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        highScore = sharedPref.getInt("high_score", 0)

        if (score > highScore) {
            with(sharedPref.edit()) {
                putInt("high_score", score)
                apply()
            }
        }

        paint.color = Color.WHITE
        paint.textSize = 80f
        paint.textAlign = android.graphics.Paint.Align.CENTER
    }

    override fun update(et: Float) {}

    override fun draw() {
        canvas?.let {
            it.drawColor(Color.argb(200, 0, 0, 0))

            it.drawText("Game Over", it.width / 2f, it.height / 2f - 150, paint)

            paint.textSize = 60f
            it.drawText("Pontuação: $score", it.width / 2f, it.height / 2f - 50, paint)

            val highScoreText = if (score > highScore) {
                "Novo Recorde: $score"
            } else {
                "Recorde: $highScore"
            }
            it.drawText(highScoreText, it.width / 2f, it.height / 2f + 50, paint)

            paint.textSize = 40f
            it.drawText("Toque para jogar novamente", it.width / 2f, it.height / 2f + 150, paint)
        }
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN) {
            game.actualScreen = GameScreen(game)
        }
    }

    override fun onPause() {}
    override fun onResume() {}
    override fun backPressed() {}
}