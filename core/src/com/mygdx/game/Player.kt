package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.mygdx.Helpers.HexField
import com.mygdx.Helpers.InputHandler
import com.mygdx.GameObjects.ActorHex
import com.mygdx.Helpers.SkillExecutor
import com.mygdx.GameWorld.GameRenderer
import com.mygdx.GameWorld.GameWorld
import java.util.*
import kotlin.properties.Delegates

public class Player(private val gameWorld: GameWorld, gameRenderer: GameRenderer,
                    val playerInd : Int, val skillExec: SkillExecutor,
                    virtualHeight : Float, virtualWidth : Float,
                    private var isAI : Boolean = false) {
    //if inputHandler == null, it's an AI player.
    //NO actionPoints!!! They belong to actors!
    var inHandler : InputHandler by Delegates.notNull<InputHandler>()
    var actorIndices = arrayListOf<Int>()
    var actorsNum = 0
    init {
        if (!isAI)
            inHandler = InputHandler(gameWorld, gameRenderer, skillExec, virtualHeight, virtualWidth, this)
    }

    public fun fieldActorIndToPlayerActorInd(ind : Int) : Int {
        return actorIndices.binarySearch(ind, 0, actorsNum)
    }

    public fun grabInput() {
        if (!isAI) Gdx.input.inputProcessor = inHandler
        //TODO: that's where I stopped
    }

    public fun addActorInd(actorInd : Int) {
        actorIndices.add(actorInd)
        if (!isAI) inHandler.addNewActorSkills()
        actorsNum++
    }

    public fun delActorInd(actorInd : Int) {
        if (!isAI) inHandler.actorsSkillsBtns.removeAt(fieldActorIndToPlayerActorInd(actorInd))
        actorIndices.remove(actorInd)
    //    inHandler.removeActorSkills()
        actorsNum--
        correctActorFieldIndicesAfterDeletion(actorInd)
    }

    public fun correctActorFieldIndicesAfterDeletion(actorInd: Int) {
        for (i in actorIndices.indices) {
            if (actorIndices[i] > actorInd) actorIndices[i]--
        }
    }

    fun findNearestEnemy(i : Int, j : Int, enemyIndices : MutableList<Int>) : Int {
        var minDist = Math.sqrt(Math.pow(gameWorld.field.actors[enemyIndices[0]].hex.i - i.toDouble(), 2.0)
                          + Math.pow(gameWorld.field.actors[enemyIndices[0]].hex.j -j.toDouble(), 2.0))
        var minInd = 0
        for (k in enemyIndices) {
            val curActorHex =  gameWorld.field.actors[enemyIndices[k]].hex
            val curDist = Math.sqrt(Math.pow(curActorHex.i - i.toDouble(), 2.0) + Math.pow(curActorHex.j - j.toDouble(), 2.0))
            if (curDist < minDist) {
                minDist = curDist
                minInd = k
            }
        }
        return minInd
    }

    public fun makeTurnAI(enemy : Player) : Boolean {
        for (actorInd in actorIndices) {
            val actor = gameWorld.field.actors[actorInd]
            val i = actor.hex.i
            val j = actor.hex.j
            val vicinityHexes: MutableList<Pair<Int, Int>> = gameWorld.field.hexesInVicinityRadius(i, j)!!
            for (k in vicinityHexes) {
                val enemyActInd = gameWorld.field.findActorInd(k.first, k.second, enemy.playerInd)
                if (enemyActInd != null)  {
                    val enemyActor = gameWorld.field.actors[enemyActInd]
                    if (tryToUseSkill(actor, enemyActor.hex.i, enemyActor.hex.j, "Stab")) continue
                }
            }
            val nearestEnemy = gameWorld.field.actors[findNearestEnemy(i, j, enemy.actorIndices)]
            var idiff = nearestEnemy.hex.i - i
            var jdiff = nearestEnemy.hex.j - j
            //get direction vector
            if (idiff != 0) idiff = idiff / Math.abs(idiff)
            if (jdiff != 0) jdiff = jdiff / Math.abs(jdiff)
            if (!vicinityHexes.contains(Pair(i + idiff, j + jdiff)) || !moveActor(actorInd, i + idiff, j + jdiff)) {
                if (!vicinityHexes.contains(Pair(i, j + jdiff)) || !moveActor(actorInd, i, j + jdiff)) {
                    //due to hexagonal field either j+jdiff or i+idiff may be out of vicinity, but not both.
                    moveActor(actorInd, i + idiff, j)
                }
            }
        }
        return true
    }

    public fun tryToUseSkill(source : ActorHex, i : Int, j : Int, skillName: String) : Boolean {
        var res = false
        if (skillExec.useSkill(skillName, source.hex.i, source.hex.j, i, j)) {
            gameWorld.field.deadActorsExist = true
            source.skillHaveBeenUsed(skillName)
            if (checkIfNoAP()) {
                res = true
                restoreActionPoints()
            }
        }
        return res
    }

    private fun checkIfNoAP() : Boolean{
        var sum = 0
        for (i in actorIndices)
            sum += gameWorld.field.actors[i].curActionPoints
        return sum == 0
    }

    public fun getInput() : Boolean {
        if (isAI)
            return makeTurnAI(gameWorld.players[1 - playerInd])
        if (inHandler.dataHasBeenGot) {
            inHandler.dataHasBeenGot = false
            return true
        }
        return false
    }

    public fun restoreActionPoints() {
        for (i in actorIndices) {
            val actor = gameWorld.field.actors[i]
            actor.curActionPoints = actor.maxActionPoints
        }
    }

    public fun moveActor(actInd : Int, iInd : Int, jInd : Int) : Boolean {
        val moveCost = 1
        val movingActor = gameWorld.field.actors[actInd]
        if (movingActor.curActionPoints >= moveCost) {
            if (iInd < 0 || jInd < 0 || iInd >= gameWorld.field.width || jInd >= gameWorld.field.height
                         ||!gameWorld.field.moveActor(actInd, gameWorld.field.field[iInd][jInd])) return false
            movingActor.curActionPoints--
            if (checkIfNoAP()) {
                restoreActionPoints()
                return true
            }
        }
        return false
    }
}