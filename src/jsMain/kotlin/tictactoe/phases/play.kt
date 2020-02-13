package com.caseyjbrooks.boardgames.tictactoe.phases

import com.caseyjbrooks.boardgames.boardgameio.ContextData
import com.caseyjbrooks.boardgames.boardgameio.Move
import com.caseyjbrooks.boardgames.boardgameio.MoveFunction
import com.caseyjbrooks.boardgames.boardgameio.Moves
import com.caseyjbrooks.boardgames.boardgameio.PhaseConfig
import com.caseyjbrooks.boardgames.boardgameio.WithMoves
import com.caseyjbrooks.boardgames.tictactoe.TicTacToeData

class PlayPhase : PhaseConfig<TicTacToeData>(start = true), WithMoves<PlayPhaseMoves> {
    override val moves = PlayPhaseMoves()
}

class PlayPhaseMoves : Moves<TicTacToeData>() {
    val clickCell: ClickCellMove by ClickCellMove()

    override fun enumerateMoves(g: TicTacToeData, ctx: ContextData): List<Move> {
        return (0 until 9)
            .filter { g.cells[it] === null }
            .map {
                clickCell.enumerateMove(it)
            }
    }
}

class ClickCellMove : MoveFunction<TicTacToeData, Int>() {
    fun enumerateMove(id: Int) = withArgs(id)

    override fun apply(g: TicTacToeData, ctx: ContextData, args: Int) = g.apply {
        cells[args] = ctx.asDynamic().currentPlayer
    }
}
