package com.scholze.entregaparcial.galinha

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import com.scholze.entregaparcial.Game
import com.scholze.entregaparcial.Screen
import com.scholze.entregaparcial.Vector2
import kotlin.random.Random

class GameScreen(game: Game) : Screen(game) {
    private val lanes = List(10) { 800f + it * 100f }
    private val chickenSize = 100f
    private val startPositionOffset = 150f

    private val chickenPosition = Vector2(500f, lanes.last() + startPositionOffset)
    private var currentLane = lanes.lastIndex
    private val chickenVelocity = Vector2()
    private var targetLaneY = lanes.last() + startPositionOffset

    private val cars = mutableListOf<Car>()
    private var timeSinceLastCar = 0f
    private var lives = 3
    private var score = 0
    private var isJumping = false
    private val gravity = 1200f
    private val jumpForce = -1000f
    private var isHit = false
    private var hitEffectTimer = 0f

    init {
        paint.color = Color.YELLOW
        Car.resetSpeed()
    }

    override fun update(et: Float) {
        if (lives <= 0) {
            checkHighScoreAndShowScreen()
            return
        }

        updateChicken(et)
        updateCars(et)
        updateEffects(et)
    }

    private fun checkHighScoreAndShowScreen() {
        val highScore = getHighScore()
        if (score > highScore) {
            saveHighScore(score)
            game.actualScreen = VictoryScreen(game, score, true)
        } else {
            game.actualScreen = GameOverScreen(game, score)
        }
    }

    private fun saveHighScore(newScore: Int) {
        val sharedPref = game.context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("high_score", newScore)
            apply()
        }
    }

    private fun getHighScore(): Int {
        val sharedPref = game.context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("high_score", 0)
    }

    private fun updateChicken(et: Float) {
        if (!isJumping) return

        chickenVelocity.y += gravity * et
        chickenPosition.y += chickenVelocity.y * et

        if ((chickenVelocity.y > 0 && chickenPosition.y >= targetLaneY) ||
            (chickenVelocity.y < 0 && chickenPosition.y <= targetLaneY)
        ) {
            chickenPosition.y = targetLaneY
            chickenVelocity.y = 0f
            isJumping = false
        }
    }

    private fun updateCars(et: Float) {
        timeSinceLastCar += et

        if (timeSinceLastCar > 1.5f) {
            val laneIndex = Random.nextInt(lanes.size)
            if (cars.count { it.y == lanes[laneIndex] } < 1) {
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
            } else if (!isHit && car.collidesWith(
                    chickenPosition.x,
                    chickenPosition.y,
                    chickenSize
                )
            ) {
                loseLife()
            }
        }
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN && !isHit) {
            if (!isJumping) {
                if (currentLane == lanes.size) {
                    currentLane = lanes.lastIndex
                    targetLaneY = lanes.last()
                    chickenVelocity.y = jumpForce
                    isJumping = true
                } else if (currentLane > 0) {
                    jumpToNextLane()
                } else if (currentLane == 0) {
                    completeLevel()
                }
            }
        }
    }

    private fun jumpToNextLane() {
        currentLane--
        targetLaneY = lanes[currentLane]
        chickenVelocity.y = jumpForce
        isJumping = true
    }

    private fun completeLevel() {
        score++
        Car.increaseSpeed()
        resetChickenPosition()
    }

    private fun resetChickenPosition() {
        currentLane = lanes.lastIndex
        chickenPosition.y = lanes.last() + startPositionOffset
        targetLaneY = lanes.last() + startPositionOffset
        chickenVelocity.y = 0f
        isJumping = false
    }

    private fun loseLife() {
        lives--
        isHit = true
        hitEffectTimer = 0f
        resetChickenPosition()
    }

    private fun updateEffects(et: Float) {
        if (!isHit) return
        hitEffectTimer += et
        if (hitEffectTimer > 0.5f) {
            isHit = false
            hitEffectTimer = 0f
        }
    }

    override fun draw() {
        canvas?.let { c ->
            c.drawColor(if (isHit) Color.argb(100, 255, 0, 0) else Color.DKGRAY)

            paint.color = Color.GRAY
            c.drawRect(0f, lanes.first() - 50f, c.width.toFloat(), lanes.last() + startPositionOffset + chickenSize, paint)

            paint.color = Color.WHITE
            paint.strokeWidth = 10f
            lanes.forEach { lane -> c.drawLine(0f, lane, c.width.toFloat(), lane, paint) }

            cars.forEach { it.draw(c) }

            paint.color = when {
                isHit -> Color.RED
                isJumping -> Color.argb(255, 255, 255, 0)
                else -> Color.YELLOW
            }
            c.drawRect(
                chickenPosition.x,
                chickenPosition.y,
                chickenPosition.x + chickenSize,
                chickenPosition.y + chickenSize,
                paint
            )

            paint.color = Color.WHITE
            paint.textSize = 60f
            c.drawText("Pontos: $score", 50f, 100f, paint)
            c.drawText("Vidas: $lives", 50f, 180f, paint)
            c.drawText("Recorde: ${getHighScore()}", 50f, 260f, paint)
        }
    }

    override fun onPause() {}
    override fun onResume() {}
    override fun backPressed() {}
}