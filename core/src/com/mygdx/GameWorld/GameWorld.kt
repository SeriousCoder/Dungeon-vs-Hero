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

class GameWorld() {
    public var batch : PolygonSpriteBatch by Delegates.notNull<PolygonSpriteBatch>()
    public val field = HexField()

    val virtualWidth  = 360f
    val virtualHeight = 640f
    //because of line below, it'll work correctly only on 16:9 resolutions.
   // val resolutionMultiplier = Gdx.graphics.width / virtualWidth

    public var stage: Stage by Delegates.notNull<Stage>()
    var players : List<Player> by Delegates.notNull<List<Player>>()
    val playerTurnLabels = arrayListOf<Label>()

    //I need this function, 'cause otherwise there's GameRenderer in GameWorld constructor and vice versa
    public fun initPlayersAndEverythingNeedingThem(renderer: GameRenderer) {
        players = listOf(Player(this, renderer, 0, SkillExecutor(this, 0), virtualHeight, virtualWidth),
                Player(this, renderer, 1, SkillExecutor(this, 1), virtualHeight, virtualWidth))
        Gdx.input.inputProcessor = players[0].inHandler

        val actorPlacer = ActorPlacer()
        actorPlacer.addActorAtRandomPosition("Archer", 0)
        actorPlacer.addActorAtRandomPosition("DemonFighter", 1)
      //  actorPlacer.addActorAtRandomPosition("DemonFighter", 1)
      //  actorPlacer.addActorAtRandomPosition("DemonFighter", 1)
      //  actorPlacer.addActorAtRandomPosition("DemonFighter", 1)
    }

    inner class ActorPlacer {
        private val rand = Random(System.currentTimeMillis())
        public fun addActorAtRandomPosition(name: String, owner : Int) {
            var i = Math.abs(rand.nextInt())% 9
            var j = Math.abs(rand.nextInt())% 9
            while (field.field[i][j].occupied) {
                i = Math.abs(rand.nextInt())% 9
                j = Math.abs(rand.nextInt())% 9
            }
            when (name) {
                "Archer" -> addActor(Archer(field.field[i][j], owner))
                "DemonFighter" -> addActor(DemonFighter(field.field[i][j], owner))
                else -> return
            }
        }
    }

    init {
      batch = PolygonSpriteBatch()

      stage = Stage(FitViewport(virtualWidth, virtualHeight), batch)
      stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true);

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
    }

    fun addActor(actor : ActorHex) {
        val actorInd = field.addActor(actor)
        actor.hex.occupied = true
        players[actor.owner].addActorInd(actorInd)
        stage.addActor(actor)
    }

    public fun removeDeadActors() : Int {
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
        return checkVictory()
    }

    private fun checkVictory() : Int{
        for (i in players) {
            if (i.actorIndices.isEmpty()) {
                val playerColor = if (i.playerInd == 0) Color.CYAN else Color.RED
                return (1 - i.playerInd)
                var fontPlayerColor = AssetLoader.generateFont("Doux Medium.ttf", 35, playerColor)
                var labelStyle = Label.LabelStyle(fontPlayerColor, playerColor)
                var endLabel   = Label("Player ${2 - i.playerInd} wins!", labelStyle)
                endLabel.x = virtualWidth/2 - 80
                endLabel.y = virtualHeight/2
                stage.addActor(endLabel)
            }
        }
        return -1
    }

    fun update() : Int {
        if (field.deadActorsExist) {
            return removeDeadActors()
        }
        return -1
    }

    fun dispose() {
        stage.dispose()
        batch.dispose()
    }
}
