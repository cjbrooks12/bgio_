package com.caseyjbrooks.boardgames.boardgameio

import com.caseyjbrooks.boardgames.asFunction
import com.caseyjbrooks.boardgames.toDynamic
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface WithMoves<M : Moves<*>> {
    @JsName("_moves")
    val moves: M
}

open class Moves<GameState> {
    val map = mutableMapOf<String, MoveFunction<GameState, *>>()
    fun toDynamic() = map.mapValues { it.value.doObjectInit() }.toDynamic()

    open fun enumerateMoves(g: GameState, ctx: ContextData): List<Move> {
        return emptyList()
    }
}

abstract class MoveFunction<GameState, Args> {

    lateinit var name: String

    abstract fun apply(g: GameState, ctx: ContextData, args: Args): GameState

    fun doObjectInit(): dynamic {
        return ::apply.asFunction()
    }

    operator fun provideDelegate(
        thisRef: Moves<GameState>,
        prop: KProperty<*>
    ): ReadOnlyProperty<Moves<GameState>, dynamic> {
        name = prop.name
        thisRef.map[name] = this

        return object : ReadOnlyProperty<Moves<GameState>, dynamic> {
            override fun getValue(thisRef: Moves<GameState>, property: KProperty<*>): dynamic {
                return this@MoveFunction
            }
        }
    }

    fun withArgs(vararg args: dynamic): Move {
        return Move(
            move = this.name,
            args = args
        )
    }
}

class Move(
    val move: String,
    val args: Array<dynamic>
)
