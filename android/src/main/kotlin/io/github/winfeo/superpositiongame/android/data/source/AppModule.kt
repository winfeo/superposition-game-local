package io.github.winfeo.superpositiongame.android.data.source

import android.content.Context
import io.github.winfeo.superpositiongame.android.data.repository.AccountRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.AiGameRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.AuthRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.CardsRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.GameHistoryRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.GameRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.GuestRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.InvitationRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.LobbyRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.PingRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.ProfileRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.UserRepository
import io.github.winfeo.superpositiongame.android.data.source.local.GuestSessionManager
import io.github.winfeo.superpositiongame.android.data.source.rest.AuthApi
import io.github.winfeo.superpositiongame.android.data.source.rest.AiGameApi
import io.github.winfeo.superpositiongame.android.data.source.rest.GameHistoryApi
import io.github.winfeo.superpositiongame.android.data.source.rest.GuestApi
import io.github.winfeo.superpositiongame.android.data.source.local.TokenManager
import io.github.winfeo.superpositiongame.android.data.source.rest.UserApi
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.data.source.local.SettingsManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AppModule {
    private lateinit var appContext: Context
    private val client by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            expectSuccess = true
            defaultRequest {
                UserSession.token.value?.let { token ->
                    header("Authorization", "Bearer $token")
                }
            }
        }
    }

    fun init(context: Context) {
        appContext = context
    }

    val settingsManager by lazy { SettingsManager(appContext) }
    val tokenManager by lazy { TokenManager(appContext) }
    val guestSessionManager by lazy { GuestSessionManager(appContext) }
    val authApi by lazy { AuthApi(client) }
    val authRepository by lazy { AuthRepositoryImpl(authApi) }
    val aiGameApi by lazy { AiGameApi(client) }
    val aiGameRepository by lazy { AiGameRepositoryImpl(aiGameApi) }
    val gameHistoryApi by lazy { GameHistoryApi(client) }
    val gameHistoryRepository by lazy { GameHistoryRepositoryImpl(gameHistoryApi) }
    val guestApi by lazy { GuestApi(client) }
    val guestRepository by lazy { GuestRepositoryImpl(guestApi) }
    val userApi by lazy { UserApi(client) }
    val userRepository by lazy { UserRepository(userApi) }
    val invitationRepository by lazy { InvitationRepositoryImpl() }
    val cardsRepository by lazy { CardsRepositoryImpl() }
    val lobbyRepository by lazy { LobbyRepositoryImpl() }
    val profileRepository by lazy { ProfileRepositoryImpl(userApi, gameHistoryApi) }
    val accountRepository by lazy { AccountRepositoryImpl(userApi) }
    val gameRepository by lazy { GameRepositoryImpl() }
    val pingRepository by lazy { PingRepositoryImpl() }
}
