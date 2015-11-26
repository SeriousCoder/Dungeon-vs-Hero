package com.mygdx.game.Helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.GameObjects.ActorHex
import com.mygdx.Helpers.AssetLoader
import com.mygdx.Helpers.SkillExecutor
import com.mygdx.game.GameWorld.GameRenderer
import com.mygdx.game.GameWorld.GameWorld
import com.mygdx.game.Player
import java.util.*
import kotlin.properties.Delegates

class InputHandler (private val gameWorld: GameWorld,
                    private val gameRenderer: GameRenderer,
                    private val skillExec : SkillExecutor,
                    private val virtualHeight : Float, private val virtualWidth : Float,
                    private val player: Player) : InputProcessor {
    private val field = gameWorld.field
    private var stageUI    : Stage by Delegates.notNull<Stage>()
    private var curSkill   : SkillBeingUsedData? = null
    var actorsActionPoints : Label by Delegates.notNull<Label>()
    var skillDescription   : Label by Delegates.notNull<Label>()

    val actorsSkillsBtns = arrayListOf<ArrayList<ImageButton>>()
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

        val fontBig    = AssetLoader.generateFont("Doux Medium.ttf", 35, Color.WHITE)
        var labelStyle = Label.LabelStyle(fontBig, fontBig.color)
        actorsActionPoints   = Label("AP: ", labelStyle)
        actorsActionPoints.x = virtualWidth - 100
        actorsActionPoints.y = 20f
        actorsActionPoints.isVisible = false

        val fontSmall = AssetLoader.generateFont("Doux Medium.ttf", 25, Color.WHITE)
        labelStyle    = Label.LabelStyle(fontSmall, fontSmall.color)
        skillDescription   = Label("", labelStyle)
        skillDescription.x = 20f
        skillDescription.y = virtualHeight - 50

        stageUI.addActor(actorsActionPoints)
        stageUI.addActor(skillDescription)
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
        val buttonSkin   = Skin(buttonsAtlas)

        val newActorInd = player.actorIndices[player.actorsNum]
        val tempAr      = arrayListOf<ImageButton>()

        val curActor = field.actors[newActorInd]

        for (j in 0..curActor.skills.size - 1) {
            val skillName     = curActor.skills[j].first
            val skillPicsPair = curActor.skillPics[skillName]
                    ?: throw  Exception("Something's wrong with skill names/skill pics names")

            val style  = ImageButton.ImageButtonStyle()
            style.up   = buttonSkin.getDrawable(skillPicsPair.first)
            style.down = buttonSkin.getDrawable(skillPicsPair.second)

            val button    = ImageButton(style)
            button.width  = 70f
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
                        skillDescription.setText(skillName)
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
        actorsActionPoints.setText("AP: " + actor.curActionPoints)
    }

    fun changeVisibilityActionPoints() {
        actorsActionPoints.isVisible = !actorsActionPoints.isVisible
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
                cleanUpAfterUsingSkill()
                actorsActionPoints.isVisible = false
                field.deactivateActorsExcept(-1)
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
        player.restoreActionPoints()
    }

    private fun deactivateActorUI() {
        gameRenderer.disableDrawingUI()
        actorsActionPoints.isVisible = false
        delCurSkill()
        hideButtons()
        skillDescription.setText("")
    }

    private fun activateActorUI(actIndInPlayer : Int) {
        Gdx.input.inputProcessor = stageUI
        hideButtons()
        for (btn in actorsSkillsBtns[actIndInPlayer])
            btn.isVisible = true
        actorsActionPoints.isVisible = true
        gameRenderer.enableDrawingUI(stageUI)
    }

    private fun moveActor(actInd : Int, iInd : Int, jInd : Int) {
        val moveCost = 1
        val movingActor = field.actors[actInd]
        if (movingActor.curActionPoints >= moveCost) {
            field.moveActor(actInd, field.field[iInd][jInd])
            movingActor.curActionPoints--
            cleanUpAfterUsingSkill()
        }
    }

    private fun cleanUpAfterUsingSkill() {
        changeVisibilityActionPoints()
        delCurSkill()
        hideButtons()
        skillDescription.setText("")
        tryToEndTurn()
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
            else {
                delCurSkill()
                hideButtons()
            }

            field.deactivateActorsExcept(actInd)
            //changeVisibilityActionPoints()
            updateActionPoints(curActor)
            if (curActor.changeActivation()) activateActorUI(actIndInPlayer)
            else deactivateActorUI()
        }
        else {
            actInd = field.activatedActorInVicinityInd(iInd, jInd, player.playerInd)
            if (!field.field[iInd][jInd].occupied) {
                val skillHasSucceeded =  tryToUseSkill(iInd, jInd)
                if (actInd != null && !skillHasSucceeded) {
                    moveActor(actInd, iInd, jInd)
                }
            }
            else {
                var actEnemyInd = field.findActorInd(iInd, jInd)
                if (actEnemyInd != null) {
                    if (!tryToUseSkill(field.actors[actEnemyInd])) deactivateActorUI()
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
