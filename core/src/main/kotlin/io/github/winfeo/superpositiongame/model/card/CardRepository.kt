package io.github.winfeo.superpositiongame.model.card

import io.github.winfeo.superpositiongame.model.card.description.CardDescription
import io.github.winfeo.superpositiongame.model.card.description.instance.hadamard.Hadamard
import io.github.winfeo.superpositiongame.model.card.description.instance.hadamard.Hadamard3
import io.github.winfeo.superpositiongame.model.card.description.instance.pauli.PauliX
import io.github.winfeo.superpositiongame.model.card.description.instance.pauli.PauliX3
import io.github.winfeo.superpositiongame.model.card.description.instance.pauli.PauliY
import io.github.winfeo.superpositiongame.model.card.description.instance.pauli.PauliY3
import io.github.winfeo.superpositiongame.model.card.description.instance.pauli.PauliZ
import io.github.winfeo.superpositiongame.model.card.description.instance.pauli.PauliZ3
import io.github.winfeo.superpositiongame.model.card.description.instance.phase.Phase
import io.github.winfeo.superpositiongame.model.card.description.instance.phase.PhaseBackwards
import io.github.winfeo.superpositiongame.model.card.description.instance.rotate.RotateX
import io.github.winfeo.superpositiongame.model.card.description.instance.rotate.RotateY
import io.github.winfeo.superpositiongame.model.card.description.instance.rotate.RotateZ
import io.github.winfeo.superpositiongame.model.card.description.instance.special.Identity
import io.github.winfeo.superpositiongame.model.card.description.instance.special.KroneckerMultiplication
import io.github.winfeo.superpositiongame.model.card.description.instance.special.Measurement
import io.github.winfeo.superpositiongame.model.card.description.instance.special.QuantumNoise
import io.github.winfeo.superpositiongame.model.card.description.instance.special.QuantumLucky
import io.github.winfeo.superpositiongame.model.card.description.instance.special.Reshuffle
import io.github.winfeo.superpositiongame.model.card.description.instance.special.Swap

object CardRepository {
    private val descriptions: Map<String, CardDescription> = mapOf(
        "pauli_x" to PauliX(),
        "pauli_y" to PauliY(),
        "pauli_z" to PauliZ(),
        "pauli_x3" to PauliX3(),
        "pauli_y3" to PauliY3(),
        "pauli_z3" to PauliZ3(),
        "rotate_x" to RotateX(),
        "rotate_y" to RotateY(),
        "rotate_z" to RotateZ(),
        "phase_s" to Phase(),
        "phase_s_backwards" to PhaseBackwards(),
        "hadamard_h" to Hadamard(),
        "hadamard_h3" to Hadamard3(),
        "swap" to Swap(),
        "quantum_noise" to QuantumNoise(),
        "quantum_lucky" to QuantumLucky(),
        "kronecker_multiplication" to KroneckerMultiplication(),
        "measurement" to Measurement(),
        "identity" to Identity(),
        "reshuffle" to Reshuffle()
    )

    fun getDescription(type: String): CardDescription? {
        return descriptions[type]
    }
}
