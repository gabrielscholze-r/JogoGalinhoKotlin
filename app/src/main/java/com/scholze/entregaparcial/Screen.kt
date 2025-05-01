package com.scholze.entregaparcial

import android.graphics.Canvas

abstract class Screen(protected val game: Game) {
    var canvas: Canvas? = null
    protected val paint = android.graphics.Paint()

    abstract fun update(et: Float)
    abstract fun draw()
    abstract fun handleEvent(event: Int, x: Float, y: Float)
    abstract fun onPause()
    abstract fun onResume()
    abstract fun backPressed()
}