package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.Helpers.AssertLoader
import com.mygdx.screens.*


/*public class NNGame2 : ApplicationAdapter() {


    override fun create() {
        var batch = SpriteBatch()
        var img = Texture("badlogic.jpg")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    }
}*/

public class DvHGame() : Game() {

    public override fun create() {
        Gdx.app.log("Game", "created")
        val assert = AssertLoader()
        assert.load()
        setScreen(MainScreen(assert, this))
    }

    public override fun dispose() {
        super.dispose()
    }


}