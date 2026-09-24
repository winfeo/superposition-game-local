package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.winfeo.superpositiongame.R

@Composable
fun EditNicknameDialog(
    currentNickname: String,
    errorMessage: String?,
    onConfirm: (newNickname: String) -> Unit,
    onDismiss: () -> Unit
) {
    var newNickname by remember { mutableStateOf(currentNickname) }

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
                    text = stringResource(R.string.dialog_edit_nickname_title),
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 16.dp)
                )

                OutlinedTextField(
                    value = newNickname,
                    onValueChange = { newNickname = it },
                    label = {
                        Text(
                            text = stringResource(R.string.dialog_edit_nickname_new),
                            color = Color.White.copy(alpha = 0.45f)
                        )
                    },
                    isError = errorMessage != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                    DialogButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.dialog_edit_nickname_cancel),
                        isPrimary = false,
                        onClick = onDismiss
                    )

                    DialogButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.dialog_edit_nickname_save),
                        isPrimary = true,
                        onClick = {
                            onConfirm(newNickname.trim())
//                            if (newNickname.isBlank()) {
//
//                            } else {
//                                onConfirm(newNickname.trim())
//                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun EditNicknameDialogPrev() {
    EditNicknameDialog(
        currentNickname = "Winfeo",
        errorMessage = null,
        onConfirm = {},
        onDismiss = {}
    )
}
