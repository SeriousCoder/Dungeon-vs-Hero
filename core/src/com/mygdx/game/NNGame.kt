package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonRegion
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.Helpers.AssertLoader
import com.mygdx.screens.*
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.EarClippingTriangulator
import com.mygdx.game.hex.HexField
import com.mygdx.game.hex.HexForPolygon
import com.mygdx.game.hex.HexPolygonDefault
import kotlin.properties.Delegates
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.game.actors.Archer
import com.mygdx.game.gameWorld.GameRenderer
import com.mygdx.game.gameWorld.GameWorld
import com.mygdx.game.helpers.InputHandler

/*public class NNGame2 : ApplicationAdapter() {

    private var batch : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
    private var world : GameWorld by Delegates.notNull<GameWorld>()
    private var renderer : GameRenderer by Delegates.notNull<GameRenderer>()

    override fun create() {
        batch = PolygonSpriteBatch()
        world = GameWorld(batch, HexField())
        renderer = GameRenderer(world)
    }

    override fun render() {
        renderer.render()
    }

    override fun dispose() {
        super.dispose()
        renderer.dispose()
        world.dispose()
    }
}*/

public class DvHGame() : Game() {

    public override fun create() {
        Gdx.app.log("Game", "created")
        val assert = AssertLoader()
        assert.load()
        setScreen(MainScreen(assert, this))
    }

    public override fun dispose() {
        super.dispose()
    }


}