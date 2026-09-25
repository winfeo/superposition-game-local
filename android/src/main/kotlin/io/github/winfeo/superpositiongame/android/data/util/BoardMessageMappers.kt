package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.domain.game.model.BoardFieldCard
import io.github.winfeo.superpositiongame.android.domain.game.model.BoardGameState
import io.github.winfeo.superpositiongame.android.domain.game.model.BoardPlayer
import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.toPlayer(): BoardPlayer? {
    val id = optInt("id", -1)
    return if (id < 0) null else BoardPlayer(id, optString("name", "player$id"))
}

fun JSONArray.toPlayers(): List<BoardPlayer> {
    return (0 until length()).mapNotNull { optJSONObject(it)?.toPlayer() }.sortedBy { it.id }
}

fun JSONArray.toIntList(): List<Int> {
    return (0 until length()).mapNotNull { (opt(it) as? Number)?.toInt() }
}

private fun JSONArray.toStringList(): List<String> {
    return (0 until length()).mapNotNull { opt(it) as? String }
}

fun JSONObject.toGameState(): BoardGameState? {
    val ids = optJSONArray("players")?.toIntList()?: return null
    val rawRegisters = optJSONArray("registers")?: return null
    val registers = (0 until rawRegisters.length()).map { index ->
        rawRegisters.optJSONArray(index)?.toStringList()?: return null
    }
    val target = optJSONArray("targetRegister")?.toStringList()?: return null
    val turn = optInt("currentPlayer", -1)

    if (ids.size != registers.size || turn !in ids.indices || registers.any { it.size != target.size }) return null
    val rawCards = optJSONArray("cardsOnField")?: JSONArray()

    val cards = (0 until rawCards.length()).mapNotNull { index ->
        val card = rawCards.optJSONObject(index) ?: return@mapNotNull null
        val player = card.optInt("player", -1)
        val cubit = card.optInt("cubit", -1)
        if (player !in registers.indices || cubit !in registers[player].indices) return@mapNotNull null
        BoardFieldCard(player, cubit, card.optString("card"))
    }

    return BoardGameState(ids, registers, target, turn, optInt("playedCards"), cards)
}
