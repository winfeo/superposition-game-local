package io.github.winfeo.superpositiongame.android.ui.screen.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.repository.AuthRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.source.rest.AuthApi
import io.github.winfeo.superpositiongame.android.domain.auth.AuthRepository
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur
import io.ktor.client.HttpClient

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onSuccess()
            viewModel.resetSuccess()
        }
    }

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
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Заголовок
            Text(
                text = stringResource(R.string.auth_title),
                color = Color.White,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.auth_info),
                color = Color.White.copy(alpha = 0.45f),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(40.dp))

            //Почта
            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = {
                    Text(
                        stringResource(R.string.auth_email_field),
                        color = Color.White.copy(alpha = 0.45f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.45f)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.White.copy(alpha = 0.03f),
                    focusedBorderColor = Color(0xFF6C8CFF),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                    cursorColor = Color(0xFF6C8CFF)
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Пароль
            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = {
                    Text(
                        stringResource(R.string.auth_password_field),
                        color = Color.White.copy(alpha = 0.45f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.45f)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            painter = if (passwordVisible) {
                                painterResource(R.drawable.ic_eye)
                            } else {
                                painterResource(R.drawable.ic_eye_off)
                            },
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.45f)
                        )
                    }
                },
                visualTransformation =
                    if (passwordVisible) { VisualTransformation.None }
                    else { PasswordVisualTransformation() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.White.copy(alpha = 0.03f),
                    focusedBorderColor = Color(0xFF6C8CFF),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                    cursorColor = Color(0xFF6C8CFF)
                ),
                shape = RoundedCornerShape(16.dp)
            )

            //Ошибка
            state.error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = Color(0xFFFF6B6B),
                    style = MaterialTheme.typography.body2
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            //Кнопка войти
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF6C8CFF),
                                Color(0xFF9DB2FF)
                            )
                        )
                    )
                    .then(
                        if (state.isLoading) Modifier else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = viewModel::login,
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.auth_logIn_button),
                            color = Color.White,
                            style = MaterialTheme.typography.button,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Кнопка регистрации
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = viewModel::register,
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.auth_signUp_button),
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.button,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AuthScreenPrev() {
    AuthScreen(
        viewModel = AuthViewModel(
            repository = AuthRepositoryImpl(AuthApi(HttpClient()))
        ),
        onSuccess = {}
    )
}
