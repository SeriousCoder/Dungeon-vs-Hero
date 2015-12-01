package com.mygdx.Helpers

public class HexForLogic(val x : Float, val y : Float, val R: Float, val i : Int, val j : Int) {
    public val r  = R * Math.sqrt(3.0) / 2
    public val xl = x - R
    public val yl = (y - r).toFloat()//R * Math.sqrt(3.0) / 2

    public var occupied = false
    public var isActorHereDead = false

    public var lit = false

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

