package com.mygdx.GameObjects

import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.Helpers.HexForLogic
import java.util.*

public abstract class ActorHex(var hex : HexForLogic, val owner : Int) : Actor() {
    var activated = false
    var actorX = hex.xl + 5
    var actorY = hex.yl + 5
    val actionPointsMax = 2

    //first string is skill name, second - path to the picture.
    public var skills = arrayListOf<String>()//names of skills
    public var skillPics = HashMap<String, Pair<String, String>>()//first string is skill name,
        // second - names of areas from SkillButtons.pack for pressed and unpressed states.

    fun changeActivation () : Boolean{
        if (activated) {
            deactivate()
            return false
        }
        activate()
        return true
    }

    fun activate() {
        activated = true
        hex.activate()
    }

    fun deactivate() {
        activated = false
        hex.deactivate()
    }
}