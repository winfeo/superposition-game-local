package io.github.winfeo.superpositiongame.model.game

enum class GamePhase {
    WAITING_FOR_SECOND_PLAYER, ///TODO Добавить в начале при создании игры
//    ANIMATED //TODO состояние для анимаций реализовать?
    GAME_SETUP,
    DEAL_CARDS,
    MOVE_START,
    MOVE_FINISH,
    GAME_FINISHED
}
