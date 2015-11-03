package com.mygdx.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.PolygonRegion
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.mygdx.Helpers.AssetLoader
import com.mygdx.game.DvHGame
import com.mygdx.game.GameWorld.GameRenderer
import com.mygdx.game.GameWorld.GameWorld
import com.mygdx.game.helpers.HexField
import com.mygdx.game.helpers.HexPolygonDefault
import kotlin.properties.Delegates


public class LevelScreen(val asset: AssetLoader, game : DvHGame) : Screen
{
    private var batch : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
    private var renderer : GameRenderer by Delegates.notNull<GameRenderer>()

    init {
        batch = PolygonSpriteBatch()
        asset.gameWorld = GameWorld(batch, HexField())
        renderer = GameRenderer(asset.gameWorld)
    }

    override fun render(delta: Float) {
        renderer.render()
       // asset.gameWorld!!.update()
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        asset.gameWorld.dispose()
    }

    override fun show() {

    }

    override fun pause() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun hide() {

    }



    override fun resume() {

    }



}