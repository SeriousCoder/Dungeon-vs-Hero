package com.mygdx.game.GameWorld

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonRegion
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.mygdx.game.Helpers.HexField
import com.mygdx.game.Helpers.HexPolygonActivated
import com.mygdx.game.Helpers.HexPolygonDefault
import kotlin.properties.Delegates

class GameRenderer(private val gameWorld : GameWorld) {

	private var polygon : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
	private var field : HexField by Delegates.notNull<HexField>()
	private var hex : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexActive : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var curPlayer = 0

    init {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        polygon = gameWorld.batch
        field   = gameWorld.field
        hex     = HexPolygonDefault(field.hexR.toFloat()).hexRegion
        hexActive = HexPolygonActivated(field.hexR.toFloat()).hexRegion
    }

    fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        polygon.begin()
        for (i in 0.. field.width - 1) {
            for (j in 0..field.height - 1) {
                val cur = field.field[i][j]
                var curText = hex
                if (cur.activated) {
                    curText = hexActive
                }
                polygon.draw(curText, cur.xl.toFloat() + (-47f + 20 * Math.sqrt(3.0) / 2).toFloat(),
                        cur.yl.toFloat() + (-50f + 20 * Math.sqrt(3.0) / 2).toFloat())
            }
        }
        polygon.end()

        if (gameWorld.players[curPlayer].getInput()) curPlayer = 1 - curPlayer

        gameWorld.update()
    }

    fun dispose() {
        //hex.dispose()
    }

}
