package com.scholze.entregaparcial

import android.graphics.Color
import android.view.MotionEvent
import com.scholze.entregaparcial.galinha.GameScreen

class FirstScreen(game: Game) : Screen(game) {

    init {
        paint.color = Color.WHITE
        paint.textSize = 80f
        paint.textAlign = android.graphics.Paint.Align.CENTER
    }

    override fun update(et: Float) {}

    override fun draw() {
        canvas?.drawColor(Color.BLUE)
        canvas?.drawText("Galinha na Rua", canvas!!.width / 2f, canvas!!.height / 2f - 100, paint)
        paint.textSize = 50f
        canvas?.drawText("Toque para começar", canvas!!.width / 2f, canvas!!.height / 2f + 100, paint)
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