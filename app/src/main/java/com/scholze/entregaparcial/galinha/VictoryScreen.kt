package com.scholze.entregaparcial.galinha

import android.graphics.Color
import android.view.MotionEvent
import com.scholze.entregaparcial.FirstScreen
import com.scholze.entregaparcial.Game
import com.scholze.entregaparcial.Screen

class VictoryScreen(game: Game, private val score: Int, private val isNewRecord: Boolean = false) : Screen(game) {
    init {
        paint.color = Color.WHITE
        paint.textSize = 80f
        paint.textAlign = android.graphics.Paint.Align.CENTER
    }

    override fun update(et: Float) {}

    override fun draw() {
        canvas?.let {
            val bgColor = if (isNewRecord) Color.argb(200, 0, 150, 0) else Color.argb(200, 0, 100, 0)
            it.drawColor(bgColor)

            it.drawText("Vitória!", it.width / 2f, it.height / 2f - 150, paint)

            paint.textSize = 60f
            it.drawText("Pontuação: $score", it.width / 2f, it.height / 2f - 50, paint)

            if (isNewRecord) {
                it.drawText("Novo Recorde!", it.width / 2f, it.height / 2f + 50, paint)
            }

            paint.textSize = 40f
            it.drawText("Toque para continuar", it.width / 2f, it.height / 2f + 150, paint)
        }
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN) {
            game.actualScreen = FirstScreen(game)
        }
    }

    override fun onPause() {}
    override fun onResume() {}
    override fun backPressed() {}
}