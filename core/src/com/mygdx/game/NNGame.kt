package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonRegion
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.EarClippingTriangulator
import com.mygdx.game.hex.HexField
import com.mygdx.game.hex.HexForPolygon
import com.mygdx.game.hex.HexPolygonDefault
import kotlin.properties.Delegates


public class NNGame2 : ApplicationAdapter() {
    private var batch : SpriteBatch by Delegates.notNull<SpriteBatch>()
    private var texture : Texture by Delegates.notNull<Texture>()
    private var polygon : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
    private var field : HexField by Delegates.notNull<HexField>()
    private var hex : PolygonRegion by Delegates.notNull<PolygonRegion>()
    override fun create() {
        batch = SpriteBatch()
        texture = Texture("tile.png")
        polygon = PolygonSpriteBatch()
        field = HexField()
        hex = HexPolygonDefault(field.hexR.toFloat()).hexRegion
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

//        val hex = HexPolygonDefault(20f).hexRegion
//        val hex2 = HexPolygonDefault(20f).hexRegion
//        val a = EarClippingTriangulator()
//        val vertices = floatArrayOf(0f, 0f, 100f, 0f, 100f, 100f, 0f, 100f)
//        val shar = a.computeTriangles(vertices).toArray()
//        val po = PolygonRegion(tr, vertices, shar)

        polygon.begin()
        for (i in 0.. field.width - 1) {
            for (j in 0..field.height - 1) {
                val cur = field.field[i][j]
                polygon.draw(hex, cur.xl.toFloat(), cur.yl.toFloat())
            }
        }
//        polygon.draw(hex, 200f, 200f)
//        polygon.draw(hex2, 0f, 0f)
   //     polygon.draw(po2, 80f, 0f)
       // polygon.draw(po2, 100f, 0f)
      //  polygon.draw(texture, 0f, 0f)
        polygon.end()
    }

    override fun dispose() {
        super.dispose()
        texture.dispose()
        batch.dispose()
    }
}