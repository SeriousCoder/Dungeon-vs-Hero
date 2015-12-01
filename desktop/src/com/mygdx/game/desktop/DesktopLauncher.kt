package com.mygdx.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.mygdx.game.DvHGame

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()

    config.title = "Dungeon vs Hero"
    config.height = 640
    config.width = 360
    LwjglApplication(DvHGame(), config)
}
