package com.scholze.entregaparcial

import android.graphics.Color
import android.view.MotionEvent
import com.scholze.entregaparcial.galinha.GameScreen

class FirstScreen(game: Game) : Screen(game) {
    private var animationOffset = 0f
    private var growing = true

    init {
        paint.color = Color.WHITE
        paint.textSize = 80f
        paint.textAlign = android.graphics.Paint.Align.CENTER
    }

    override fun update(et: Float) {
        if (growing) {
            animationOffset += et * 20
            if (animationOffset > 20f) growing = false
        } else {
            animationOffset -= et * 20
            if (animationOffset < 0f) growing = true
        }
    }

    override fun draw() {
        canvas?.let {
            it.drawColor(Color.BLUE)

            paint.textSize = 80f + animationOffset
            it.drawText("Galinha na Rua", it.width / 2f, it.height / 2f - 100, paint)

            paint.textSize = 60f
            it.drawText("Toque para começar", it.width / 2f, it.height / 2f + 100, paint)

            paint.textSize = 50f
            it.drawText("Use toques para pular as pistas", it.width / 2f, it.height / 2f + 200, paint)
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