package com.mygdx.game.gameObjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.GameObjects.ActorHex
import com.mygdx.game.Helpers.HexForLogic
import java.util.*

public class Archer(hex : HexForLogic, owner : Int) : ActorHex(hex, owner) {
    internal var texture = Texture(Gdx.files.internal("Data/Images/archer.png"))

    internal var sizeX = 40f
    internal var sizeY = 40f

    public var waiting = false
    init {
        skills = arrayListOf(Pair("Quick shot", 1), Pair("Jump", 1))
        skillPics.put("Quick shot", Pair("Quick_shot", "Quick_shot_pressed"))
        skillPics.put("Jump", Pair("Jump_archer", "Jump_archer_pressed"))
        setBounds(actorX, actorY, texture.width.toFloat(), texture.height.toFloat())
//        addListener(object : InputListener() {
//            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
//                (event.getTarget() as Archer).started = true
//                return true
//            }
//        })
    }


    override fun draw(batch: Batch, alpha: Float) {
        batch.draw(texture, actorX, actorY, sizeX - 15, sizeY -15)
    }

//    override fun act(delta: Float) {
//        if (started) {
//            //while (!Gdx.input.isTouched) {}
//            //            val xtouch = Gdx.input.x
//            //            val ytouch = Gdx.input.y
//            //            if (xtouch > 100 && ytouch > 100) actorX += 10f
//            //            if (xtouch < 100 && ytouch < 100) actorX -= 10f
//            waiting = true
//            setBounds(actorX, actorY, texture.getWidth().toFloat(), texture.getHeight().toFloat())
//            started = false
//        }
//    }

    override fun act(delta: Float) {
        activated = !activated
    }
}