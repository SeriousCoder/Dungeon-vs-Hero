package com.mygdx.game.actors

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable

public class Archer(var actorX : Float, var actorY : Float) : Actor() {
    internal var texture = Texture(Gdx.files.internal("archer.png"))
  //  internal var actorX = 100f
  //  internal var actorY = 100f
    internal var sizeX = 40f
    internal var sizeY = 40f
    var started = false

    init {
        setBounds(actorX, actorY, texture.getWidth().toFloat(), texture.getHeight().toFloat())
       // setSize(40f, 40f)
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                (event.getTarget() as Archer).started = true
                return true
            }
        })
    }


    override fun draw(batch: Batch, alpha: Float) {

        batch.draw(texture, actorX, actorY, 40f, 40f)
    }

    override fun act(delta: Float) {
        if (started) {
            actorX += 5f
            setBounds(actorX, actorY, texture.getWidth().toFloat(), texture.getHeight().toFloat())
            started = false
        }
    }
}