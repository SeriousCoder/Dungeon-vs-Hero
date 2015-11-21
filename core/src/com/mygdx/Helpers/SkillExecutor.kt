package com.mygdx.Helpers

import com.mygdx.game.GameWorld.GameWorld
import com.mygdx.game.Helpers.InputHandler

/**
 * Created by Alexander on 14.11.2015.
 */
public class SkillExecutor(private val gameworld : GameWorld, private val executorPlayerInd: Int) {
    public fun useSkill(skillName: String, i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
        when (skillName) {
            "Quick shot" -> return QuickShot().quickShot(i_executor, j_executor, i_target, j_target)
            "Stab" -> return Stab().stab(i_executor, j_executor, i_target, j_target)
        }
        return false
    }

    public fun changeHexLightForSkill(skillName: String, i_executor: Int, j_executor: Int) {
        when (skillName) {
            "Quick shot" ->QuickShot().changeHexLight(i_executor, j_executor)
            "Stab" -> Stab().changeHexLight(i_executor, j_executor)
        }
    }

    public fun useSkill(skillData: InputHandler.SkillBeingUsedData): Boolean {
        //TODO: CORRECT THROWING EXCEPTION
        return useSkill(skillData.skillName, skillData.iFst, skillData.jFst,
                skillData.iSnd ?: throw Exception(), skillData.jSnd ?: throw Exception())
    }

    inner class QuickShot() {
        val damage = 1
        val radius = 2

        public fun quickShot(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
            val vicinity = gameworld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                    ?: throw Exception("Error in QuickShot (wrong radius)")
            if (!vicinity.contains(Pair(i_target, j_target))) return false
            if (gameworld.field.field[i_target][j_target].occupied) {
                gameworld.field.actors[gameworld.field.findActorIndNotOwner(i_target, j_target, executorPlayerInd)
                        ?: return false].damageTaken(damage)
            }
            return true
        }

        public fun changeHexLight(i_executor: Int, j_executor: Int) {
            val vicinity = gameworld.field.hexesInVicinityRadius(i_executor, j_executor, radius)
                    ?: throw Exception("Error in QuickShot (wrong radius)")
            for (i in vicinity) {
                val cur = gameworld.field.field[i.first][i.second]
                cur.lit = !cur.lit
            }
        }
    }
    inner class Stab() {
        val damage = 1

        public fun stab(i_executor: Int, j_executor: Int, i_target: Int, j_target: Int): Boolean {
            val vicinity = gameworld.field.hexesInVicinityRadius(i_executor, j_executor)
                    ?: throw Exception("Error in Stab (wrong radius)")
            if (!vicinity.contains(Pair(i_target, j_target))) return false
            //if (Math.abs(i_executor - i_target) > 1 || Math.abs(j_executor - j_target) > 1) return false
            if (gameworld.field.field[i_target][j_target].occupied) {
                gameworld.field.actors[gameworld.field.findActorIndNotOwner(i_target, j_target, executorPlayerInd)
                        ?: return false].damageTaken(damage)
            }
            return true
        }

        public fun changeHexLight(i_executor: Int, j_executor: Int) {
            val vicinity = gameworld.field.hexesInVicinityRadius(i_executor, j_executor)
                    ?: throw Exception("Error in Stab (wrong radius)")
            for (i in vicinity) {
                val cur = gameworld.field.field[i.first][i.second]
                cur.lit = !cur.lit
            }
        }
    }

}