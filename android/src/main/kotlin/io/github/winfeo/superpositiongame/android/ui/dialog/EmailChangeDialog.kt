package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.winfeo.superpositiongame.R

@Composable
fun EmailChangeDialog(
    currentEmail: String,
    errorMessage: String?,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var email by remember { mutableStateOf(currentEmail) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1B2E),
                            Color(0xFF11121F)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF6C8CFF).copy(alpha = 0.12f),
                                Color.Transparent
                            ),
                            radius = 700f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.dialog_settings_change_email),
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 16.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        val color =
                            if (errorMessage != null) Color(0xFFFF6B6B)
                            else Color.White.copy(alpha = 0.45f)
                        Text(
                            text = stringResource(R.string.dialog_settings_email),
                            color = color
                        )
                    },
                    isError = errorMessage != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        backgroundColor = Color.White.copy(alpha = 0.03f),
                        focusedBorderColor = Color(0xFF6C8CFF),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                        cursorColor = Color(0xFF6C8CFF),
                        errorBorderColor = Color(0xFFFF6B6B),
                        errorLabelColor = Color(0xFFFF6B6B)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFFF6B6B),
                        style = MaterialTheme.typography.caption
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    EmailDialogButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.dialog_settings_cancel),
                        isPrimary = false,
                        onClick = onDismiss
                    )

                    EmailDialogButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.dialog_settings_save),
                        isPrimary = true,
                        onClick = { onConfirm(email.trim()) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmailDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .height(52.dp)
            .then(
                if (isPrimary) {
                    Modifier.background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4B5DFF),
                                Color(0xFF6C8CFF)
                            )
                        ),
                        shape = shape
                    )
                } else {
                    Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.04f),
                            shape = shape
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.08f),
                            shape = shape
                        )
                }
            )
            .clip(shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isPrimary) Color.White else Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.body2
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun EmailChangeDialogPrev() {
    EmailChangeDialog(
        currentEmail = "123",
        errorMessage = null,
        onConfirm = {},
        onDismiss = {}
    )
}
