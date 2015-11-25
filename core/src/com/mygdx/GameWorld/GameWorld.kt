package com.mygdx.game.GameWorld

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.GameObjects.ActorHex
import com.mygdx.GameObjects.DemonFighter
import com.mygdx.Helpers.AssetLoader
import com.mygdx.Helpers.SkillExecutor
import com.mygdx.game.gameObjects.Archer
import com.mygdx.game.Helpers.InputHandler
import com.mygdx.game.Helpers.HexField
import com.mygdx.game.Player
import java.util.*
import kotlin.properties.Delegates

object GameWorld {
    public var batch : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
    public val field = HexField()
    val virtualWidth  = 360f
    val virtualHeight = 640f
    //because of line below, it'll work correctly only on 16:9 resolutions.
   // val resolutionMultiplier = Gdx.graphics.width / virtualWidth
    public var stage: Stage by Delegates.notNull<Stage>()

    val players = listOf(Player(0, field, SkillExecutor(0), virtualHeight, virtualWidth),
            Player(1, field, SkillExecutor(1), virtualHeight, virtualWidth))
    val playerTurnLabels = arrayListOf<Label>()
  //  val player0 = Player(0, InputHandler(field, virtualHeight.toInt(), resolutionMultiplier))
  //  val player1 = Player(1, InputHandler(field, virtualHeight.toInt(), resolutionMultiplier))
    init {
      batch = PolygonSpriteBatch()

      stage = Stage(FitViewport(virtualWidth, virtualHeight), batch)
      stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);

      val rand = Random(System.currentTimeMillis())
      val a = arrayOf(rand.nextInt())

//      val archer = Archer(field.field[2][0], 1)
//      val demon = DemonFighter(field.field[0][0], 0)
//      val demon2 = DemonFighter(field.field[0][2], 1)

     // val archer = Archer(field.field[Math.abs(rand.nextInt()) % 5][Math.abs(rand.nextInt()) % 5], 1)
      val archer2 = Archer(field.field[Math.abs(rand.nextInt())% 9][Math.abs(rand.nextInt())% 9], 0)
   //   val demon = DemonFighter(field.field[Math.abs(rand.nextInt())% 5][Math.abs(rand.nextInt())% 5], 0)
      val demon2 = DemonFighter(field.field[Math.abs(rand.nextInt())% 9][Math.abs(rand.nextInt())% 9], 1)
      val demon3 = DemonFighter(field.field[Math.abs(rand.nextInt())% 9][Math.abs(rand.nextInt())% 9], 1)
      val demon1 = DemonFighter(field.field[Math.abs(rand.nextInt())% 9][Math.abs(rand.nextInt())% 9], 1)
      val demon4 = DemonFighter(field.field[Math.abs(rand.nextInt())% 9][Math.abs(rand.nextInt())% 9], 1)
    //  addActor(archer)
      addActor(archer2)
    //  addActor(demon)
      addActor(demon2)
      addActor(demon3)
      addActor(demon1)
      addActor(demon4)

      for (i in 0..1) {
          val playerColor = if (i ==0) Color.RED else Color.CYAN
          var fontPlayerColor = AssetLoader.generateFont("Doux Medium.ttf", 25, playerColor)
          var labelStyle = Label.LabelStyle(fontPlayerColor, playerColor)
          var turnLabel   = Label("Turn of player $i", labelStyle)
          turnLabel.x = virtualWidth - 180
          turnLabel.y = virtualHeight - 30
          turnLabel.isVisible = i == 0
          playerTurnLabels.add(turnLabel)
          stage.addActor(turnLabel)
      }

      Gdx.input.inputProcessor = players[0].inHandler
    }

    fun addActor(actor : ActorHex) {
        val actorInd = field.addActor(actor)
        actor.hex.occupied = true
        players[actor.owner].addActorInd(actorInd)
        stage.addActor(actor)
    }

    public fun removeDeadActors() {
        for (i in 0..field.actors.size - 1) {
            val curActor : ActorHex
            try {
                curActor = field.actors[i]
            }
            catch (exc : Exception) {
                break
            }
            if (curActor.curHealthPoints <= 0) {
                curActor.hex.occupied = false

                players[curActor.owner].delActorInd(i)
                players[1 - curActor.owner].correctActorFieldIndicesAfterDeletion(i)
                curActor.remove()
                field.actors.remove(curActor)
            }
        }
        field.deadActorsExist = false
        checkVictory()
    }

    private fun checkVictory() {
        for (i in players) {
            if (i.actorIndices.isEmpty()) {
                val playerColor = if (i.playerInd == 0) Color.CYAN else Color.RED
                var fontPlayerColor = AssetLoader.generateFont("Doux Medium.ttf", 35, playerColor)
                var labelStyle = Label.LabelStyle(fontPlayerColor, playerColor)
                var endLabel   = Label("Player ${1 - i.playerInd} wins!", labelStyle)
                endLabel.x = virtualWidth/2 - 80
                endLabel.y = virtualHeight/2
                stage.addActor(endLabel)
            }
        }
    }

    fun update() {
        if (field.deadActorsExist) removeDeadActors()
    }

    fun dispose() {
        stage.dispose()
        batch.dispose()
    }
}
