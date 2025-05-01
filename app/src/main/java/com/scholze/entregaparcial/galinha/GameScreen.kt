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

    private val cars = mutableListOf<Car>()
    private var timeSinceLastCar = 0f
    private var isGameOver = false
    private var score = 0

    init {
        paint.color = Color.YELLOW
    }

    override fun update(et: Float) {
        if (isGameOver) return

        // Conversão correta do tempo (et estava muito pequeno)
        val deltaTime = et * 1000f  // Convertendo para milissegundos

        if (chickenY <= 0) {
            score++
            chickenY = 1500f
            Car.increaseSpeed()
            return
        }

        timeSinceLastCar += deltaTime
        if (timeSinceLastCar > 300f) {  // 300ms entre carros
            val lane = listOf(600f, 800f, 1000f, 1200f).random()
            cars.add(Car(-200f, lane, Car.currentSpeed))
            timeSinceLastCar = 0f
        }

        val iterator = cars.iterator()
        while (iterator.hasNext()) {
            val car = iterator.next()
            car.update(deltaTime)
            if (car.x > game.screenWidth + 200) iterator.remove()
            if (car.collidesWith(chickenX, chickenY, chickenSize)) {
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
            c.drawRect(0f, 500f, c.width.toFloat(), 1500f, paint)

            paint.color = Color.WHITE
            paint.strokeWidth = 10f
            for (i in 500..1500 step 200) {
                c.drawLine(0f, i.toFloat(), c.width.toFloat(), i.toFloat(), paint)
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
        if (event == MotionEvent.ACTION_DOWN && !isGameOver) {
            chickenY -= 300f  // Movimento mais rápido
            if (chickenY < 0) chickenY = 0f
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
        var currentSpeed = 15f  // Valor otimizado
        fun increaseSpeed() {
            currentSpeed += 5f  // Incremento otimizado
            if (currentSpeed > 30f) currentSpeed = 30f
        }
    }

    fun update(deltaTime: Float) {
        x += speed * (deltaTime / 16f)  // Fator de normalização
    }

    fun draw(canvas: android.graphics.Canvas) {
        val paint = android.graphics.Paint().apply {
            color = Color.RED
            style = android.graphics.Paint.Style.FILL
        }
        canvas.drawRect(x, y, x + width, y + height, paint)

        paint.color = Color.BLACK
        canvas.drawRect(x + 20, y + 15, x + width - 20, y + height - 15, paint)
    }

    fun collidesWith(cx: Float, cy: Float, size: Float): Boolean {
        return x < cx + size &&
                x + width > cx &&
                y < cy + size &&
                y + height > cy
    }
}