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
import com.mygdx.game.hex.Hex
import kotlin.properties.Delegates


public class NNGame2 : ApplicationAdapter() {
    private var batch : SpriteBatch by Delegates.notNull<SpriteBatch>()
    private var texture : Texture by Delegates.notNull<Texture>()
    private var polygon : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
    override fun create() {
        batch = SpriteBatch()
        texture = Texture("tile.png")
        polygon = PolygonSpriteBatch()

    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val tr = TextureRegion(texture, 100, 100)
        val hex = Hex(50f, 50f, 40f)
        val po = PolygonRegion(tr, hex.vertices, hex.triangles)
        val hex2 = Hex(80f, 200f, 40f)
        val po2 = PolygonRegion(tr, hex2.vertices, hex2.triangles)
//        val a = EarClippingTriangulator()
//        val vertices = floatArrayOf(0f, 0f, 100f, 0f, 100f, 100f, 0f, 100f)
//        val shar = a.computeTriangles(vertices).toArray()
//        val po = PolygonRegion(tr, vertices, shar)
        polygon.begin()
        polygon.draw(tr, 200f, 200f)
        polygon.draw(po, 0f, 0f)
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