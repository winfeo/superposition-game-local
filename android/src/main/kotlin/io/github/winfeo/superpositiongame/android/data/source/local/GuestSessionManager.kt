package io.github.winfeo.superpositiongame.android.data.source.local

import android.content.Context
import androidx.core.content.edit

class GuestSessionManager(
    context: Context
) {
    private companion object {
        const val PREFS_NAME = "guest_session_prefs"
        const val KEY_GUEST_ID = "guest_id"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveGuestId(guestId: String) {
        prefs.edit { putString(KEY_GUEST_ID, guestId) }
    }

    fun getGuestId(): String? {
        return prefs.getString(KEY_GUEST_ID, null)?.takeIf { it.isNotBlank() }
    }

    fun clearGuestId() {
        prefs.edit { remove(KEY_GUEST_ID) }
    }
}
