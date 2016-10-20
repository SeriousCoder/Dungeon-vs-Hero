package com.mygdx.Helpers

import com.mygdx.GameObjects.ActorHex
import java.util.*

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

    public val actors = arrayListOf<ActorHex>()
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

    public fun activatedActorInVicinityInd(i : Int, j : Int, owner : Int) : Int? {
        //returns index of first activated actor found in 6 nearby hexes
        val searchAr = hexesInVicinity(i, j)
        for (k in searchAr) {
            val actInd = findActorInd(k.first, k.second, owner)
            if (actInd != null && actors[actInd].activated) return actInd
        }
        return null
    }

    public fun hexesInVicinityRadius(i : Int, j : Int, r : Int = 1) : MutableList<Pair<Int, Int>>?{
        if (r < 0)  return null
        if (r == 0) return arrayListOf(Pair(i, j))

        val res = HashSet<Pair<Int, Int>>()
        var curR = r

        var neighbours = hexesInVicinity(i, j)
        res.addAll(neighbours)
        curR--
        while (curR > 0) {
            val temp = arrayListOf<Pair<Int, Int>>()
            for (hex in neighbours) {
                for (k in hexesInVicinity(hex.first, hex.second)) {
                    temp.add(k)
                }
            }
            neighbours = temp
            res.addAll(temp)
            curR--
        }
        return res.toMutableList()//toArrayList()
    }

    private fun hexesInVicinity(i : Int, j : Int) : MutableList<Pair<Int, Int>> {
        val res = arrayListOf<Pair<Int, Int>>()
        if (i % 2 == 0) {
            //columns 0, 2, ...
            if (i - 1 >= 0) {
                if (j - 1 >= 0) res.add(Pair(i - 1, j - 1))
                res.add(Pair(i - 1, j))//j always >= 0
            }
            if (i + 1 < width) {
                if (j - 1 >= 0) res.add(Pair(i + 1, j - 1))
                res.add(Pair(i + 1, j))//j always >= 0
            }
            if (j - 1 >= 0) res.add(Pair(i, j - 1))
            if (j + 1 < height) res.add(Pair(i, j + 1))
        }
        else {
            if (i - 1 >= 0) {
                if (j + 1 < height) res.add(Pair(i - 1, j + 1))
                res.add(Pair(i - 1, j))//j always >= 0
            }
            if (i + 1 < width) {
                if (j + 1 < height) res.add(Pair(i + 1, j + 1))
                res.add(Pair(i + 1, j))//j always >= 0
            }
            if (j - 1 >= 0) res.add(Pair(i, j - 1))
            if (j + 1 < height) res.add(Pair(i, j + 1))
        }
        return res
    }

    public fun moveActor(actInd : Int, hex : HexForLogic) : Boolean {
        if (hex.occupied) return false
        actors[actInd].hex.occupied = false
        actors[actInd].hex.deactivate()
        actors[actInd].hex = hex
        actors[actInd].hex.occupied = true
        actors[actInd].actorX = hex.xl + 6
        actors[actInd].actorY = hex.yl + 4
        actors[actInd].activated = false
        return true
    }

    public fun findActorIndInField(actor : ActorHex) : Int? {
        for (i in 0..actors.size -1)
            if (actors[i] == actor) return i
        return null
    }

    fun deactivateActorsExcept(doNotDeactivateInd : Int) {
        for (i in 0..actors.size - 1) if (i != doNotDeactivateInd) actors[i].deactivate()
    }

    fun dispose() {
//        field.dispose()
//        actors.dispose()
    }
}