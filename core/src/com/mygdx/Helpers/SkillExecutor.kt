package com.mygdx.Helpers

import com.mygdx.game.GameWorld.GameWorld
import com.mygdx.game.Helpers.InputHandler

/**
 * Created by Alexander on 14.11.2015.
 */
public class SkillExecutor(private val gameWorld: GameWorld, private val executorPlayerInd: Int) {
    private val quickShot = QuickShot()
    private val stab = Stab()
    private val jump = Jump()

    public fun useSkill(skillName: String, i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
        when (skillName) {
            "Quick shot" -> return quickShot.quickShot(i_executor, j_executor, i_target, j_target)
            "Stab" -> return stab.stab(i_executor, j_executor, i_target, j_target)
            "Jump" -> return jump.jump(i_executor, j_executor, i_target, j_target)
        }
        return false
    }

    public fun changeHexLightForSkill(skillName: String, i_executor: Int, j_executor: Int) {
        when (skillName) {
            "Quick shot" -> quickShot.changeHexLight(i_executor, j_executor, quickShot.radius)
            "Stab" -> stab.changeHexLight(i_executor, j_executor)
            "Jump" -> jump.changeHexLight(i_executor, j_executor, jump.radius)
        }
    }

    public fun useSkill(skillData: InputHandler.SkillBeingUsedData): Boolean {
        //TODO: CORRECT THROWING EXCEPTION
        return useSkill(skillData.skillName, skillData.iFst, skillData.jFst,
                skillData.iSnd ?: throw Exception(), skillData.jSnd ?: throw Exception())
    }

    open inner class Skill() {
        public fun changeHexLight(i_executor: Int, j_executor: Int, radius : Int = 1) {
            val vicinity = gameWorld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                    ?: throw Exception("Error in hex lighting (wrong radius)")
            vicinity.remove(Pair(i_executor, j_executor))
            for (i in vicinity) {
                val cur = gameWorld.field.field[i.first][i.second]
                cur.lit = !cur.lit
            }
        }
    }

    inner class Jump() : Skill() {
        val radius = 2

        public fun jump(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
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

    }

    inner class QuickShot() : Skill(){
        val damage = 1
        val radius = 2

        public fun quickShot(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
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
    }
    inner class Stab() : Skill() {
        val damage = 1

        public fun stab(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
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

}