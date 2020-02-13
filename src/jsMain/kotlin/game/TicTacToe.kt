package com.caseyjbrooks.babbage.game

import com.caseyjbrooks.babbage.Boardgame
import com.caseyjbrooks.babbage.Move
import com.caseyjbrooks.babbage.obj

class TicTacToe : Boardgame(
    seed = "test"
) {

    override fun setup(): TicTacToeData {
        return TicTacToeData()
    }

    override fun endIf(g: TicTacToeData, ctx: dynamic): dynamic {
        val winningPositions = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        var result: dynamic = null

        winningPositions.forEach { pos ->
            if ((g.cells[pos[0]] != null) &&
                (g.cells[pos[0]] == g.cells[pos[1]]) &&
                (g.cells[pos[0]] == g.cells[pos[2]])
            ) {
                result =
                    TicTacToeWinner(winner = ctx.currentPlayer)
            }
        }

        return result
    }

    override fun enumerate(g: TicTacToeData, ctx: dynamic): List<Move> {
        return (0 until 9)
            .filter { g.cells[it] === null }
            .map {
                Move(
                    move = "clickCell",
                    args = arrayOf(it)
                )
            }
    }

    override fun aggregateWinners(winnerMap: Map<String?, dynamic>, result: TicTacToeWinner?): Map<String?, dynamic> {
        val map = winnerMap.toMutableMap()

        val key = result?.winner

        if (!map.containsKey(key)) {
            map[key] = 0
        }
        map[key] = map[key]!! + 1

        return map
    }

// Moves
//----------------------------------------------------------------------------------------------------------------------

    override fun moves(): dynamic {
        return obj {
            this.clickCell =
                { g: TicTacToeData, ctx: dynamic, id: dynamic ->
                    clickCellMove(
                        g,
                        ctx,
                        id
                    )
                }
        }
    }

    private fun clickCellMove(g: TicTacToeData, ctx: dynamic, id: dynamic): TicTacToeData {
        g.cells[id] = ctx.currentPlayer
        return g
    }
}
