package com.mygdx.game.Helpers

import com.mygdx.GameObjects.ActorHex

public class HexForLogic(val x : Float, val y : Float, val R: Float, val i : Int, val j : Int) {
    public val r  = R * Math.sqrt(3.0) / 2
    public val xl = x - R
    public val yl = (y - r).toFloat()//R * Math.sqrt(3.0) / 2

    public var occupied = false
    public var isActorHereDead = false

    public var activated = false
    public fun changeActivation() {
        activated = !activated
    }
    public fun activate() {
        activated = true
    }
    public fun deactivate() {
        activated = false
    }
}

public class HexField() {
    public val height = 11
    public val width  = 9

    public  val hexR   = 20f

    private val xspace = 45f
    private val yspace = 70f
    //subtrahends in 3 lines below were calculated in an empiric way.
    private val xdiff       = hexR * 2f - 6f
    private val ydiff       = hexR * 2f - 2f
    private val evenOddDiff = hexR - 2

    //center of the most left and down hex
    private val xlce = xspace
    private val ylce = hexR + yspace

    val field = Array(width,
            {i -> Array(height,
                    {j -> HexForLogic(xlce + i * xdiff, evenOddDiff * (i % 2) + ylce + j * ydiff, hexR, i, j) }
            ).toList()}
    ).toList()

    public  val actors = arrayListOf<ActorHex>()//shouldn't be public; temporary workaround
    public var deadActorsExist = false
    public fun addActor(actor : ActorHex) : Int{
        actors.add(actor)
        return actors.lastIndex
    }

    public fun findHex(x : Int, y : Int) : Pair<Int, Int>?{
        for (i in 0..width - 1) {
            for (j in 0.. height - 1) {
                val rcur = field[i][j].r
                val xcur = field[i][j].x
                val ycur = field[i][j].y
                if (Math.pow(x.toDouble() - xcur, 2.0) +
                        Math.pow(y.toDouble() - ycur, 2.0) <= Math.pow(rcur.toDouble(), 2.0))
                    return Pair(i, j)//x, y
            }
        }
        return null
    }

    public fun findActorInd(i : Int, j : Int, owner : Int) : Int?{
        if (field[i][j].occupied) {
            for (k in 0..actors.size - 1) {
                val cur = actors[k]
                if (cur.hex.i == i && cur.hex.j == j && cur.owner == owner)
                    return k
            }
        }
        return null
    }

    public fun findActorIndNotOwner(i : Int, j : Int, notOwner : Int) : Int?{
        if (field[i][j].occupied) {
            for (k in 0..actors.size - 1) {
                val cur = actors[k]
                if (cur.hex.i == i && cur.hex.j == j && cur.owner != notOwner)
                    return k
            }
        }
        return null
    }

    public fun findActorInd(i : Int, j : Int) : Int?{
        if (field[i][j].occupied) {
            for (k in 0..actors.size - 1) {
                val cur = actors[k]
                if (cur.hex.i == i && cur.hex.j == j)
                    return k
            }
        }
        return null
    }

    public fun activatedActorInVicinityInd(i : Int, j : Int, owner : Int) : Int? {
        //returns index of first activated actor found in 6 nearby hexes
        val searchAr = arrayListOf<Pair<Int, Int>>()
        if (i % 2 == 0) {
            //columns 0, 2, ...
            if (i - 1 >= 0) {
                if (j - 1 >= 0) searchAr.add(Pair(i - 1, j - 1))
                searchAr.add(Pair(i - 1, j))//j always >= 0
            }
            if (i + 1 < width) {
                if (j - 1 >= 0) searchAr.add(Pair(i + 1, j - 1))
                searchAr.add(Pair(i + 1, j))//j always >= 0
            }
            if (j - 1 >= 0) searchAr.add(Pair(i, j - 1))
            if (j + 1 < height) searchAr.add(Pair(i, j + 1))
        }
        else {
            if (i - 1 >= 0) {
                if (j + 1 < height) searchAr.add(Pair(i - 1, j + 1))
                searchAr.add(Pair(i - 1, j))//j always >= 0
            }
            if (i + 1 < width) {
                if (j + 1 < height) searchAr.add(Pair(i + 1, j + 1))
                searchAr.add(Pair(i + 1, j))//j always >= 0
            }
            if (j - 1 >= 0) searchAr.add(Pair(i, j - 1))
            if (j + 1 < height) searchAr.add(Pair(i, j + 1))
        }
        for (k in searchAr) {
            val actInd = findActorInd(k.first, k.second, owner)
            if (actInd != null && actors[actInd].activated) return actInd
        }
        return null
    }

    public fun moveActor(actInd : Int, hex : HexForLogic) {
        actors[actInd].hex.occupied = false
        actors[actInd].hex.deactivate()
        actors[actInd].hex = hex
        actors[actInd].hex.occupied = true
        actors[actInd].actorX = hex.xl + 6
        actors[actInd].actorY = hex.yl + 4
        actors[actInd].activated = false
    }

    public fun findActorIndInField(actor : ActorHex) : Int? {
        for (i in 0..actors.size -1)
            if (actors[i] == actor) return i
        return null
    }
}