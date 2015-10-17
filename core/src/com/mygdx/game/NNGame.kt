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
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.mygdx.game.actors.Archer

public class NNGame2 : ApplicationAdapter() {
    private var batch : SpriteBatch by Delegates.notNull<SpriteBatch>()
    private var texture : Texture by Delegates.notNull<Texture>()
    private var polygon : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
    private var field : HexField by Delegates.notNull<HexField>()
    private var hex : PolygonRegion by Delegates.notNull<PolygonRegion>()



    private var stage: Stage? = null

    override fun create() {
        batch = SpriteBatch()
        texture = Texture("tile.png")
        polygon = PolygonSpriteBatch()
        field = HexField()
        hex = HexPolygonDefault(field.hexR.toFloat()).hexRegion

        stage = Stage()
        Gdx.input.setInputProcessor(stage)

        val myActor = Archer(100f, 300f)
        myActor.setTouchable(Touchable.enabled)
        stage!!.addActor(myActor)
    }

    override fun render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        polygon.begin()
        for (i in 0.. field.width - 1) {
            for (j in 0..field.height - 1) {
                val cur = field.field[i][j]
                polygon.draw(hex, cur.xl.toFloat(), cur.yl.toFloat())
            }
        }
        polygon.end()
        stage!!.act(Gdx.graphics.getDeltaTime())
        stage!!.draw()
    }

    override fun dispose() {
        super.dispose()
        texture.dispose()
        batch.dispose()
    }
}