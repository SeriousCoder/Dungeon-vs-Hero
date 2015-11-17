package com.mygdx.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.Helpers.AssetLoader
import com.mygdx.game.DvHGame

public class MainScreen(val game : DvHGame) : Screen
{
    private val batcher = SpriteBatch()
    private val stage   = Stage(FitViewport(360f, 640f), batcher)

    val font = AssetLoader.generateFont("Doux Medium.ttf", 40, Color.WHITE)

    init
    {
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
        val play = ImageButton(AssetLoader.getImageButtonStyle(0, 0, 0, 0, 150, 150))

        play.addListener(object : ClickListener()
        {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = LevelScreen(AssetLoader, game)
            }
        })

        val titleStyle = Label.LabelStyle()
        titleStyle.font = font

        val title1 = Label("Dungeon", titleStyle)
        val title2 = Label("vs", titleStyle)
        val title3 = Label("Hero", titleStyle)

        title1.setPosition(105f, 460f)
        title2.setPosition(160f, 430f)
        title3.setPosition(135f, 400f)

        play.setPosition(110f, 120f)

        stage.addActor(title1)
        stage.addActor(title2)
        stage.addActor(title3)
        stage.addActor(play)

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
        Gdx.gl.glClearColor(0/255.0f, 0/255.0f, 0/255.0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val fps = (1/delta)

        batcher.begin()
        batcher.draw(AssetLoader.backgroungs, 0f, 0f)
        batcher.end()

        Gdx.app.log("FPS", fps.toString() + "")

        stage.act(delta)
        stage.draw()
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
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