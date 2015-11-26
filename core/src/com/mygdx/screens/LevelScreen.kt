package com.mygdx.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.Helpers.AssetLoader
import com.mygdx.game.DvHGame
import com.mygdx.game.GameWorld.GameRenderer
import com.mygdx.game.GameWorld.GameWorld
import kotlin.properties.Delegates


public class LevelScreen(val game : DvHGame) : Screen
{
    private var renderer : GameRenderer by Delegates.notNull<GameRenderer>()
    private var gameIsEnded = false
    init {
        AssetLoader.gameWorld = GameWorld()
        renderer = GameRenderer(AssetLoader.gameWorld)
        //without calling function below everything will go to hell crashing
        AssetLoader.gameWorld.initPlayersAndEverythingNeedingThem(renderer)
    }

    private fun getThisScreen() : LevelScreen {
        return this
    }

    override fun render(delta: Float) {
        if (gameIsEnded) {
            renderAfterEndOfGame()
            return
        }

        val winner = renderer.render()
        if (winner != -1) {
            //game.screen = EndGameScreen(winner, this, game)
            gameIsEnded = true

            val skin = Skin(Gdx.files.internal("uiskin.json"));
            val restart = TextButton("Player ${1 + winner} wins!", skin, "default")

            val width = 250f
            val height = 70f

            restart.width = width
            restart.height = height
            restart.addListener(object : ClickListener()
            {
                override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                    game.screen = ChoosingScreen(game, getThisScreen())
                }
            })

            val screenHeight = Gdx.graphics.height.toFloat()
            val screenWidth = Gdx.graphics.width.toFloat()

            restart.setPosition((screenWidth - width) / 2, screenHeight/ 2 - height /2)

//            for (i in AssetLoader.gameWorld.stage.actors)
//                i.clearListeners()
            AssetLoader.gameWorld.stage.addActor(restart)
            Gdx.input.inputProcessor = AssetLoader.gameWorld.stage
        }
    }

    private fun renderAfterEndOfGame() {
        renderer.render(false)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//        AssetLoader.gameWorld.stage.draw()
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