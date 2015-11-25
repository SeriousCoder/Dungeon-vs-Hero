package com.mygdx.GameObjects

import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.Helpers.HexForLogic
import java.util.*

public abstract class ActorHex(var hex : HexForLogic, val owner : Int) : Actor() {
    var activated = false
    var actorX = hex.xl + 5
    var actorY = hex.yl + 5
    open val maxActionPoints = 2
    open var curActionPoints = maxActionPoints
    val maxHealthPoints = 1
    var curHealthPoints = maxHealthPoints

    public var skills = arrayListOf<Pair<String, Int>>()//names of skills and costs of them
    public var skillPics = HashMap<String, Pair<String, String>>()//first string is skill name,
        // second - names of areas from SkillButtons.pack for pressed and unpressed states.

    fun skillHaveBeenUsed(skillName : String) : Boolean {
        for (sk in skills) {
            if (sk.first == skillName) {
                curActionPoints -= sk.second
                return true
            }
        }
        return false
    }

    fun damageTaken(damage : Int) {
        curHealthPoints -= damage
        if (curHealthPoints <= 0) hex.isActorHereDead = true
    }

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