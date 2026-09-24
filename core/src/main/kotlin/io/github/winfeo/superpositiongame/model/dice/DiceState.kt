package io.github.winfeo.superpositiongame.model.dice

//Возможные состояния кубитов
enum class DiceState(
    val stateName: String,
    val textureId: String
) {
    ZERO(
        stateName = "0",
        textureId = "zero",
    ),
    ONE(
        stateName = "1",
        textureId = "one",
    ),
    PLUS(
        stateName = "+",
        textureId = "plus",
    ),
    MINUS(
        stateName = "-",
        textureId = "minus",
    ),
    I(
        stateName = "I",
        textureId = "i_plus",
    ),
    I_MINUS(
        stateName = "-I",
        textureId = "i_minus",
    )
}
