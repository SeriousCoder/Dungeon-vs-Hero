package com.mygdx.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.mygdx.game.DvHGame

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()

    config.title = "Dungeon vs Hero"

    LwjglApplication(DvHGame(), config)
}
