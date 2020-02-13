package com.caseyjbrooks.babbage

import kotlin.js.Promise

@JsModule("boardgame.io/client")
@JsNonModule
external object BoardgameIoClient {
    fun <Game: Boardgame> Client(game: BoardgameApp<Game>): BoardgameApp<Game>
}

@JsModule("boardgame.io/ai")
@JsNonModule
external object BoardgameAis {
    class RandomBot(game: dynamic)
    class MCTSBot(game: dynamic)

    fun Step(client: BoardgameApp<*>, bot: dynamic): Promise<*>
    fun Simulate(config: dynamic): Promise<dynamic>
}

class BoardgameApp<Game: Boardgame>(
    val game: Game
)

val <Game: Boardgame> BoardgameApp<Game>.ai: Game get() = asDynamic().ai

val BoardgameApp<*>.currentState: dynamic get() = asDynamic().store.getState()
val BoardgameApp<*>.currentContext: dynamic get() = currentState.ctx
val BoardgameApp<*>.currentGameData: dynamic get() = currentState.G
val BoardgameApp<*>.moves: dynamic get() = asDynamic().moves

abstract class Boardgame(
    val seed: String
) {

// Normal functions
//----------------------------------------------------------------------------------------------------------------------

    @JsName("_setup")
    abstract fun setup(): dynamic

    @JsName("_endIf")
    abstract fun endIf(g: dynamic, ctx: dynamic): dynamic

    @JsName("_enumerate")
    abstract fun enumerate(g: dynamic, ctx: dynamic): List<Move>

    @JsName("_turn")
    open fun turn(): TurnConfig {
        return TurnConfig()
    }

    @JsName("_phases")
    open fun phases(): PhaseConfig {
        return PhaseConfig()
    }

    @JsName("_moves")
    abstract fun moves(): dynamic

// Helper Functions
//----------------------------------------------------------------------------------------------------------------------

    abstract fun aggregateWinners(winnerMap: Map<String?, dynamic>, result: dynamic): Map<String?, dynamic>

    fun doObjectInit(): Boardgame {
        return this.applyAsDynamic {
            setup = { setup() }
            endIf = { g: dynamic, ctx: dynamic -> endIf(g, ctx) }
            ai = obj { enumerate = { g: dynamic, ctx: dynamic -> enumerate(g, ctx).toTypedArray() } }
            turn = turn()
            phases = phases()
            moves = moves()
        }
    }
}

class TurnConfig(
    val moveLimit: Int = 1
)

class PhaseConfig

class Move(
    val move: String,
    val args: Array<dynamic>
)
