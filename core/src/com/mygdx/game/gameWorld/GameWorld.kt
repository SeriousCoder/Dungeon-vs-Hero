package com.mygdx.game.gameWorld

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.game.actors.Archer
import com.mygdx.game.helpers.InputHandler
import kotlin.properties.Delegates

class GameWorld(public val batch : PolygonSpriteBatch) {

    val virtualWidth  = 360f
    val virtualHeight = 640f

    private var stage: Stage by Delegates.notNull<Stage>()

    init {
        stage = Stage(FitViewport(virtualWidth, virtualHeight), batch)
        stage.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        val myActor = Archer(100f, 300f)
        myActor.touchable = Touchable.enabled
        Gdx.input.inputProcessor = InputHandler(myActor)
        stage.addActor(myActor)
    }

    fun update() {
        stage.draw()
        stage.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    fun dispose() {
        stage.dispose()
    }
}
