package io.github.winfeo.superpositiongame.model.card

import io.github.winfeo.superpositiongame.model.card.description.CardDescription

//Модель для использования и передачи по сети
data class Card(
    val id: String,
    val textureId: String?,
    val description: CardDescription
) {
    val type get() = description.type
    val axis get() = description.axis
    //val angle get() = description.angle
    val actionRadius get() = description.actionRadius
    val requiredSpecialSlot get() = description.requiredSpecialSlot
    val isForwardRotation get() = description.isForwardRotation
}
