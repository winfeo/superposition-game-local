package io.github.winfeo.superpositiongame.android.util

import android.content.Context
import android.media.MediaPlayer
import io.github.winfeo.superpositiongame.R

class GameMusicPlayer(
    context: Context
) {
    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null

    private fun getMusicPlayer(): MediaPlayer? {
        mediaPlayer?.let { return it }

        return MediaPlayer.create(
            appContext,
            R.raw.background_music
        )?.apply {
            isLooping = true
            setVolume(0.25f, 0.25f)
        }?.also {
            mediaPlayer = it
        }
    }

    fun play() {
        val player = getMusicPlayer()?: return
        if (!player.isPlaying) player.start()
    }

    fun pause() {
        val player = mediaPlayer?: return
        if (player.isPlaying) player.pause()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
