package com.scholze.entregaparcial.galinha

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Car(var x: Float, val y: Float, private val speed: Float) {
    private val width = 200f
    private val height = 100f

    companion object {
        var currentSpeed = 0.5f

        fun increaseSpeed() {
            currentSpeed += 0.05f
            if (currentSpeed > 0.9f) currentSpeed = 0.9f
        }

        fun resetSpeed() {
            currentSpeed = 0.5f
        }
    }

    fun update(deltaTime: Float) {
        x += speed * deltaTime * 1000
    }

    fun draw(canvas: Canvas) {
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
        }
        canvas.drawRect(x, y - height / 2, x + width, y + height / 2, paint)

        paint.color = Color.BLACK
        canvas.drawRect(x + 20, y - height / 2 + 15, x + width - 20, y + height / 2 - 15, paint)
    }

    fun collidesWith(cx: Float, cy: Float, size: Float): Boolean {
        return x < cx + size &&
                x + width > cx &&
                y - height / 2 < cy + size &&
                y + height / 2 > cy
    }
}
