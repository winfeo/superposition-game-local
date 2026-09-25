package io.github.winfeo.superpositiongame.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.ui.nav.Navigation
import io.github.winfeo.superpositiongame.android.ui.theme.SuperpositionGameTheme

class AndroidLauncher : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppModule.init(applicationContext)
        setContent {
            SuperpositionGameTheme(darkTheme = true) {
                Navigation()
            }
        }
    }
}
