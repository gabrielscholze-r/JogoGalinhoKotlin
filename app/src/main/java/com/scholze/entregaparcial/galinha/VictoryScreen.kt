package com.scholze.entregaparcial.galinha

import android.graphics.Color
import android.view.MotionEvent
import com.scholze.entregaparcial.Game
import com.scholze.entregaparcial.Screen

class VictoryScreen(game: Game, private val score: Int) : Screen(game) {

    init {
        paint.color = Color.WHITE
        paint.textSize = 80f
        paint.textAlign = android.graphics.Paint.Align.CENTER
    }

    override fun update(et: Float) {}

    override fun draw() {
        canvas?.let {
            it.drawColor(Color.GREEN)
            it.drawText("Você Venceu!", it.width / 2f, it.height / 2f - 100, paint)

            paint.textSize = 60f
            it.drawText("Pontuação: $score", it.width / 2f, it.height / 2f, paint)

            paint.textSize = 40f
            it.drawText("Toque para continuar", it.width / 2f, it.height / 2f + 150, paint)
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