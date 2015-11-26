package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.mygdx.game.Helpers.HexField
import com.mygdx.game.Helpers.InputHandler
import com.mygdx.GameObjects.ActorHex
import com.mygdx.Helpers.SkillExecutor
import com.mygdx.game.GameWorld.GameRenderer
import com.mygdx.game.GameWorld.GameWorld
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
        inHandler = InputHandler(gameWorld, gameRenderer, skillExec, virtualHeight, virtualWidth, this)
    }

    public fun fieldActorIndToPlayerActorInd(ind : Int) : Int {
        return actorIndices.binarySearch(ind, 0, actorsNum)
    }

    public fun grabInput() {
        Gdx.input.inputProcessor = inHandler
    }

    public fun addActorInd(actorInd : Int) {
        actorIndices.add(actorInd)
        inHandler.addNewActorSkills()
        actorsNum++
    }

    public fun delActorInd(actorInd : Int) {
        inHandler.actorsSkillsBtns.removeAt(fieldActorIndToPlayerActorInd(actorInd))
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

    public fun makeTurnAI(enemy : Player)
    {
        if (isAI == true)
        {
            val enemyActor = enemy.gameWorld.field.actors[0]
            val enemyX = enemyActor.hex.i
            val enemyY = enemyActor.hex.j

            for (actor in gameWorld.field.actors)
            {
                val ind = gameWorld.field.actors.indexOf(actor)

                val x = actor.hex.i
                val y = actor.hex.j

                val diffX = Math.abs(enemyX - x)
                val diffY = Math.abs(enemyY - y)

                if (diffX > diffY)
                {
                    if (enemyX > x && (x % 2).toInt() == 0) inHandler.moveActor(ind, x + 1, y)
                    if (enemyX > x && (x % 2).toInt() == 1) inHandler.moveActor(ind, x + 1, y + 1)
                    if (enemyX < x && (x % 2).toInt() == 0) inHandler.moveActor(ind, x - 1, y - 1)
                    if (enemyX < x && (x % 2).toInt() == 1) inHandler.moveActor(ind, x - 1, y)
                }
                else
                {
                    if (enemyY > y) inHandler.moveActor(ind, x, y + 1)
                    if (enemyY < y) inHandler.moveActor(ind, x, y - 1)
                }
            }
        }
    }

    public fun getInput() : Boolean {
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
}