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

public class ChoosingScreen(game : DvHGame, prevLevelScreen: LevelScreen? = null) : Screen
{
    private val batcher = SpriteBatch()
    private val stage   = Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()), batcher)

    val font = AssetLoader.generateFont("Doux Medium.ttf", 40, Color.WHITE)

    init
    {
        prevLevelScreen?.dispose()
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
        val skin = Skin(Gdx.files.internal("uiskin.json"));
        val single = TextButton("One player", skin, "default")
        val multi = TextButton("Two players", skin, "default")

        val width = 250f
        val height = 70f

        single.width = width
        single.height = height

        multi.width = width
        multi.height = height

        single.addListener(object : ClickListener()
        {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = LevelScreen(game)
            }
        })

        multi.addListener(object : ClickListener()
        {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = LevelScreen(game)
            }
        })

        val screenHeight = Gdx.graphics.height.toFloat()
        val screenWidth = Gdx.graphics.width.toFloat()

        single.setPosition((screenWidth - width) / 2, screenHeight/ 3 - height /2)
        multi.setPosition((screenWidth - width) / 2, 2 * screenHeight /3 - height / 2)

        stage.addActor(single)
        stage.addActor(multi)
        Gdx.input.inputProcessor = stage
    }



    override fun pause() {
       // Gdx.app.log("Screen", "paused")
    }

    override fun resize(width: Int, height: Int) {
        //Gdx.app.log("Screen", "resized")
    }

    override fun hide() {
       // Gdx.app.log("Screen", "hidden")
    }

    override fun render(delta: Float)
    {
        Gdx.gl.glClearColor(0/255.0f, 0/255.0f, 0/255.0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val fps = (1/delta)

        batcher.begin()
        batcher.draw(AssetLoader.backgroungs, 0f, 0f)
        batcher.end()

        //      Gdx.app.log("FPS", fps.toString() + "")

        stage.act(delta)
        stage.draw()
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
    }

    override fun resume() {
      //  Gdx.app.log("Screen", "resumed")
    }

    override fun dispose() {
          stage.dispose()
    }

    override fun show() {
       // Gdx.app.log("Screen", "showed")
    }

}