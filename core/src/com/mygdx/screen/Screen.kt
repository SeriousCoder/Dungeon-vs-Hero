package com.mygdx.Screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.mygdx.AssertLoader

public class DvHScreen (val assert : AssertLoader) : Screen
{
    private val batcher = SpriteBatch()
    private val stage   = Stage()

    val font = assert.generateFont("Doux Medium.ttf", 60, Color.WHITE)

    init
    {
        val titleStyle = Label.LabelStyle()
        titleStyle.font = font

        val title = Label("Dungeon vs Hero", titleStyle)
        
        title.setPosition(100f, 350f)

        stage.addActor(title)

        Gdx.input.inputProcessor = stage
    }



    override fun pause() {
        Gdx.app.log("Screen", "paused")
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log("Screen", "resized")
    }

    override fun hide() {
        Gdx.app.log("Screen", "hided")
    }

    override fun render(delta: Float)
    {
        Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val fps = (1/delta)

        batcher.begin()
        batcher.disableBlending()
        batcher.draw(assert.startImage, 0f, 0f)
        batcher.end()

        Gdx.app.log("FPS", fps.toString() + "")

        stage.act(delta)
        stage.draw()
    }

    override fun resume() {
        Gdx.app.log("Screen", "resumed")
    }

    override fun dispose() {
        Gdx.app.log("Screen", "disposed")
    }

    override fun show() {
        Gdx.app.log("Screen", "showed")
    }

}