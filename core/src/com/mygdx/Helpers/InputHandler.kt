package com.mygdx.game.Helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
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
    private var stageUI  : Stage by Delegates.notNull<Stage>()
    private var curSkill : SkillBeingUsedData? = null

    val actorsSkillsBtns = arrayListOf<ArrayList<ImageTextButton>>()
    val actorsActionPoints = arrayListOf<Pair<ActorHex, Label>>()
    var dataHasBeenGot   = false

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
                    return inputHandler.touchDown(screenX.toInt(), screenY.toInt(), 0, 0)
                }
                return true
            }
        })
    }

    private fun getThisInputHandler() : InputHandler{
        return this
    }

    class SkillBeingUsedData(val skillName : String,
                             val actor : ActorHex,
                             val iFst: Int, val jFst: Int) {
        var iSnd: Int? = null
        var jSnd: Int? = null
    }

    private fun delCurSkill() {
        val curSkillVal = curSkill
        if (curSkillVal != null)
            skillExec.changeHexLightForSkill(curSkillVal.skillName, curSkillVal.iFst, curSkillVal.jFst)
        curSkill = null
    }

    public fun addNewActorSkills() {
        val buttonsAtlas = TextureAtlas("Data/UI/SkillButtons.pack"); //** button atlas image **//
        val buttonSkin = Skin(buttonsAtlas)
        val font = AssetLoader.generateFont("Doux Medium.ttf", 35, Color.WHITE)

        val newActorInd = player.actorIndices[player.actorsNum]
        val tempAr = arrayListOf<ImageTextButton>()

        val curActor = field.actors[newActorInd]

        val labelStyle = Label.LabelStyle(font, font.color)
        val actionPoints = Label("AP: " + curActor.maxActionPoints.toString(), labelStyle)
        actionPoints.x = virtualWidth - 100
        actionPoints.y = 20f
        stageUI.addActor(actionPoints)
        actorsActionPoints.add(Pair(curActor, actionPoints))
        actionPoints.isVisible = false

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

    fun updateActionPoints(actor: ActorHex) {
        for (i in actorsActionPoints)
            if (i.first == actor) {
                i.second.setText("AP: " + actor.curActionPoints)
                break
            }
    }

    fun changeVisibilityActionPoints(actor: ActorHex?) {
        actor ?: return
        for (i in actorsActionPoints)
            if (i.first == actor) {
                i.second.isVisible = !(i.second.isVisible)
                break
            }
    }

    fun hideButtons() {
        for (btnArray in actorsSkillsBtns)
            for (btn in btnArray)
                btn.isVisible = false
    }

    private fun tryToUseSkill(curActor : ActorHex) : Boolean {
        return tryToUseSkill(curActor.hex.i, curActor.hex.j)
    }

    private fun tryToUseSkill(iSnd : Int, jSnd : Int) : Boolean {
        val curSkillVal = curSkill
        if (curSkillVal != null) {

            curSkillVal.iSnd = iSnd
            curSkillVal.jSnd = jSnd
            if (skillExec.useSkill(curSkillVal)) {
                field.deadActorsExist = true

                curSkillVal.actor.skillHaveBeenUsed(curSkillVal.skillName)
                updateActionPoints(curSkillVal.actor)
                changeVisibilityActionPoints(curSkillVal.actor)
                delCurSkill()
                field.deactivateActorsExcept(-1)
                hideButtons()
                tryToEndTurn()
                return true
            }
        }
        return false
    }

    private fun checkIfNoAP() : Boolean{
        var sum = 0
        for (i in player.actorIndices)
            sum += field.actors[i].curActionPoints
        return sum == 0
    }

    private fun tryToEndTurn() {
        if (!checkIfNoAP()) return
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

            if (curSkill?.actor != curActor) delCurSkill()

            if (tryToUseSkill(curActor)) return true

            field.deactivateActorsExcept(actInd)
            changeVisibilityActionPoints(curActor)
            if (curActor.changeActivation()) {
                Gdx.input.inputProcessor = stageUI
                hideButtons()
                for (btn in actorsSkillsBtns[actIndInPlayer])
                    btn.isVisible = true
                GameRenderer.enableDrawingUI(stageUI)
            }
            else {
                GameRenderer.disableDrawingUI()
                delCurSkill()
                for (btn in actorsSkillsBtns[actIndInPlayer])
                    btn.isVisible = false
            }
        }
        else {
            actInd = field.activatedActorInVicinityInd(iInd, jInd, player.playerInd)
            if (!field.field[iInd][jInd].occupied) {
                if (actInd != null) {
                    field.moveActor(actInd, field.field[iInd][jInd])
                    val movingActor = field.actors[actInd]
                    changeVisibilityActionPoints(movingActor)
                    delCurSkill()
                    hideButtons()
                    tryToEndTurn()
                }
                else {
                    tryToUseSkill(iInd, jInd)
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
        return true
    }

    override fun keyDown(keycode: Int): Boolean = false

    override fun keyUp(keycode: Int): Boolean = false

    override fun keyTyped(character: Char): Boolean = false

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false

    override fun scrolled(amount: Int): Boolean = false

}
