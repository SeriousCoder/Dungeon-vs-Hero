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


public class LevelScreen(val game : DvHGame, ai : Boolean = false) : Screen
{
    private var renderer : GameRenderer by Delegates.notNull<GameRenderer>()
    private var gameIsEnded = false
    init {
        AssetLoader.gameWorld = GameWorld()
        renderer = GameRenderer(AssetLoader.gameWorld)
        //without calling function below everything will go to hell crashing
        AssetLoader.gameWorld.initPlayersAndEverythingNeedingThem(renderer, ai)
    }

    private fun getThisScreen() : LevelScreen {
        return this
    }

    override fun render(delta: Float) {
        val winner = renderer.render(!gameIsEnded)
        if (winner != -1) {
            //game.screen = EndGameScreen(winner, this, game)
            gameIsEnded = true

            val skin = Skin(Gdx.files.internal("uiskin.json"));
            var text : String? = null
            if (winner == 0) text = "Hero wins!"
            else text = "Dungeon wins!"
            val restart = TextButton(text, skin, "default")

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

            val screenHeight = Gdx.graphics.height
            val screenWidth = Gdx.graphics.width

            restart.setPosition((screenWidth - width) / 2, screenHeight/ 2 - height /2)

            AssetLoader.gameWorld.stage.addActor(restart)
            Gdx.input.inputProcessor = AssetLoader.gameWorld.stage
        }
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