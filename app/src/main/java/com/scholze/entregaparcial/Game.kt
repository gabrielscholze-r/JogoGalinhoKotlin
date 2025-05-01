package com.scholze.entregaparcial

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class Game(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    var screenWidth = 0
    var screenHeight = 0
    var actualScreen: Screen = FirstScreen(this)
    private var thread: Thread? = null
    private var isRunning = false

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        screenWidth = width
        screenHeight = height
        resume()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        pause()
    }

    fun pause() {
        isRunning = false
        thread?.join()
        thread = null
        actualScreen.onPause()
    }

    fun resume() {
        isRunning = true
        thread = Thread(this)
        thread?.start()
        actualScreen.onResume()
    }

    override fun run() {
        var lastTime = System.nanoTime()
        val nsPerUpdate = 1000000000.0 / 60.0
        var delta = 0.0

        while (isRunning) {
            val now = System.nanoTime()
            delta += (now - lastTime) / nsPerUpdate
            lastTime = now

            while (delta >= 1) {
                update(1f / 60f)
                delta--
            }

            render()
        }
    }

    private fun update(et: Float) {
        actualScreen.update(et)
    }

    private fun render() {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            if (canvas != null) {
                actualScreen.canvas = canvas
                actualScreen.draw()
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        actualScreen.handleEvent(event.action, event.x, event.y)
        return true
    }

    fun backPressed() {
        actualScreen.backPressed()
    }
}