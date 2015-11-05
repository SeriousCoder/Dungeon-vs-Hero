package com.mygdx.Helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.mygdx.game.GameWorld.GameWorld
import kotlin.properties.Delegates

public class AssetLoader()
{
    public var backgroungs : Texture by Delegates.notNull<Texture>()
    public var buttons : Texture by Delegates.notNull<Texture>()
    public var units : Texture by Delegates.notNull<Texture>()

    public var gameWorld : GameWorld by Delegates.notNull<GameWorld>()


    public fun load()
    {
        backgroungs = Texture(Gdx.files.internal("Data/Images/main2.jpg"))
        units = Texture(Gdx.files.internal("Data/Images/archer.png"))
        buttons = Texture(Gdx.files.internal("Data/Images/playBtn.png"))
    }

    public fun generateFont (fileName : String, size : Int, color : Color) : BitmapFont
    {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("Data/Fonts/" + fileName))
        val param = FreeTypeFontGenerator.FreeTypeFontParameter()

        param.size = size
        param.color = color

        val RUS_CHARACTERS = "àáâãäå¸æçèéêëìíîïğñòóôõö÷øùúûüışÿÀÁÂÃÄÅ¨ÆÇÈÉÊËÌÍÎÏĞÑÒÓÔÕÖ×ØÙÚÛÜİŞß"
        val ENG_CHARACTERS     = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val DIGITS_AND_SYMBOLS = "0123456789][_!$%#@|\\/?-+=()*&.:;,{}\"?`'<>"

        param.characters = RUS_CHARACTERS + ENG_CHARACTERS + DIGITS_AND_SYMBOLS

        val font = generator.generateFont(param)
        generator.dispose()

        return font
    }

    fun getImageButtonStyle(x1 : Int, y1 : Int, x2 : Int, y2 : Int, height : Int, width : Int)
            : ImageButton.ImageButtonStyle {
        // skin for button
        val skin = Skin()
        skin.add("button-up", TextureRegion(buttons, x1, y1, height, width))
        //skin.add("button-down", TextureRegion(buttons, x2, y2, height, width))

        val style  = ImageButton.ImageButtonStyle()
        style.up   = skin.getDrawable("button-up")
        //style.down = skin.getDrawable("button-down")
        return style
    }
}