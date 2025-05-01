package com.scholze.entregaparcial.galinha

import android.graphics.Color
import android.view.MotionEvent
import com.scholze.entregaparcial.Game
import com.scholze.entregaparcial.Screen
import kotlin.random.Random
import kotlin.math.abs


class GameScreen(game: Game) : Screen(game) {

    private val chickenX = 500f
    private var chickenY = 1800f
    private val chickenSize = 100f
    private val lanes = List(10) { 800f + it * 100f }
    private var isMoving = false
    private val MOVE_SPEED = 800f

    private val cars = mutableListOf<Car>()
    private var timeSinceLastCar = 0f
    private var isGameOver = false
    private var score = 0
    private var currentLane = lanes.size
    private var targetY = chickenY

    init {
        paint.color = Color.YELLOW
    }

    override fun update(et: Float) {
        if (isGameOver) return

        if (isMoving) {
            val direction = if (targetY > chickenY) 1 else -1
            chickenY += direction * MOVE_SPEED * et

            if (abs(chickenY - targetY) < 10f) {
                chickenY = targetY
                isMoving = false

                if (currentLane == -1) {
                    score++
                    if (score >= 2) {
                        game.actualScreen = VictoryScreen(game, score)
                        return
                    }
                    resetChicken()
                }
            }
        }

        timeSinceLastCar += et
        if (timeSinceLastCar > 1.3f) {
            if (cars.size < lanes.size * 2) {
                val laneIndex = Random.nextInt(lanes.size)
                cars.add(Car(-200f, lanes[laneIndex], Car.currentSpeed))
            }
            timeSinceLastCar = 0f
        }

        val iterator = cars.iterator()
        while (iterator.hasNext()) {
            val car = iterator.next()
            car.update(et)

            if (car.x > game.screenWidth + 200) {
                iterator.remove()
            } else if (car.collidesWith(chickenX, chickenY, chickenSize)) {
                isGameOver = true
                game.actualScreen = GameOverScreen(game, score)
                return
            }
        }
    }

    private fun resetChicken() {
        chickenY = 1800f
        currentLane = lanes.size
        Car.increaseSpeed()
    }

    override fun draw() {
        canvas?.let { c ->
            c.drawColor(Color.DKGRAY)

            paint.color = Color.GRAY
            c.drawRect(0f, lanes.first() - 50f, c.width.toFloat(), lanes.last() + 150f, paint)

            paint.color = Color.WHITE
            paint.strokeWidth = 10f
            lanes.forEach { lane ->
                c.drawLine(0f, lane, c.width.toFloat(), lane, paint)
            }

            cars.forEach { it.draw(c) }

            paint.color = Color.YELLOW
            c.drawRect(chickenX, chickenY, chickenX + chickenSize, chickenY + chickenSize, paint)

            paint.color = Color.WHITE
            paint.textSize = 60f
            c.drawText("Pontos: $score", 50f, 100f, paint)
        }
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN && !isGameOver && !isMoving) {
            currentLane--
            targetY = if (currentLane >= 0) {
                lanes[currentLane] - chickenSize / 2
            } else {
                lanes[0] - 150f
            }
            isMoving = true
        }
    }

    override fun onPause() {}
    override fun onResume() {}
    override fun backPressed() {}
}

class Car(var x: Float, val y: Float, private val speed: Float) {
    private val width = 200f
    private val height = 100f

    companion object {
        var currentSpeed = 300f
        fun increaseSpeed() {
            currentSpeed += 30f
            if (currentSpeed > 500f) currentSpeed = 500f
        }
    }

    fun update(deltaTime: Float) {
        x += currentSpeed * deltaTime
    }

    fun draw(canvas: android.graphics.Canvas) {
        val paint = android.graphics.Paint().apply {
            color = Color.RED
            style = android.graphics.Paint.Style.FILL
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
