package io.github.winfeo.superpositiongame.android.domain.profile.usecase

import io.github.winfeo.superpositiongame.android.data.repository.GuestRepositoryImpl

class CreateGuestUseCase(
    private val guestRepository: GuestRepositoryImpl
) {
    suspend operator fun invoke(): Result<String> {
        return guestRepository.createGuest()
    }
}
