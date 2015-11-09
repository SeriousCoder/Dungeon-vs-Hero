package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.mygdx.game.Helpers.InputHandler
import kotlin.properties.Delegates

public class Player(val playerInd : Int, inputHandler : InputHandler?) {
    //if inputHandler == null, it's an AI player.
    //NO actionPoints!!! They belong to actors!
    var inHandler : InputHandler by Delegates.notNull<InputHandler>()
    private var isAI = false
    init {
        if (inputHandler == null) isAI = true
        else inHandler = inputHandler
    }

    public fun getInput() : Boolean {
        Gdx.input.inputProcessor = inHandler
        if (inHandler.dataHasBeenGot) {
            inHandler.dataHasBeenGot = false
            return true
        }
        return false
    }
}