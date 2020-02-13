package com.caseyjbrooks.boardgames

import com.caseyjbrooks.boardgames.tictactoe.TicTacToe
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch {
        println(TicTacToe.simulate())
    }
}
