package com.scholze.entregaparcial.galinha

import android.graphics.Color
import android.view.MotionEvent
import com.scholze.entregaparcial.Game
import com.scholze.entregaparcial.Screen
import kotlin.random.Random

class GameScreen(game: Game) : Screen(game) {

    private val chickenX = 500f
    private var chickenY = 1500f
    private val chickenSize = 100f
    private val lanes = List(10) { 600f + it * 100f }
    private var isMoving = false
    private val moveSpeed = 15f

    private val cars = mutableListOf<Car>()
    private var timeSinceLastCar = 0f
    private var isGameOver = false
    private var score = 0
    private var currentLane = lanes.size - 1

    init {
        paint.color = Color.YELLOW
    }

    override fun update(et: Float) {
        if (isGameOver) return

        val deltaTime = et * 1000f

        if (isMoving) {
            chickenY -= moveSpeed
            if (chickenY <= lanes[currentLane] - chickenSize/2) {
                chickenY = lanes[currentLane] - chickenSize/2
                isMoving = false

                if (currentLane == 0) {
                    score++
                    if (score >= 2) {
                        game.actualScreen = VictoryScreen(game, score)
                        return
                    }
                    chickenY = lanes.last() + 100f
                    currentLane = lanes.size - 1
                    Car.increaseSpeed()
                }
            }
        }

        timeSinceLastCar += deltaTime
        if (timeSinceLastCar > 1300f) {
            if (cars.size < lanes.size * 2) {
                val laneIndex = Random.nextInt(lanes.size)
                cars.add(Car(-200f, lanes[laneIndex], Car.currentSpeed))
            }
            timeSinceLastCar = 0f
        }

        val iterator = cars.iterator()
        while (iterator.hasNext()) {
            val car = iterator.next()
            car.update(deltaTime)

            if (car.x > game.screenWidth + 200) {
                iterator.remove()
            } else if (car.collidesWith(chickenX, chickenY, chickenSize)) {
                isGameOver = true
                game.actualScreen = GameOverScreen(game, score)
                return
            }
        }
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
        if (event == MotionEvent.ACTION_DOWN && !isGameOver && !isMoving && currentLane > 0) {
            currentLane--
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
        var currentSpeed = 0.3f
        fun increaseSpeed() {
            currentSpeed += 0.03f
            if (currentSpeed > 0.5f) currentSpeed = 0.5f
        }
    }

    fun update(deltaTime: Float) {
        x += speed * deltaTime
    }

    fun draw(canvas: android.graphics.Canvas) {
        val paint = android.graphics.Paint().apply {
            color = Color.RED
            style = android.graphics.Paint.Style.FILL
        }
        canvas.drawRect(x, y - height/2, x + width, y + height/2, paint)

        paint.color = Color.BLACK
        canvas.drawRect(x + 20, y - height/2 + 15, x + width - 20, y + height/2 - 15, paint)
    }

    fun collidesWith(cx: Float, cy: Float, size: Float): Boolean {
        return x < cx + size &&
                x + width > cx &&
                y - height/2 < cy + size &&
                y + height/2 > cy
    }
}