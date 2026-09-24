package io.github.winfeo.superpositiongame.android.data.source.socket

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.StompClient
import java.util.concurrent.ConcurrentHashMap

object StompSubscription {
    private val subscriptions = ConcurrentHashMap<String, Disposable>()

    fun subscribe(
        topic: String,
        client: StompClient,
        onMessage: (String) -> Unit
    ) {
        if (subscriptions.containsKey(topic)) return

        val topicDisposable = client.topic(topic)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ message ->
                Log.d("STOMP_RAW", "Получено сообщение: ${message.payload}")
                Log.d("STOMP_RAW", "Destination: ${message.stompHeaders}")
                onMessage(message.payload)
            }, { error ->
                Log.d("STOMP", "Ошибка подписки на топик $topic: ${error.message}")
            })

        val existingSubscription = subscriptions.putIfAbsent(
            topic,
            topicDisposable
        )
        if (existingSubscription != null) {
            topicDisposable.dispose()
        }
    }

    fun unsubscribe(topic: String) {
        subscriptions.remove(topic)?.dispose()
    }

    fun clear() {
        subscriptions.values.forEach { it.dispose() }
        subscriptions.clear()
    }
}
