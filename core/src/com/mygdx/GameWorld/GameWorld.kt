package com.mygdx.game.GameWorld

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.game.gameObjects.Archer
import com.mygdx.game.helpers.InputHandler
import com.mygdx.game.helpers.HexField
import kotlin.properties.Delegates

class GameWorld(public val batch : PolygonSpriteBatch, public val field : HexField) {

    val virtualWidth  = 360f
    val virtualHeight = 640f
    //because of line below, it'll work correctly only on 16:9 resolutions.
    val resolutionMultiplier = Gdx.graphics.width / virtualWidth
    private var stage: Stage by Delegates.notNull<Stage>()

    init {
        stage = Stage(FitViewport(virtualWidth, virtualHeight), batch)
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);


        val myActor = Archer(field.field[5][5])
        myActor.touchable = Touchable.enabled

        val myActor2 = Archer(field.field[0][0])
        myActor.touchable = Touchable.enabled

        Gdx.input.inputProcessor = InputHandler(field, virtualHeight.toInt(), resolutionMultiplier)

        field.actors.add(myActor)
        myActor.hex.occupied = true
        stage.addActor(field.actors[0])

        field.actors.add(myActor2)
        myActor2.hex.occupied = true
        stage.addActor(field.actors[1])
    }

    fun update() {
        stage.draw()
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
    }

    fun dispose() {
        stage.dispose()
    }
}
