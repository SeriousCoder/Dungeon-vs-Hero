package com.mygdx.screens

import com.badlogic.gdx.Screen
import com.mygdx.Helpers.AssetLoader
import com.mygdx.game.DvHGame
import com.mygdx.game.GameWorld.GameRenderer
import com.mygdx.game.GameWorld.GameWorld
import kotlin.properties.Delegates


public class LevelScreen(game : DvHGame) : Screen
{
    private var renderer : GameRenderer by Delegates.notNull<GameRenderer>()

    init {
        AssetLoader.gameWorld = GameWorld()
        renderer = GameRenderer(AssetLoader.gameWorld)
        //without calling function below everything will go to hell crashing
        AssetLoader.gameWorld.initPlayersAndEverythingNeedingThem(renderer)
    }

    override fun render(delta: Float) {
        renderer.render()
    }

    override fun dispose() {
        renderer.dispose()

        AssetLoader.gameWorld.dispose()
    }

    override fun show() {}
    override fun pause() {}
    override fun resize(width: Int, height: Int) {}
    override fun hide() {}
    override fun resume() {}
}