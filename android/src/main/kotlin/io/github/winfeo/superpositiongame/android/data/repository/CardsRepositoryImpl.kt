package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.domain.library.Card
import io.github.winfeo.superpositiongame.android.domain.library.CardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CardsRepositoryImpl: CardsRepository {
    override fun getAllCards(): Flow<List<Card>> = flow {
        emit(
            listOf(
                Card(
                    id = "pauli_x",
                    imageRes = R.drawable.pauli_x
                ),
                Card(
                    id = "pauli_x3",
                    imageRes = R.drawable.pauli_x3
                ),
                Card(
                    id = "pauli_y",
                    imageRes = R.drawable.pauli_y
                ),
                Card(
                    id = "pauli_y3",
                    imageRes = R.drawable.pauli_y3
                ),
                Card(
                    id = "pauli_z",
                    imageRes = R.drawable.pauli_z
                ),
                Card(
                    id = "pauli_z3",
                    imageRes = R.drawable.pauli_z3
                ),
                Card(
                    id = "rotate_x",
                    imageRes = R.drawable.rotate_x
                ),
                Card(
                    id = "rotate_y",
                    imageRes = R.drawable.rotate_y
                ),
                Card(
                    id = "rotate_z",
                    imageRes = R.drawable.rotate_z
                ),
                Card(
                    id = "phase_forward",
                    imageRes = R.drawable.phase_forward
                ),
                Card(
                    id = "phase_backward",
                    imageRes = R.drawable.phase_backward
                ),
                Card(
                    id = "hadamard",
                    imageRes = R.drawable.hadamard
                ),
                Card(
                    id = "hadamard_3",
                    imageRes = R.drawable.hadamard_3
                ),
                Card(
                    id = "swap",
                    imageRes = R.drawable.swap
                ),
                Card(
                    id = "quantum_noise",
                    imageRes = R.drawable.quantum_noise
                ),
                Card(
                    id = "kronecker_multiplication",
                    imageRes = R.drawable.kronecker_multiplication
                ),
                Card(
                    id = "measurement",
                    imageRes = R.drawable.measurement
                ),
                Card(
                    id = "identity",
                    imageRes = R.drawable.identity
                ),
                Card(
                    id = "reshuffle",
                    imageRes = R.drawable.reshuffle
                ),
                Card(
                    id = "quantum_lucky",
                    imageRes = R.drawable.quantum_lucky
                ),
            )
        )
    }
}
