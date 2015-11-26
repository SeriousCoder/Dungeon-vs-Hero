package com.mygdx.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
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

public class EndGameScreen(winner : Int, prevGame : LevelScreen, game : DvHGame) : Screen
{
    private val stage   = Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
    val font = AssetLoader.generateFont("Doux Medium.ttf", 40, Color.WHITE)

    init
    {
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
        val skin = Skin(Gdx.files.internal("uiskin.json"));
        val restart = TextButton("Player ${1 + winner} wins!", skin, "default")

        val width = 250f
        val height = 70f

        restart.width = width
        restart.height = height
        restart.addListener(object : ClickListener()
        {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                prevGame.dispose()
                game.screen = ChoosingScreen(game)
            }
        })

        val screenHeight = Gdx.graphics.height.toFloat()
        val screenWidth = Gdx.graphics.width.toFloat()

        restart.setPosition((screenWidth - width) / 2, screenHeight/ 3 - height /2)

        stage.addActor(restart)
        Gdx.input.inputProcessor = stage
        stage.draw()
    }


    override fun render(delta: Float)
    {
//        Gdx.gl.glClearColor(0/255.0f, 0/255.0f, 0/255.0f, 1f)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

   //     stage.act(delta)
       // stage.draw()
     //   stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
    }

    override fun dispose() {}
    override fun show() {}
    override fun pause() {}
    override fun resize(width: Int, height: Int) {}
    override fun hide() {}
    override fun resume() {}
}