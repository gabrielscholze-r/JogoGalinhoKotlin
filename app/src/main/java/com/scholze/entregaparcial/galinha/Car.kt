package com.scholze.entregaparcial.galinha

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.scholze.entregaparcial.Vector2

class Car(initialX: Float, initialY: Float, speed: Float, private val screenWidth: Int) {
    val position = Vector2(initialX, initialY)
    val velocity = Vector2(speed * 1000, 0f)
    private val size = Vector2(200f, 100f)
    var isActive = true

    companion object {
        var currentSpeed = 0.5f
        private const val MAX_SPEED = 0.9f
        private const val SPEED_INCREMENT = 0.05f

        fun increaseSpeed() {
            currentSpeed = (currentSpeed + SPEED_INCREMENT).coerceAtMost(MAX_SPEED)
        }

        fun resetSpeed() {
            currentSpeed = 0.5f
        }
    }

    fun update(deltaTime: Float) {
        if (!isActive) return

        position.x += velocity.x * deltaTime

        if (position.x > screenWidth + size.x * 2) {
            isActive = false
        }
    }

    fun draw(canvas: Canvas) {
        if (!isActive) return

        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
        }

        canvas.drawRect(
            position.x,
            position.y - size.y / 2,
            position.x + size.x,
            position.y + size.y / 2,
            paint
        )

        paint.color = Color.BLACK
        canvas.drawRect(
            position.x + 20,
            position.y - size.y / 2 + 15,
            position.x + size.x - 20,
            position.y + size.y / 2 - 15,
            paint
        )
    }

    fun collidesWith(cx: Float, cy: Float, size: Float): Boolean {
        if (!isActive) return false

        return position.x < cx + size &&
                position.x + this.size.x > cx &&
                position.y - this.size.y / 2 < cy + size &&
                position.y + this.size.y / 2 > cy
    }

    val x: Float get() = position.x
    val y: Float get() = position.y
}