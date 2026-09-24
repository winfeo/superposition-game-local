package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import io.github.winfeo.superpositiongame.Main

class GameFragment: AndroidFragmentApplication() {
    lateinit var game: Main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val config = AndroidApplicationConfiguration().apply {
            useImmersiveMode = true
            r = 8
            g = 8
            b = 8
            a = 8
            useGL30 = false
        }

        val view = initializeForView(game, config)

        //Настройка GLSurfaceView (для прозрачности)
        if (view is SurfaceView) {
            val sv = view as SurfaceView?
            sv!!.holder.setFormat(PixelFormat.TRANSLUCENT)
            sv.setZOrderOnTop(true)
        }
        view.setBackgroundColor(android.graphics.Color.TRANSPARENT)

        return view
    }

    override fun exit() {
        requireActivity().finish()
    }
}
