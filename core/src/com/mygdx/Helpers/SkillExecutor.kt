package com.mygdx.Helpers

import com.mygdx.game.GameWorld.GameWorld
import com.mygdx.game.Helpers.InputHandler

/**
 * Created by Alexander on 14.11.2015.
 */
public class SkillExecutor(private val Gameworld : GameWorld, private val executorPlayerInd: Int) {
    public fun useSkill(skillName : String, X_executor : Int, Y_executor : Int, X_target : Int, Y_target : Int) : Boolean {
        when (skillName) {
            "Quick shot" -> return QuickShot(X_executor, Y_executor, X_target, Y_target)
        }
        return false
    }
    public  fun useSkill (skillData : InputHandler.SkillBeingUsedData) : Boolean{
        //TODO: CORRECT THROWING EXCEPTION
        return useSkill(skillData.skillName, skillData.iFst, skillData.jFst,
                skillData.iSnd ?: throw Exception(), skillData.jSnd?: throw Exception())
    }
    public fun QuickShot( X_executor : Int, Y_executor : Int, X_target : Int, Y_target : Int) : Boolean{
        return true
    }
}