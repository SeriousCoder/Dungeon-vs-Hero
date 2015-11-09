package com.mygdx.game.Helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor

class InputHandler (private val field : HexField, val height : Int, val resolutionMultiplier : Float) : InputProcessor {
    val curPlayerInd = 0

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) : Boolean {
        val virtualX = screenX / resolutionMultiplier
        val virtualY = height - screenY / resolutionMultiplier
        val hexInd = field.findHex(virtualX.toInt(), virtualY.toInt()) ?: return true

        val iInd = hexInd.first
        val jInd = hexInd.second

        var actInd = field.findActorInd(iInd, jInd, curPlayerInd)
        if (actInd != null) {
            for (i in 0..field.actors.size - 1) if (i != actInd) field.actors[i].deactivate()
            field.actors[actInd].changeActivation()
        }
        else {
            actInd = field.activatedActorInVicinityInd(iInd, jInd, curPlayerInd) ?: return true
            field.moveActor(actInd, field.field[iInd][jInd])
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
