package io.github.winfeo.superpositiongame.android.ui.dialog.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.R

@Composable
fun GameMenuDialog(
    onResume: () -> Unit,
    onRules: () -> Unit,
    onSettings: () -> Unit,
    onSurrender: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
        ) {

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(32.dp)
                    .background(
                        Color(0xFF6C8CFF).copy(alpha = 0.12f),
                        RoundedCornerShape(24.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1A1526),
                                Color(0xFF120E1C)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.08f),
                        RoundedCornerShape(28.dp)
                    )
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.dialog_menu_title).uppercase(),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.06f))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                //Правила
                MenuButton(
                    title = stringResource(R.string.dialog_menu_rules),
                    subtitle = stringResource(R.string.dialog_menu_rules_detailed),
                    accent = Color(0xFFA98CFF),
                    onClick = onRules
                )

                Spacer(modifier = Modifier.height(12.dp))

                //Настройки
//                MenuButton(
//                    title = stringResource(R.string.dialog_menu_settings),
//                    subtitle = stringResource(R.string.dialog_menu_settings_detailed),
//                    accent = Color(0xFF8CE6FF),
//                    onClick = onSettings
//                )

                Spacer(modifier = Modifier.height(12.dp))

                //Продолжить
                MenuButton(
                    title = stringResource(R.string.dialog_menu_continue),
                    subtitle = stringResource(R.string.dialog_menu_continue_detailed),
                    accent = Color(0xFF6C8CFF),
                    onClick = onResume
                )

                Spacer(modifier = Modifier.height(48.dp))

                //Сдаться
                MenuSurrenderButton(
                    title = stringResource(R.string.dialog_menu_surrender),
                    subtitle = stringResource(R.string.dialog_menu_surrender_detailed),
                    onClick = onSurrender
                )

            }
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    subtitle: String,
    accent: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        accent.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.03f)
                    )
                )
            )
            .border(
                1.dp,
                accent.copy(alpha = 0.18f),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {

        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun MenuSurrenderButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFF6B6B).copy(alpha = 0.22f),
                        Color.White.copy(alpha = 0.03f)
                    )
                )
            )
            .border(
                1.dp,
                Color(0xFFFF6B6B).copy(alpha = 0.25f),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {

        Column {
            Text(
                text = title,
                color = Color(0xFFFF9090),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 12.sp
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GameMenuDialogPrev() {
    GameMenuDialog(
        onResume = {},
        onRules = {},
        onSettings = {},
        onSurrender = {},
        onDismiss = {}
    )
}
