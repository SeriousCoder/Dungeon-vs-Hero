package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.mygdx.game.Helpers.HexField
import com.mygdx.game.Helpers.InputHandler
import com.mygdx.GameObjects.ActorHex
import kotlin.properties.Delegates

public class Player(val playerInd : Int, field : HexField, virtualHeight : Float,
                    virtualWidth : Float, private var isAI : Boolean= false) {
    //if inputHandler == null, it's an AI player.
    //NO actionPoints!!! They belong to actors!
    var inHandler : InputHandler by Delegates.notNull<InputHandler>()
    var actorIndices = arrayListOf<Int>()
    init {
        if (!isAI) inHandler = InputHandler(field, virtualHeight, virtualWidth, this)
    }

    public fun fieldActorIndToPlayerActorInd(ind : Int) : Int?{
        return actorIndices.find { i -> actorIndices[i] == ind}
    }

    public fun grabInput() {
        Gdx.input.inputProcessor = inHandler
    }

    public fun addActorInd(actorInd : Int) {
        actorIndices.add(actorInd)
        inHandler.watchForNewActors()
    }

    public fun getInput() : Boolean {
        if (inHandler.dataHasBeenGot) {
            inHandler.dataHasBeenGot = false
            return true
        }
        return false
    }
}