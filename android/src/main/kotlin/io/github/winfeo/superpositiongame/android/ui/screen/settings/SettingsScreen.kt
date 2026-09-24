package io.github.winfeo.superpositiongame.android.ui.screen.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.ui.dialog.DeleteAccountDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.EmailChangeDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.LogoutDialog
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onReplayOnboarding: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName

    var showEmailDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0812))
    ) {
        BackgroundBlur()

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.015f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SettingsHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(16.dp))

            //Звук
            val soundTitle = stringResource(R.string.settings_sound_section)
            SettingsSection(title = soundTitle) {
                SettingsSwitchItem(
                    icon = painterResource(R.drawable.ic_note), //TODO поменять
                    title = stringResource(R.string.settings_music),
                    checked = state.isMusicEnabled,
                    onCheckedChange = { viewModel.toggleMusic(it) }
                )
                SettingsSwitchItem(
                    icon = painterResource(R.drawable.ic_bell),
                    title = stringResource(R.string.settings_invite_sound),
                    checked = state.isInviteSoundEnabled,
                    onCheckedChange = { viewModel.toggleInviteSound(it) }
                )
            }

            //Обучение
            val onboardingTitle = stringResource(R.string.settings_onboarding_section)
            SettingsSection(title = onboardingTitle) {
                SettingsActionItem(
                    icon = ImageVector.vectorResource(R.drawable.ic_lightbulb),
                    title = stringResource(R.string.settings_onboarding_replay),
                    onClick = onReplayOnboarding
                )
            }

            if (state.isAuthorized) {
                val accountTitle = stringResource(R.string.settings_account_section)
                SettingsSection(title = accountTitle) {
                    SettingsActionItem(
                        icon = Icons.Default.Email,
                        title = stringResource(R.string.settings_change_email),
                        onClick = { showEmailDialog = true }
                    )

                    SettingsDivider()

                    SettingsActionItem(
                        icon = Icons.Default.ExitToApp,
                        title = stringResource(R.string.settings_logout),
                        onClick = { showLogoutDialog = true },
                        titleColor = Color(0xFFFFB08A),
                        iconTint = Color(0xFFFFB08A)
                    )

                    SettingsDivider()

                    SettingsActionItem(
                        icon = Icons.Default.Delete,
                        title = stringResource(R.string.settings_delete_account),
                        onClick = { showDeleteDialog = true },
                        titleColor = Color(0xFFFF6B6B),
                        iconTint = Color(0xFFFF6B6B)
                    )
                }
            }

            //О приложении
            val infoTitle = stringResource(R.string.settings_info_section)
            SettingsSection(title = infoTitle) {
                SettingsInfoItem(
                    icon = ImageVector.vectorResource(R.drawable.ic_info_outline),
                    title = stringResource(R.string.settings_version),
                    value = versionName?: "1.0.0"
                )
            }
        }
    }

    //Смена почты
    if (state.isAuthorized && showEmailDialog) {
        val textSuccessful = stringResource(R.string.settings_toast_email_successful)
        EmailChangeDialog(
            currentEmail = UserSession.currentUser.value?.email ?: "",
            onConfirm = { email ->
                viewModel.changeEmail(
                    newEmail = email,
                    onSuccess = {
                        showEmailDialog = false
                        emailError = null
                        Toast.makeText(context, textSuccessful, Toast.LENGTH_SHORT).show()
                    },
                    onError = { error ->
                        emailError = error
                    }
                )
            },
            onDismiss = { showEmailDialog = false },
            errorMessage = emailError
        )
    }

    //Выход из аккаунта
    if (state.isAuthorized && showLogoutDialog) {
        val textSuccessful = stringResource(R.string.settings_toast_logout_successful)
        LogoutDialog(
            onConfirm = {
                viewModel.logout(
                    onSuccess = {
                        showLogoutDialog = false
                        Toast.makeText(context, textSuccessful, Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                )
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    //Удаление аккаунта
    if (state.isAuthorized && showDeleteDialog) {
        val textSuccessful = stringResource(R.string.settings_toast_delete_successful)
        DeleteAccountDialog(
            onConfirm = {
                viewModel.deleteAccount(
                    onSuccess = {
                        showDeleteDialog = false
                        Toast.makeText(context, textSuccessful, Toast.LENGTH_SHORT).show()
                        onBack()
                    },
                    onError = { error ->
                        emailError = error
                    }
                )
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
fun SettingsHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2B36A6).copy(alpha = 0.45f),
                        Color(0xFF15162A).copy(alpha = 0.35f)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.settings_title),
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.h6
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.15f),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.65f),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White.copy(alpha = 0.04f),
            elevation = 0.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: Painter,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.9f),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF6C8CFF),
                checkedTrackColor = Color(0xFF6C8CFF).copy(alpha = 0.5f),
                uncheckedThumbColor = Color.White.copy(alpha = 0.5f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
fun SettingsActionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    titleColor: Color = Color.White.copy(alpha = 0.9f),
    iconTint: Color = Color.White.copy(alpha = 0.7f)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = titleColor,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
//        Icon(
//            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//            contentDescription = null,
//            tint = Color.White.copy(alpha = 0.4f),
//            modifier = Modifier.size(20.dp)
//        )
    }
}

@Composable
private fun SettingsDivider() {
    Divider(
        modifier = Modifier.padding(start = 56.dp, end = 16.dp),
        color = Color.White.copy(alpha = 0.07f),
        thickness = 1.dp
    )
}

@Composable
fun SettingsInfoItem(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.9f),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = Color.White.copy(alpha = 0.5f),
            style = MaterialTheme.typography.body2
        )
    }
}
