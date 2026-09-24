package io.github.winfeo.superpositiongame.ui.actor

//Состояния кубита (UI отображение)
enum class SlotActorStates {
    NO_ACTION, //Игрок никак не взаимодействует с кубитом
    HOVERED_CAN_PLACE, //Игрок хочет использовать карту на кубит: можно
    HOVERED_CANT_PLACE, //Игрок хочет использовать карту на кубит: нельзя
    REQUIRED_DICE_STATE //Кубит находится в нужном состоянии
    //FROZEN_ONE_MOVE ///TODO добавить состояние при котором карту нельзя изменить на один ход?
}
