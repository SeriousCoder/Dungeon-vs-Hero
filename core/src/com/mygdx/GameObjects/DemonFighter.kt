package com.mygdx.GameObjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.Helpers.HexForLogic

/**
 * Created by Alexander on 11.11.2015.
 */
class DemonFighter (hex : HexForLogic, owner : Int) : ActorHex(hex, owner) {
    internal var texture = Texture(Gdx.files.internal("Data/Images/demonFighter.png"))

    internal var sizeX = 40f
    internal var sizeY = 40f

    public var waiting = false
    init {
        skills = arrayListOf(Pair("Stab", 1), Pair("Jump", 1))
        skillPics.put("Stab", Pair("Stab_demon", "Stab_demon_pressed"))
        skillPics.put("Jump", Pair("Jump_demon", "Jump_demon_pressed"))
        setBounds(actorX, actorY, texture.width.toFloat(), texture.height.toFloat())
    }


    override fun draw(batch: Batch, alpha: Float) {
        batch.draw(texture, actorX, actorY, sizeX - 10, sizeY - 10)
    }

    override fun act(delta: Float) {
        activated = !activated
    }
}