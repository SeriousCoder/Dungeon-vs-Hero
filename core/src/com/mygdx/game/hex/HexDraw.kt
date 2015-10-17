package com.mygdx.game.hex

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.EarClippingTriangulator

public abstract class HexForPolygon(private val r : Float) {
    //radius of circumscribed circle
    //x axis: left to right; y axis: down to up.
    private val xcenter = 50f
    private val ycenter = 50f
    public val vertices = floatArrayOf(
            xcenter - r, ycenter,
            xcenter - r / 2f, (ycenter + r * Math.sqrt(3.0) / 2).toFloat(),
            xcenter + r / 2f, (ycenter + r * Math.sqrt(3.0) / 2).toFloat(),
            xcenter + r, ycenter,
            xcenter + r / 2f, (ycenter - r * Math.sqrt(3.0) / 2).toFloat(),
            xcenter - r / 2f, (ycenter - r * Math.sqrt(3.0) / 2).toFloat()
    )
}
public class HexPolygonDefault (private val r : Float) : HexForPolygon(r) {
    private val texture   = Texture("tile.png")
    public  val triangles = EarClippingTriangulator().computeTriangles(vertices).toArray()
    private val tr        = TextureRegion(texture)
    public  val hexRegion = PolygonRegion(tr, vertices, triangles)
}