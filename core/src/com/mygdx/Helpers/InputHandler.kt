package com.mygdx.game.Helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.Helpers.AssetLoader
import com.mygdx.game.Player
import java.util.*
import kotlin.properties.Delegates

class InputHandler (private val field : HexField, val virtualHeight : Float, val virtualWidth : Float, val player: Player) : InputProcessor {
    var dataHasBeenGot = false
    private var stageUI: Stage by Delegates.notNull<Stage>()
    val actorsSkills = arrayListOf<ArrayList<ImageTextButton>>()
    val asset = AssetLoader()
  //  val actorsSkills : ArrayList<ArrayList<ImageTextButton>> by Delegates.notNull<ArrayList<ArrayList<ImageTextButton>>>()
    //list of lists of button for skills of actors. One outer list - one actor. One inner list - buttons of his skills.

    private inner class clickHelper(val inh : InputHandler) : ClickListener() {
        override fun touchDown(event : InputEvent , x : Float , y: Float, pointer : Int, button : Int) : Boolean{
            val resolutionMultiplier = Gdx.graphics.width / virtualWidth
            val screenY = Gdx.graphics.height - resolutionMultiplier * y
            val screenX = x * resolutionMultiplier
            if (y > 75) {
                Gdx.input.inputProcessor = inh
                return inh.touchDown(screenX.toInt(), screenY.toInt(), 0, 0)
            }
            return true
        }
    }

    public fun watchForNewActors() {
        val buttonsAtlas = TextureAtlas("Data/UI/SkillButtons.pack"); //** button atlas image **//
        val buttonSkin = Skin(buttonsAtlas)
        val font = asset.generateFont("Doux Medium.ttf", 15, Color.RED)
        for (i in player.actorIndices) {
            val tempAr = arrayListOf<ImageTextButton>()
            for (j in 0..field.actors[i].skills.size - 1) {
                val skillName = field.actors[i].skills[j]
                val skillPicsPair = field.actors[i].skillPics[skillName] ?: throw  Exception()//correct later
                val style = ImageTextButton.ImageTextButtonStyle()
                style.up = buttonSkin.getDrawable(skillPicsPair.first)
                style.down = buttonSkin.getDrawable(skillPicsPair.second)
                style.font = font
                val button = ImageTextButton("", style)
                button.width = 70f
                button.height = 70f
                button.setPosition((j * 80).toFloat(), 0f)
                button.addListener(object : ClickListener() {
                    override fun clicked(event : InputEvent, x : Float, y : Float) {
                        dataHasBeenGot = true
                    }
                })
                //        button.isVisible = false
                stageUI.addActor(button)
                tempAr.add(button)
            }
            actorsSkills.add(tempAr)
        }
    }

    init {
        //TESTING UI BEGIN

        stageUI = Stage(FitViewport(virtualWidth, virtualHeight))

        //Not working version (for unknown reason):
//        for (i in player.actorIndices) {
//            actorsSkills.add(Array(field.actors[i].skills.size, {j ->
//                val button = ImageTextButton(field.actors[i].skills[j], skin)
//                button.width = 50f
//                button.height = 50f
//                button.setPosition((j * 60).toFloat(), 0f)
//                button.addListener(object : ClickListener() {
//                    override fun clicked(event : InputEvent, x : Float, y : Float) {
//                        dataHasBeenGot = true
//                    }
//                })
//        //        button.isVisible = false
//                stageUI.addActor(button)
//                button
//            }
//            ).toArrayList())
//        }




  //      val button = TextButton("Click me", skin, "default")

        stageUI.addListener (clickHelper(this))

//        button.width = 50f
//        button.height = 50f
//        button.setPosition(Gdx.graphics.width /2 - 100f, 0f)
//        button.addListener(object : ClickListener() {
//            override fun clicked(event : InputEvent, x : Float, y : Float) {
//                dataHasBeenGot = true
//            }
//        })
//        stageUI.addActor(button)
        //TESTING UI END
    }
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) : Boolean {
        //resizing doesn't work properly.
        val resolutionMultiplier = Gdx.graphics.width / virtualWidth
        val virtualX = screenX / resolutionMultiplier
        val virtualY =  (Gdx.graphics.height - screenY) / resolutionMultiplier
        val hexInd = field.findHex(virtualX.toInt(), virtualY.toInt()) ?: return true

        val iInd = hexInd.first
        val jInd = hexInd.second

        var actInd = field.findActorInd(iInd, jInd, player.playerInd)
        if (actInd != null) {
            for (i in 0..field.actors.size - 1) if (i != actInd) field.actors[i].deactivate()
            if (field.actors[actInd].changeActivation()) {
                Gdx.input.inputProcessor = stageUI
            }
        }
        else {
            actInd = field.activatedActorInVicinityInd(iInd, jInd, player.playerInd)
            if (actInd == null) {
                for (i in 0..field.actors.size - 1) if (i != actInd) field.actors[i].deactivate()
                return true
            }
            field.moveActor(actInd, field.field[iInd][jInd])
            dataHasBeenGot = true
        }
        return true // Return true to say we handled the touch.
    }
    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

}
