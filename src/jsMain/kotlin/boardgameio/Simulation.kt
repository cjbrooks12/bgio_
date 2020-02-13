package com.caseyjbrooks.boardgames.boardgameio

import com.caseyjbrooks.boardgames.obj
import kotlinx.coroutines.await
import kotlin.js.Promise

@JsModule("boardgame.io/ai")
@JsNonModule
external object BoardgameAis {
    class RandomBot(game: dynamic)
    class MCTSBot(game: dynamic)

    fun Step(client: BoardgameApp<*>, bot: dynamic): Promise<*>
    fun Simulate(config: dynamic): Promise<dynamic>
}

sealed class SimulationConfig(
    val runs: Int
) {
    abstract fun getBot(_game: dynamic, _enumerate: dynamic): dynamic
}

class RandomBot(
    runs: Int = 1000
) : SimulationConfig(runs) {
    override fun getBot(_game: dynamic, _enumerate: dynamic): dynamic {
        return BoardgameAis.RandomBot(obj {
            enumerate = _enumerate
        })
    }
}

class AiBot(
    runs: Int = 1000,
    val _seed: String = "test",
    val _iterations: Int = 200
) : SimulationConfig(runs) {
    override fun getBot(_game: dynamic, _enumerate: dynamic): dynamic {
        val bot = BoardgameAis.MCTSBot(obj {
            game = _game
            seed = _seed
            iterations = _iterations
            enumerate = _enumerate
        })
//        bot.asDynamic().setOpt("async", true);

        return bot
    }
}

suspend fun runSimulation(_game: Boardgame<*>, config: SimulationConfig): dynamic {
    val _enumerate = _game.asDynamic().ai.enumerate

    val client = BoardgameIo.Client(
        BoardgameApp(
            game = _game
        )
    )

    val bot = config.getBot(_game, _enumerate)

    val result = BoardgameAis.Simulate(obj {
        game = _game
        bots = bot
        state = client.currentState
        depth = 10000
    }).await()

    return result.state.ctx.gameover
}

suspend fun runSimulations(
    config: SimulationConfig,
    game: () -> Boardgame<*>
): dynamic {
    return (0 until config.runs)
        .map {
            val _game = game()
            _game to runSimulation(_game, config)
        }
        .fold(emptyMap<String?, dynamic>()) { acc, result ->
            val (_game, _winner) = result
            _game.aggregateWinners(acc, _winner)
        }
}
