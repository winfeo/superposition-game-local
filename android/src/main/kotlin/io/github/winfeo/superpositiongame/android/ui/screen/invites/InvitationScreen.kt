package io.github.winfeo.superpositiongame.android.ui.screen.invites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur
import io.github.winfeo.superpositiongame.android.util.TimeFormatter

@Composable
fun InvitesScreen(
    viewModel: InvitationViewModel,
    onReturnToLobby: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundBlur()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            InvitesHeader(onReturnToLobby = onReturnToLobby)

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = Color(0xFF6C8CFF)
                        )
                    }
                }
                state.invitations.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.invites_empty_list),
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
                else -> InvitesList(
                    invitations = state.invitations,
                    onAccept = { invitation ->
                        viewModel.acceptInvitation(invitation)
                    },
                    onReject = { invitation ->
                        viewModel.rejectInvitation(invitation)
                    }
                )
            }
        }
    }
}

@Composable
fun InvitesHeader(
    onReturnToLobby: () -> Unit
) {
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
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6C8CFF).copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        radius = 900f
                    )
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

//            Box(
//                modifier = Modifier
//                    .size(42.dp)
//                    .clickable { onReturnToLobby() },
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_arrow_back),
//                    contentDescription = null,
//                    tint = Color.White.copy(alpha = 0.9f)
//                )
//            }

            IconButton(
                onClick = { onReturnToLobby() },
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.invites_title),
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
fun InvitesList(
    invitations: List<Invitation>,
    onAccept: (Invitation) -> Unit,
    onReject: (Invitation) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(invitations) { invite ->
            InviteCard(
                invitation = invite,
                onAccept = onAccept,
                onReject = onReject
            )
        }
    }
}


@Composable
fun InviteCard(
    invitation: Invitation,
    onAccept: (Invitation) -> Unit,
    onReject: (Invitation) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.06f),
                        Color.White.copy(alpha = 0.03f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val senderName = invitation.senderNickname?: invitation.senderId
                Text(
                    text = senderName.take(9),
                    color = Color.White.copy(alpha = 0.92f),
                    style = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = TimeFormatter.formatTime(invitation.sendTime),
                    color = Color.White.copy(alpha = 0.45f),
                    style = MaterialTheme.typography.caption
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    icon = R.drawable.ic_reject,
                    tint = Color(0xFFE07A7A),
                    onClick = { onReject(invitation) }
                )

                IconButton(
                    icon = R.drawable.ic_accept,
                    tint = Color(0xFF7ED9A3),
                    onClick = { onAccept(invitation) }
                )
            }
        }

    }
}

@Composable
fun IconButton(
    icon: Int,
    tint: Color,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(10.dp)

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = Color.White.copy(alpha = 0.05f),
                shape = shape
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.06f),
                shape = shape
            )
            .clip(shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint
        )
    }
}



//@Preview(
//    name = "Приглашения",
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun InvitesScreenPreview(){
//    InvitesScreen(
//        viewModel = viewModel(),
//        onReturnToLobby = {})
//}


@Composable
fun InvitesScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundBlur()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            InvitesHeader(onReturnToLobby = {})

            InvitesList(
                invitations = listOf(Invitation(
                    senderId = "guest-12345",
                    senderNickname = null,
                    receiverId = "12345",
                    sendTime = "22:30"
                )),
                onAccept = { },
                onReject = { }
            )
        }
    }
}

@Preview(
    name = "Список приглашений",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InvitesListPreview(){
    InvitesScreenContent()
//    InvitesHeader() {}
}
