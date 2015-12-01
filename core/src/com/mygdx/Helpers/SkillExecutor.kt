package com.mygdx.Helpers

import com.mygdx.GameWorld.GameWorld
import com.mygdx.Helpers.InputHandler

/**
 * Created by Alexander on 14.11.2015.
 */
public class SkillExecutor(private val gameWorld: GameWorld, private val executorPlayerInd: Int) {
    val quickShotRadius = 2
    val quickShotDamage = 1
    val stabDamage = 1
    val jumpRadius = 2

    public fun useSkill(skillName: String, i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
        when (skillName) {
            "Quick shot" -> return quickShot(i_executor, j_executor, i_target, j_target)
            "Stab" -> return stab(i_executor, j_executor, i_target, j_target)
            "Jump" -> return jump(i_executor, j_executor, i_target, j_target)
        }
        return false
    }

    public fun changeHexLightForSkill(skillName: String, i_executor: Int, j_executor: Int) {
        when (skillName) {
            "Quick shot" -> changeHexLight(i_executor, j_executor, quickShotRadius)
            "Stab" -> changeHexLight(i_executor, j_executor)
            "Jump" -> changeHexLight(i_executor, j_executor, jumpRadius)
        }
    }

    public fun useSkill(skillData: InputHandler.SkillBeingUsedData): Boolean {
        //TODO: CORRECT THROWING EXCEPTION
        return useSkill(skillData.skillName, skillData.iFst, skillData.jFst,
                skillData.iSnd ?: throw Exception(), skillData.jSnd ?: throw Exception())
    }

    public fun changeHexLight(i_executor: Int, j_executor: Int, radius : Int = 1) {
        val vicinity = gameWorld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                ?: throw Exception("Error in hex lighting (wrong radius)")
        vicinity.remove(Pair(i_executor, j_executor))
        for (i in vicinity) {
            val cur = gameWorld.field.field[i.first][i.second]
            cur.lit = !cur.lit
        }
    }

    public fun jump(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int, radius : Int = jumpRadius): Boolean {
        val vicinity = gameWorld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                ?: throw Exception("Error in QuickShot (wrong radius)")
        if (!vicinity.contains(Pair(i_target, j_target))) return false
        val moveTo = gameWorld.field.field[i_target][j_target]
        if (!moveTo.occupied) {
            val actor = gameWorld.field.findActorInd(i_executor, j_executor)
                    ?: throw Exception("No actor on hex on which skill Jump has been activated")
            gameWorld.field.moveActor(actor, moveTo)
            return true
        }
        return false
    }

    public fun quickShot(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int,
                         radius : Int = quickShotRadius, damage : Int = quickShotDamage): Boolean {
        val vicinity = gameWorld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                ?: throw Exception("Error in QuickShot (wrong radius)")
        if (!vicinity.contains(Pair(i_target, j_target))) return false
        if (gameWorld.field.field[i_target][j_target].occupied) {
            gameWorld.field.actors[gameWorld.field.findActorIndNotOwner(i_target, j_target, executorPlayerInd)
                    ?: return false].damageTaken(damage)
            return true
        }
        return false
    }

    public fun stab(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int,
                    damage: Int = stabDamage): Boolean {
        val vicinity = gameWorld.field.hexesInVicinityRadius(i_executor, j_executor)
                ?: throw Exception("Error in Stab (wrong radius)")
        if (!vicinity.contains(Pair(i_target, j_target))) return false
        //if (Math.abs(i_executor - i_target) > 1 || Math.abs(j_executor - j_target) > 1) return false
        if (gameWorld.field.field[i_target][j_target].occupied) {
            gameWorld.field.actors[gameWorld.field.findActorIndNotOwner(i_target, j_target, executorPlayerInd)
                    ?: return false].damageTaken(damage)
            return true
        }
        return false
    }
}