package com.caseyjbrooks.boardgames.boardgameio

import com.caseyjbrooks.boardgames.applyAsDynamic
import com.caseyjbrooks.boardgames.asFunction
import com.caseyjbrooks.boardgames.obj

abstract class Boardgame<GameState>(
    val seed: String
) {

// Normal functions
//----------------------------------------------------------------------------------------------------------------------

    @JsName("_setup")
    abstract fun setup(): GameState

    @JsName("_endIf")
    abstract fun endIf(g: GameState, ctx: ContextData): dynamic

    @JsName("_enumerate")
    fun enumerate(g: GameState, ctx: ContextData): Array<Move> {
        return when(this@Boardgame) {
            is WithPhases<*> -> { (phases.unsafeCast<Phases<GameState>>()).enumerateMoves(g, ctx) }
            is WithMoves<*> -> { (moves.unsafeCast<Moves<GameState>>()).enumerateMoves(g, ctx) }
            else -> emptyList()
        }.toTypedArray()
    }

    @JsName("_turn")
    open fun turn(): TurnConfig {
        return TurnConfig()
    }

// Helper Functions
//----------------------------------------------------------------------------------------------------------------------

    abstract fun aggregateWinners(winnerMap: Map<String?, dynamic>, result: dynamic): Map<String?, dynamic>

    fun doObjectInit(): Boardgame<GameState> {
        return this.applyAsDynamic {
            setup = ::setup.asFunction()
            endIf = ::endIf.asFunction()
            ai = obj {
                enumerate = ::enumerate.asFunction()
            }
            turn = turn()

            when(this@Boardgame) {
                is WithPhases<*> -> { this.phases = phases.toDynamic() }
                is WithMoves<*> -> { this.moves = moves.toDynamic() }
            }
        }
    }
}

