package com.mygdx.Helpers

import com.mygdx.game.GameWorld.GameWorld
import com.mygdx.game.Helpers.InputHandler

/**
 * Created by Alexander on 14.11.2015.
 */
public class SkillExecutor(private val executorPlayerInd: Int) {
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
            "Quick shot" -> quickShot.changeHexLight(i_executor, j_executor)
            "Stab" -> stab.changeHexLight(i_executor, j_executor)
            "Jump" -> jump.changeHexLight(i_executor, j_executor)
        }
    }

    public fun useSkill(skillData: InputHandler.SkillBeingUsedData): Boolean {
        //TODO: CORRECT THROWING EXCEPTION
        return useSkill(skillData.skillName, skillData.iFst, skillData.jFst,
                skillData.iSnd ?: throw Exception(), skillData.jSnd ?: throw Exception())
    }

    inner class Jump() {
        val radius = 2

        public fun jump(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
            val vicinity = GameWorld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                    ?: throw Exception("Error in QuickShot (wrong radius)")
            if (!vicinity.contains(Pair(i_target, j_target))) return false
            val moveTo = GameWorld.field.field[i_target][j_target]
            if (!moveTo.occupied) {
                val actor = GameWorld.field.findActorInd(i_executor, j_executor)
                        ?: throw Exception("No actor on hex on which skill Jump has been activated")
                GameWorld.field.moveActor(actor, moveTo)
                return true
            }
            return false
        }

        public fun changeHexLight(i_executor: Int, j_executor: Int) {
            val vicinity = GameWorld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                    ?: throw Exception("Error in QuickShot (wrong radius)")
            for (i in vicinity) {
                val cur = GameWorld.field.field[i.first][i.second]
                cur.lit = !cur.lit
            }
        }
    }

    inner class QuickShot() {
        val damage = 1
        val radius = 2

        public fun quickShot(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
            val vicinity = GameWorld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                    ?: throw Exception("Error in QuickShot (wrong radius)")
            if (!vicinity.contains(Pair(i_target, j_target))) return false
            if (GameWorld.field.field[i_target][j_target].occupied) {
                GameWorld.field.actors[GameWorld.field.findActorIndNotOwner(i_target, j_target, executorPlayerInd)
                        ?: return false].damageTaken(damage)
            }
            return true
        }

        public fun changeHexLight(i_executor: Int, j_executor: Int) {
            val vicinity = GameWorld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                    ?: throw Exception("Error in QuickShot (wrong radius)")
            for (i in vicinity) {
                val cur = GameWorld.field.field[i.first][i.second]
                cur.lit = !cur.lit
            }
        }
    }
    inner class Stab() {
        val damage = 1

        public fun stab(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
            val vicinity = GameWorld.field.hexesInVicinityRadius(i_executor, j_executor)
                    ?: throw Exception("Error in Stab (wrong radius)")
            if (!vicinity.contains(Pair(i_target, j_target))) return false
            //if (Math.abs(i_executor - i_target) > 1 || Math.abs(j_executor - j_target) > 1) return false
            if (GameWorld.field.field[i_target][j_target].occupied) {
                GameWorld.field.actors[GameWorld.field.findActorIndNotOwner(i_target, j_target, executorPlayerInd)
                        ?: return false].damageTaken(damage)
            }
            return true
        }

        public fun changeHexLight(i_executor: Int, j_executor: Int) {
            val vicinity = GameWorld.field.hexesInVicinityRadius(i_executor, j_executor)
                    ?: throw Exception("Error in Stab (wrong radius)")
            for (i in vicinity) {
                val cur = GameWorld.field.field[i.first][i.second]
                cur.lit = !cur.lit
            }
        }
    }

}