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
import com.mygdx.GameObjects.ActorHex
import com.mygdx.Helpers.AssetLoader
import com.mygdx.Helpers.SkillExecutor
import com.mygdx.game.GameWorld.GameRenderer
import com.mygdx.game.Player
import java.util.*
import kotlin.properties.Delegates

class InputHandler (private val field : HexField, private val skillExec : SkillExecutor,
                    private val virtualHeight : Float, private val virtualWidth : Float, private val player: Player) : InputProcessor {
    var dataHasBeenGot = false
    private var stageUI: Stage by Delegates.notNull<Stage>()
    val actorsSkillsBtns = arrayListOf<ArrayList<ImageTextButton>>()
    private var curSkill : SkillBeingUsedData? = null
  //  val actorsSkills : ArrayList<ArrayList<ImageTextButton>> by Delegates.notNull<ArrayList<ArrayList<ImageTextButton>>>()
    //list of lists of button for skills of actors. One outer list - one actor. One inner list - buttons of his skills.
    private fun getThisInputHandler() : InputHandler{
        return this
    }

    class SkillBeingUsedData(
        val skillName : String,
    //    val playerActorInd : Int,
        val actor : ActorHex,
        val iFst: Int,
        val jFst: Int
    ) {
        var iSnd: Int? = null
        var jSnd: Int? = null
    }

    public fun addNewActorSkills() {
        val buttonsAtlas = TextureAtlas("Data/UI/SkillButtons.pack"); //** button atlas image **//
        val buttonSkin = Skin(buttonsAtlas)
        val font = AssetLoader.generateFont("Doux Medium.ttf", 15, Color.RED)

        val newActorInd = player.actorIndices[player.actorsNum]
        val tempAr = arrayListOf<ImageTextButton>()

        val curActor = field.actors[newActorInd]
        for (j in 0..curActor.skills.size - 1) {
            val skillName = curActor.skills[j].first
            val skillPicsPair = curActor.skillPics[skillName]
                    ?: throw  Exception("Something's wrong with skill names/skill pics names")

            val style = ImageTextButton.ImageTextButtonStyle()
            style.up = buttonSkin.getDrawable(skillPicsPair.first)
            style.down = buttonSkin.getDrawable(skillPicsPair.second)
            style.font = font

            val button = ImageTextButton("", style)
            button.width = 70f
            button.height = 70f
            button.setPosition((j * 80).toFloat(), 0f)

            //button.addListener(btnListener(curActor, skillName, j))
            button.addListener(object : ClickListener() {
                override fun clicked(event : InputEvent, x : Float, y : Float) {
                    val skillCost = curActor.skills[j].second
                    if (skillCost <= curActor.curActionPoints) {
                        val iInd = curActor.hex.i
                        val jInd = curActor.hex.j

                        curSkill = SkillBeingUsedData(skillName, curActor, iInd, jInd)
                        skillExec.changeHexLightForSkill(skillName, iInd, jInd)
                        Gdx.input.inputProcessor = getThisInputHandler()
                    }
                }
            })
            button.isVisible = false

            stageUI.addActor(button)
            tempAr.add(button)
        }
        actorsSkillsBtns.add(tempAr)
    }

    init {
        stageUI = Stage(FitViewport(virtualWidth, virtualHeight))
        stageUI.addListener (object : ClickListener() {
            override fun touchDown(event : InputEvent , x : Float , y: Float, pointer : Int, button : Int) : Boolean{
                val resolutionMultiplier = Gdx.graphics.width / virtualWidth
                val screenY = Gdx.graphics.height - resolutionMultiplier * y
                val screenX = x * resolutionMultiplier

                if (y > 75) {
                    val inputHandler = getThisInputHandler()
                    Gdx.input.inputProcessor = inputHandler
//                    for (i in actorsSkillsBtns)
//                        for (j in i)
//                            j.isVisible = false
                    return inputHandler.touchDown(screenX.toInt(), screenY.toInt(), 0, 0)
                }
                return true
            }
        })
    }

    fun hideButtons() {
        for (btnArray in actorsSkillsBtns)
            for (btn in btnArray)
                btn.isVisible = false
    }

    private fun tryToUseSkill(curActor : ActorHex) : Boolean {
        val curSkillVal = curSkill
        if (curSkillVal != null) {
            skillExec.changeHexLightForSkill(curSkillVal.skillName, curSkillVal.iFst, curSkillVal.jFst)
            curSkillVal.iSnd = curActor.hex.i
            curSkillVal.jSnd = curActor.hex.j
            if (skillExec.useSkill(curSkillVal)) {
                field.deadActorsExist = true

                curSkillVal.actor.skillHaveBeenUsed(curSkillVal.skillName)
                curSkill = null
                field.deactivateActorsExcept(-1)
                hideButtons()
                endTurn()
                return true
            }
        }
        return false
    }

    private fun endTurn() {
        dataHasBeenGot = true
        for (i in player.actorIndices) {
            val actor = field.actors[i]
            actor.curActionPoints = actor.maxActionPoints
        }
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
            val actIndInPlayer = player.fieldActorIndToPlayerActorInd(actInd)
            if (actIndInPlayer < 0)  throw Exception("Something's wrong in adding actors to players.")

            val curActor = field.actors[actInd]

            if (tryToUseSkill(curActor)) return true

            field.deactivateActorsExcept(actInd)
            if (curActor.changeActivation()) {
                Gdx.input.inputProcessor = stageUI
                hideButtons()
                for (btn in actorsSkillsBtns[actIndInPlayer])
                    btn.isVisible = true
                GameRenderer.enableDrawingUI(stageUI)
            }
            else {
                GameRenderer.disableDrawingUI()
                for (btn in actorsSkillsBtns[actIndInPlayer])
                    btn.isVisible = false
            }
        }
        else {
            actInd = field.activatedActorInVicinityInd(iInd, jInd, player.playerInd)
            if (!field.field[iInd][jInd].occupied) {
                if (actInd != null) {
                    field.moveActor(actInd, field.field[iInd][jInd])
                    hideButtons()
                    endTurn()
                }
            }
            else {
                var actEnemyInd = field.findActorInd(iInd, jInd)
                if (actEnemyInd != null) {
                    tryToUseSkill(field.actors[actEnemyInd])
                }
                field.deactivateActorsExcept(-1)//dubious
            }
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
