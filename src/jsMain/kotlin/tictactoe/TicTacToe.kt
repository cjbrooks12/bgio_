package com.caseyjbrooks.boardgames.tictactoe

import com.caseyjbrooks.boardgames.boardgameio.Boardgame
import com.caseyjbrooks.boardgames.boardgameio.ContextData
import com.caseyjbrooks.boardgames.boardgameio.Phases
import com.caseyjbrooks.boardgames.boardgameio.RandomBot
import com.caseyjbrooks.boardgames.boardgameio.WithPhases
import com.caseyjbrooks.boardgames.boardgameio.runSimulations
import com.caseyjbrooks.boardgames.tictactoe.phases.PlayPhase

class TicTacToe : Boardgame<TicTacToeData>(
    seed = "test"
), WithPhases<TicTacToe.TicTacToePhases> {
    override val phases = TicTacToePhases()

    override fun setup(): TicTacToeData {
        return TicTacToeData()
    }

    override fun endIf(g: TicTacToeData, ctx: ContextData): TicTacToeWinner? {
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

        return winningPositions
            .firstOrNull { pos ->
                (g.cells[pos[0]] != null) &&
                        (g.cells[pos[0]] == g.cells[pos[1]]) &&
                        (g.cells[pos[0]] == g.cells[pos[2]])
            }?.let {
                TicTacToeWinner(winner = ctx.asDynamic().currentPlayer)
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

// Phases
//----------------------------------------------------------------------------------------------------------------------

    class TicTacToePhases : Phases<TicTacToeData>() {
        val play: PlayPhase by PlayPhase()
    }

// Simulate
//----------------------------------------------------------------------------------------------------------------------

    companion object {
        suspend fun simulate(): dynamic {
            return runSimulations(RandomBot()) { TicTacToe().doObjectInit() }
        }
    }
}

