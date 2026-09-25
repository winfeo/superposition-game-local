package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation

fun cardImageResource(card: Card): Int = when (card.type) {
    CardType.PAULI -> when (card.axis) {
        AxisRotation.X -> if (card.actionRadius == 1) R.drawable.pauli_x else R.drawable.pauli_x3
        AxisRotation.Y -> if (card.actionRadius == 1) R.drawable.pauli_y else R.drawable.pauli_y3
        AxisRotation.Z -> if (card.actionRadius == 1) R.drawable.pauli_z else R.drawable.pauli_z3
        null -> R.drawable.identity
    }
    CardType.ROTATE -> when (card.axis) {
        AxisRotation.X -> R.drawable.rotate_x
        AxisRotation.Y -> R.drawable.rotate_y
        AxisRotation.Z -> R.drawable.rotate_z
        null -> R.drawable.identity
    }
    CardType.PHASE -> if (card.isForwardRotation == true) {
        R.drawable.phase_forward
    } else {
        R.drawable.phase_backward
    }
    CardType.HADAMARD -> if (card.actionRadius == 1) R.drawable.hadamard else R.drawable.hadamard_3
    CardType.SWAP -> R.drawable.swap
    CardType.QUANTUM_NOISE -> R.drawable.quantum_noise
    CardType.KRONECKER_MULTIPLICATION -> R.drawable.kronecker_multiplication
    CardType.MEASUREMENT -> R.drawable.measurement
    CardType.IDENTITY -> R.drawable.identity
    CardType.BARRIER -> R.drawable.barrier
    CardType.RESHUFFLE -> R.drawable.reshuffle
    CardType.QUANTUM_LUCKY -> R.drawable.quantum_lucky
}
