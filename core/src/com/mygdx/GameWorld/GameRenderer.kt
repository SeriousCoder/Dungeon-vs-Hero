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
import com.badlogic.gdx.scenes.scene2d.Stage
import com.mygdx.game.Helpers.*
import kotlin.properties.Delegates

object  GameRenderer {

	private var polygon : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
	private var field : HexField by Delegates.notNull<HexField>()
	private var hex : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexLit : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexActiveP1 : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexActiveP0 : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var curPlayer = 0
    private var drawUI = false
    private var stageUI : Stage? = null

    init {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        polygon = GameWorld.batch
        field   = GameWorld.field
        val r = field.hexR.toFloat()
        hex  = HexPolygon(r, null).hexRegion
        hexLit = HexPolygon(r, "lit").hexRegion
        hexActiveP0 = HexPolygon(r, "p0").hexRegion
        hexActiveP1 = HexPolygon(r, "p1").hexRegion
    }

    public fun enableDrawingUI(UIStage : Stage) {
        drawUI = true
        stageUI = UIStage
    }

    public fun disableDrawingUI() {
        drawUI = false
        stageUI = null
    }

    fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        polygon.begin()
        for (i in 0.. field.width - 1) {
            for (j in 0..field.height - 1) {
                val curHex = field.field[i][j]
                var curTexture = hex
                if (curHex.activated) {
                    val actInd = field.findActorInd(curHex.i, curHex.j)
                            ?: throw Exception("An actor doesn't belong to any player")
                    if (field.actors[actInd].owner == 0) curTexture = hexActiveP0
                    else curTexture = hexActiveP1
                }
                polygon.draw(curTexture, curHex.xl.toFloat() + (-47f + 20 * Math.sqrt(3.0) / 2).toFloat(),
                        curHex.yl.toFloat() + (-50f + 20 * Math.sqrt(3.0) / 2).toFloat())
                if (curHex.lit) {
                    val c = polygon.color
                    polygon.setColor(c.r, c.g, c.b, .5f)
                    polygon.draw(hexLit, curHex.xl.toFloat() + (-47f + 20 * Math.sqrt(3.0) / 2).toFloat(),
                            curHex.yl.toFloat() + (-50f + 20 * Math.sqrt(3.0) / 2).toFloat())
                    polygon.setColor(c.r, c.g, c.b, 1f)
                    continue
                }
                if (curHex.occupied) {
                    val actInd = field.findActorInd(curHex.i, curHex.j)
                            ?: throw Exception("An actor doesn't belong to any player")
                    if (field.actors[actInd].owner == 0) curTexture = hexActiveP0
                    else curTexture = hexActiveP1

                    val c = polygon.color
                    polygon.setColor(c.r, c.g, c.b, .35f)
                    polygon.draw(curTexture, curHex.xl.toFloat() + (-47f + 20 * Math.sqrt(3.0) / 2).toFloat(),
                            curHex.yl.toFloat() + (-50f + 20 * Math.sqrt(3.0) / 2).toFloat())
                    polygon.setColor(c.r, c.g, c.b, 1f)
                }
            }
        }
        polygon.end()

        if (GameWorld.players[curPlayer].getInput()) {
            GameWorld.playerTurnLabels[curPlayer].isVisible = false
            curPlayer = 1 - curPlayer
            GameWorld.playerTurnLabels[curPlayer].isVisible = true
            GameWorld.players[curPlayer].grabInput()
        }

        GameWorld.update()
        GameWorld.stage.draw()
        GameWorld.stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);

        if (drawUI) {
            val uiStageVal = stageUI ?: throw Exception("Error in ui stage in GameRenderer")
            uiStageVal.draw()
            uiStageVal.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
        }
    }

    fun dispose() {
        //hex.dispose()
    }

}
