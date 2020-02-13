package com.caseyjbrooks.babbage

import com.caseyjbrooks.babbage.game.TicTacToe
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch {
        println(runSimulations(RandomBots()) { TicTacToe().doObjectInit() } as? Any)
    }
}
