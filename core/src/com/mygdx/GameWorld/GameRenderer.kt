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
import java.util.*
import kotlin.properties.Delegates

class GameRenderer(val gameWorld: GameWorld) {

	private var polygon : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
	private var field : HexField by Delegates.notNull<HexField>()
	private var hex : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexRock : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexLit : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexActiveP1 : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexActiveP0 : PolygonRegion by Delegates.notNull<PolygonRegion>()
    private var hexArray : Array<Array<PolygonRegion>>
    private var curPlayer = 0
    private var drawUI = false
    private var stageUI : Stage? = null

    init {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        polygon = gameWorld.batch
        field   = gameWorld.field
        val r = field.hexR.toFloat()
        hex  = HexPolygon(r, null).hexRegion
        hexRock = HexPolygon(r, "rock").hexRegion
        hexLit = HexPolygon(r, "lit").hexRegion
        hexActiveP0 = HexPolygon(r, "p0").hexRegion
        hexActiveP1 = HexPolygon(r, "p1").hexRegion

        hexArray = generateField()
    }

    private fun generateField() : Array<Array<PolygonRegion>>
    {
        var newField : Array<Array<PolygonRegion>> = Array(9, {i -> Array(11, {hex})})

        val rand = Random(System.currentTimeMillis())
        for (i in 0..field.width - 1)
            for (j in 0..field.height - 1)
        {
            if (Math.abs(rand.nextInt()) % 100 < 20)
            {
                newField[i][j] = hexRock
                field.field[i][j].occupied = true
            }
        }
        return newField
    }

    public fun enableDrawingUI(UIStage : Stage) {
        drawUI = true
        stageUI = UIStage
    }

    public fun disableDrawingUI() {
        drawUI = false
        stageUI = null
    }

    fun render(shouldUpdateGameWorld :Boolean = true) : Int{
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        polygon.begin()
        for (i in 0.. field.width - 1) {
            for (j in 0..field.height - 1) {
                val curHex = field.field[i][j]
                var curTexture = hexArray[i][j]
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
                if (curHex.occupied && curTexture == hex) {
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

        if (gameWorld.players[curPlayer].getInput()) {
            gameWorld.playerTurnLabels[curPlayer].isVisible = false
            curPlayer = 1 - curPlayer
            gameWorld.playerTurnLabels[curPlayer].isVisible = true
            gameWorld.players[curPlayer].grabInput()
        }

        var winner : Int? = null
        if (shouldUpdateGameWorld) winner = gameWorld.update()
        gameWorld.stage.draw()
        gameWorld.stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);

        if (drawUI) {
            val uiStageVal = stageUI ?: throw Exception("Error in ui stage in GameRenderer")
            uiStageVal.draw()
            uiStageVal.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
        }
        return (winner ?: -1)
    }

    fun dispose() {
        polygon.dispose()
        field.dispose()
        stageUI?.dispose()
    }

}
