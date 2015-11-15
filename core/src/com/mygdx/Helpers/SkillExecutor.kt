package com.mygdx.Helpers

import com.mygdx.game.GameWorld.GameWorld
import com.mygdx.game.Helpers.InputHandler

/**
 * Created by Alexander on 14.11.2015.
 */
public class SkillExecutor(private val gameworld : GameWorld, private val executorPlayerInd: Int) {
    public fun useSkill(skillName : String, X_executor : Int, Y_executor : Int, X_target : Int, Y_target : Int) : Boolean {
        when (skillName) {
            "Quick shot" -> return QuickShot(X_executor, Y_executor, X_target, Y_target)
            "Stab" -> return Stab(X_executor, Y_executor, X_target, Y_target)
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
    public fun Stab( i_executor : Int, j_executor : Int, i_target : Int, j_target : Int) : Boolean{
        val stabDamage = 1
        if (Math.abs(i_executor - i_target) > 1 || Math.abs(j_executor - j_target) > 1) return false
        if (gameworld.field.field[i_target][j_target].occupied) {
            gameworld.field.actors[gameworld.field.findActorIndNotOwner(i_target,j_target, executorPlayerInd)
                    ?: return false].damageTaken(stabDamage)
        }
        return true
    }
}