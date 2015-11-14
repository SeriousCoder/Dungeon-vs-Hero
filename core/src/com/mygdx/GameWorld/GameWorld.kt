package com.mygdx.game.GameWorld

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.GameObjects.ActorHex
import com.mygdx.GameObjects.DemonFighter
import com.mygdx.Helpers.SkillExecutor
import com.mygdx.game.gameObjects.Archer
import com.mygdx.game.Helpers.InputHandler
import com.mygdx.game.Helpers.HexField
import com.mygdx.game.Player
import kotlin.properties.Delegates

class GameWorld(public val batch : PolygonSpriteBatch, public val field : HexField) {

    val virtualWidth  = 360f
    val virtualHeight = 640f
    //because of line below, it'll work correctly only on 16:9 resolutions.
   // val resolutionMultiplier = Gdx.graphics.width / virtualWidth
    private var stage: Stage by Delegates.notNull<Stage>()

    val players = listOf(Player(0, field, SkillExecutor(this, 0), virtualHeight, virtualWidth),
            Player(1, field, SkillExecutor(this, 1), virtualHeight, virtualWidth))
  //  val player0 = Player(0, InputHandler(field, virtualHeight.toInt(), resolutionMultiplier))
  //  val player1 = Player(1, InputHandler(field, virtualHeight.toInt(), resolutionMultiplier))
    init {
      stage = Stage(FitViewport(virtualWidth, virtualHeight), batch)
      stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);


      val archer = Archer(field.field[5][5], 1)
      val demon = DemonFighter(field.field[0][0], 0)
      val demon2 = DemonFighter(field.field[3][3], 1)
      addActor(archer)
      addActor(demon)
      addActor(demon2)

      Gdx.input.inputProcessor = players[0].inHandler
    }

    fun addActor(actor : ActorHex) {
        val actorInd = field.addActor(actor)
        actor.hex.occupied = true
        players[actor.owner].addActorInd(actorInd)
        stage.addActor(actor)
    }

    fun update() {
        stage.draw()
        if (Gdx.input.inputProcessor is Stage) {
            val curStage = Gdx.input.inputProcessor as Stage
            curStage.draw()
            curStage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
        }
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);
    }

    fun dispose() {
        stage.dispose()
    }
}
