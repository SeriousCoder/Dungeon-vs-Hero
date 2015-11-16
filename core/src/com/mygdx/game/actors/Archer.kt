package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.hex.HexForLogic

public abstract class ActorHex(var hex : HexForLogic) : Actor() {
    var activated = false
    var actorX = hex.xl
    var actorY = hex.yl
    abstract fun changeActivation()
    abstract fun deactivate()
}

public class Archer(hex : HexForLogic) : ActorHex(hex) {
    internal var texture = Texture(Gdx.files.internal("archer.png"))

    internal var sizeX = 40f
    internal var sizeY = 40f

//    init {
//        setBounds(actorX, actorY, texture.width.toFloat(), texture.height.toFloat())
//        addListener(object : InputListener() {
//            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
//                (event.getTarget() as Archer).started = true
//                return true
//            }
//        })
//    }


    override fun draw(batch: Batch, alpha: Float) {
        batch.draw(texture, actorX, actorY, sizeX, sizeY)
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

    override fun changeActivation() {
        activated = !activated
        hex.changeActivation()
    }

    override fun deactivate() {
        activated = false
        hex.deactivate()
    }
}