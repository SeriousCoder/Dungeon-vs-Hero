package com.mygdx.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.PolygonRegion
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.mygdx.Helpers.AssertLoader
import com.mygdx.game.DvHGame
import com.mygdx.game.gameWorld.GameWorld
import com.mygdx.game.hex.HexField
import com.mygdx.game.hex.HexPolygonDefault
import kotlin.properties.Delegates


public class LevelScreen(val assert : AssertLoader, game : DvHGame) : Screen
{
    private var batch : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()

    private var polygon : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
    private var field : HexField by Delegates.notNull<HexField>()
    private var hex : PolygonRegion by Delegates.notNull<PolygonRegion>()

    init {
        batch = PolygonSpriteBatch()

        assert.gameWorld = GameWorld(batch, HexField())

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        polygon = (assert.gameWorld as GameWorld).batch
        field   = (assert.gameWorld as GameWorld).field
        hex     = HexPolygonDefault(field.hexR.toFloat()).hexRegion
    }


    override fun show() {

    }

    override fun pause() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun hide() {

    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        polygon.begin()
        for (i in 0.. field.width - 1) {
            for (j in 0..field.height - 1) {
                val cur = field.field[i][j]
                polygon.draw(hex, cur.xl.toFloat() + (-47f + 20 * Math.sqrt(3.0) / 2).toFloat(),
                        cur.yl.toFloat() + (-50f + 20 * Math.sqrt(3.0) / 2).toFloat())
            }
        }
        polygon.end()

        assert.gameWorld!!.update()
    }

    override fun resume() {

    }

    override fun dispose() {

    }

}