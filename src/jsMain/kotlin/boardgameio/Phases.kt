package com.caseyjbrooks.boardgames.boardgameio

import com.caseyjbrooks.boardgames.applyAsDynamic
import com.caseyjbrooks.boardgames.asFunction
import com.caseyjbrooks.boardgames.toDynamic
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface WithPhases<P : Phases<*>> {
    @JsName("_phases")
    val phases: P
}

open class Phases<GameState> {
    val map = mutableMapOf<String, PhaseConfig<GameState>>()
    fun toDynamic() = map.mapValues { it.value.doObjectInit() }.toDynamic()

    open fun enumerateMoves(g: GameState, ctx: ContextData): List<Move> {
        return map
            .values
            .filter { it.name == ctx.asDynamic().phase} // only enumerate the moves for the current phase
            .filterIsInstance<WithMoves<*>>()
            .flatMap { (it.moves.unsafeCast<Moves<GameState>>()).enumerateMoves(g, ctx) }
    }
}

open class PhaseConfig<GameState>(
    var start: Boolean = false
) {

    lateinit var name: String

    var next: String? = null

    @JsName("_onBegin")
    open fun onBegin(g: GameState, ctx: ContextData): GameState {
        return g
    }

    @JsName("_onEnd")
    open fun onEnd(g: GameState, ctx: ContextData): GameState {
        return g
    }

    @JsName("_endIf")
    open fun endIf(g: GameState, ctx: ContextData): Boolean {
        return false
    }

    fun doObjectInit(): PhaseConfig<GameState> {
        return this.applyAsDynamic {
            endIf = ::endIf.asFunction()
            onBegin = ::onBegin.asFunction()
            onEnd = ::onEnd.asFunction()

            if (this@PhaseConfig is WithMoves<*>) {
                this.moves = moves.toDynamic()
            }
        }
    }

    operator fun provideDelegate(
        thisRef: Phases<GameState>,
        prop: KProperty<*>
    ): ReadOnlyProperty<Phases<GameState>, dynamic> {
        name = prop.name
        thisRef.map[name] = this

        return object : ReadOnlyProperty<Phases<GameState>, dynamic> {
            override fun getValue(thisRef: Phases<GameState>, property: KProperty<*>): dynamic {
                return this@PhaseConfig
            }
        }
    }
}
