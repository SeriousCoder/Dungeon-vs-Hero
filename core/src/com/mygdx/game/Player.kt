package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.mygdx.game.Helpers.HexField
import com.mygdx.game.Helpers.InputHandler
import com.mygdx.GameObjects.ActorHex
import com.mygdx.Helpers.SkillExecutor
import com.mygdx.game.GameWorld.GameRenderer
import com.mygdx.game.GameWorld.GameWorld
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

    public fun makeTurnAI(enemy : Player) : Boolean {
        for (enemyActorInd in enemy.actorIndices) {
            val enemyActor = gameWorld.field.actors[enemyActorInd]
            val enemyX = enemyActor.hex.i
            val enemyY = enemyActor.hex.j

            fun distance (pair : Pair<Int, Int>) : Double = Math.sqrt(((enemyX - pair.first) * (enemyX - pair.first) +
                    (enemyY - pair.second) * (enemyY - pair.second).toDouble()))

            class compClass : Comparator<Pair<Int, Int>>
            {
                override fun compare(o1: Pair<Int, Int>, o2: Pair<Int, Int>): Int {
                    return distance(o1).compareTo(distance(o2))
                }
            }

            for (actorInd in actorIndices) {
                val actor = gameWorld.field.actors[actorInd]
                val x = actor.hex.i
                val y = actor.hex.j

                val diffX = Math.abs(enemyX - x)
                val diffY = Math.abs(enemyY - y)

                if ((diffX == 0 && diffY == 1) || (diffX == 1 && diffY == 0) ||
                        (diffX == 1 && diffY == 1 && x % 2 == 0 && (enemyY > y)) ||
                        (diffX == 1 && diffY == 1 && x % 2 == 1 && (enemyY < y))) {
                    tryToUseSkill(actor, enemyX, enemyY, "Stab");
                    continue
                }

                var listMoves : ArrayList<Pair<Int, Int>> = arrayListOf()

                if (x != 0) listMoves.add(Pair(x - 1, y))
                if (x != 8) listMoves.add(Pair(x + 1, y))
                if (y != 0) listMoves.add(Pair(x, y - 1))
                if (y != 10) listMoves.add(Pair(x, y + 1))

                if (x % 2 == 0 && y != 0)
                {
                    if (x != 0) listMoves.add(Pair(x - 1, y - 1))
                    if (x != 8) listMoves.add(Pair(x + 1, y - 1))
                }
                else if (y != 10)
                {
                    listMoves.add(Pair(x - 1, y + 1))
                    listMoves.add(Pair(x + 1, y + 1))
                }

                val comp : compClass = compClass()
                listMoves.sort(comp)

                var ind = 0

                while (ind < listMoves.count())
                {
                    if (moveActor(actorInd, listMoves[ind].first, listMoves[ind].second)) break
                    ind++
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
            if (!gameWorld.field.moveActor(actInd, gameWorld.field.field[iInd][jInd])) return false
            movingActor.curActionPoints--
            if (checkIfNoAP()) {
                restoreActionPoints()
                return true
            }
        }
        return false
    }
}