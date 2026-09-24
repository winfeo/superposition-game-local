package io.github.winfeo.superpositiongame.model.card

object CardFactory {
    private val typeRepository: Map<CardTypeNew, String> = mapOf(
        CardTypeNew.PAULI_X to "pauli_x",
        CardTypeNew.PAULI_X_3 to "pauli_x3",
        CardTypeNew.PAULI_Y to "pauli_y",
        CardTypeNew.PAULI_Y_3 to "pauli_y3",
        CardTypeNew.PAULI_Z to "pauli_z",
        CardTypeNew.PAULI_Z_3 to "pauli_z3",
        CardTypeNew.ROTATE_X to "rotate_x",
        CardTypeNew.ROTATE_Y to "rotate_y",
        CardTypeNew.ROTATE_Z to "rotate_z",
        CardTypeNew.HADAMARD to "hadamard_h",
        CardTypeNew.HADAMARD_3 to "hadamard_h3",
        CardTypeNew.PHASE_FORWARD to "phase_s",
        CardTypeNew.PHASE_BACKWARD to "phase_s_backwards",
        CardTypeNew.IDENTITY to "identity",
        CardTypeNew.MEASUREMENT to "measurement",
        CardTypeNew.KRONECKER_MULTIPLICATION to "kronecker_multiplication",
        CardTypeNew.QUANTUM_NOISE to "quantum_noise",
        CardTypeNew.QUANTUM_LUCKY to "quantum_lucky",
        CardTypeNew.SWAP to "swap",
        CardTypeNew.RESHUFFLE to "reshuffle"
    )
//    private val cardRepository: List<String> = listOf(
//        "pauli_x",
//        "pauli_y",
//        "pauli_z",
//        "pauli_x3",
//        "pauli_y3",
//        "pauli_z3",
//        "rotate_x",
//        "rotate_y",
//        "rotate_z",
//        "phase_s",
//        "phase_s_backwards",
//        "hadamard_h",
//        "hadamard_h3",
//        "swap",
//        "quantum_noise",
//        "kronecker_multiplication",
//        "measurement",
//        "identity",
//        "reshuffle"
//    )
//    private var idCounter = 0
//
//    fun createRandomCard(): Card {
//        val textureName = CardsAtlasManager.getRandomCardId()
//        return buildCard(textureName)
//    }
//
//    fun createCardFromName(cardName: String): Card = buildCard(cardName)
//
//    private fun buildCard(textureName: String): Card {
//        val description = CardRepository.getDescription(textureName)?: throw (IllegalStateException("Не удалось найти тип карты c id: $textureName"))
//        return Card(
//            id = "${textureName}_${idCounter++}",
//            textureId = textureName,
//            description = description
//        )
//    }

    fun buildCardFromType(
        type: CardTypeNew,
        id: String
    ): Card {
        val textureName = typeRepository[type]?: throw (IllegalStateException("Не удалось найти карту c типом: $type"))
        val description = CardRepository.getDescription(textureName)?: throw (IllegalStateException("Не удалось найти тип карты c id: $textureName"))
        return Card(
            id = id,
            textureId = textureName,
            description = description
        )
    }
//
//    fun getRandomCardName(): String = cardRepository.random()
//
////    fun createEmptyCard(): Card { ///TODO переделать на просто пустое место, а не пустую карту?
////        return Card(
////            id = "empty",
////            textureId = null,
////            description = cardRepository["empty"]!!
////        )
////    }
//
////    fun createDraggableCardForOpponentScript(): Card {
////        val textureName = CardsAtlasManager.getRandomCardId()
////
////        val type = CardType.entries.find {
////            it.textureId == textureName
////        }?: throw (IllegalStateException("Не удалось найти тип карты c id: $textureName"))
////
////        if (!type.cardComponent.canDrag) {
////            return createDraggableCardForOpponentScript()
////        }
////
////        return Card(
////            id = "${textureName}_${idCounter++}",
////            type = type
////        )
////
////    }

}
