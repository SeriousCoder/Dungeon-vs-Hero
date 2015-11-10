package com.mygdx.game.Helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import kotlin.properties.Delegates

class InputHandler (private val field : HexField, val height : Int, val virtualWidth : Float, val playerInd: Int) : InputProcessor {
    var dataHasBeenGot = false
    private var stageUI: Stage by Delegates.notNull<Stage>()

    init {
        //TESTING UI BEGIN
        val skin = Skin(Gdx.files.internal("uiskin.json"));
        stageUI = Stage()
        val button = TextButton("Click me", skin, "default")

        button.setWidth(200f);
        button.setHeight(20f);
        button.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 10f);

        button.addListener(object : ClickListener() {
            override fun clicked(event : InputEvent, x : Float, y : Float) {
                dataHasBeenGot = true
            }
        });
        stageUI.addActor(button);
        //TESTING UI END
    }
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) : Boolean {
        val resolutionMultiplier = Gdx.graphics.width / virtualWidth
        val virtualX = screenX / resolutionMultiplier
        val virtualY = height - screenY / resolutionMultiplier
        val hexInd = field.findHex(virtualX.toInt(), virtualY.toInt()) ?: return true

        val iInd = hexInd.first
        val jInd = hexInd.second

        var actInd = field.findActorInd(iInd, jInd, playerInd)
        if (actInd != null) {
            for (i in 0..field.actors.size - 1) if (i != actInd) field.actors[i].deactivate()
            field.actors[actInd].changeActivation()
        }
        else {
            actInd = field.activatedActorInVicinityInd(iInd, jInd, playerInd)// ?: return true
            if (actInd == null) {
                Gdx.input.inputProcessor = stageUI
            }
            else {
                field.moveActor(actInd, field.field[iInd][jInd])
                dataHasBeenGot = true
            }
        }
        return true // Return true to say we handled the touch.
    }
    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

}
