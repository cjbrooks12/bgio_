package com.caseyjbrooks.boardgames.boardgameio

@JsModule("boardgame.io/client")
@JsNonModule
external object BoardgameIo {
    fun <Game : Boardgame<*>> Client(game: BoardgameApp<Game>): BoardgameApp<Game>
}

class BoardgameApp<Game : Boardgame<*>>(
    val game: Game
)

val <Game : Boardgame<*>> BoardgameApp<Game>.ai: Game get() = asDynamic().ai

val BoardgameApp<*>.currentState: dynamic get() = asDynamic().store.getState()
val BoardgameApp<*>.currentContext: dynamic get() = currentState.ctx
val BoardgameApp<*>.currentGameData: dynamic get() = currentState.G
val BoardgameApp<*>.moves: dynamic get() = asDynamic().moves
