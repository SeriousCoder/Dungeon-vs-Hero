package com.mygdx.game.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.mygdx.game.actors.Archer

class InputHandler (private val archer : Archer) : InputProcessor {

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        archer.act(Gdx.graphics.getDeltaTime(), screenX, screenY)
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
