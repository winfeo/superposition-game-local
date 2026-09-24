package io.github.winfeo.superpositiongame.android.ui.screen.onboarding

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.ui.dialog.game.CardPreviewDialog
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingAccentLight
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingAppSections
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingBackground
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingCard
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingHeader
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingMutedText
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingSkipDialog
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingSlot
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingSpeechBubble
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingTableGuide
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingTaskChip
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components.OnboardingText
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.description.instance.hadamard.Hadamard
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private const val TRAINING_DESIGN_WIDTH_DP = 180f
private const val TRAINING_CONTENT_HEIGHT_DP = 366f
private const val TRAINING_HINT_RESERVED_HEIGHT_DP = 28f
private const val TRAINING_MIN_SCALE = 0.45f
private const val TRAINING_MAX_SCALE = 1.15f
private const val TAP_HINT_DELAY_MILLIS = 10_000L

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinished: () -> Unit,
    onSkipped: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val previewCard = remember {
        Card(
            id = "onboarding-hadamard",
            textureId = null,
            description = Hadamard()
        )
    }
    val speechResources = speechResourcesForState(state)
    var speechIndex by remember(
        state.currentStep,
        state.tableFocus,
        state.wasCardPreviewOpened
    ) { mutableIntStateOf(0) }
    val currentSpeechIndex = speechIndex.coerceAtMost(speechResources.lastIndex)
    val canTapToContinue = state.canContinueByTap()
    var revealSpeechImmediately by remember(
        state.currentStep,
        state.tableFocus,
        state.wasCardPreviewOpened,
        currentSpeechIndex
    ) { mutableStateOf(false) }
    var isSpeechTyping by remember(
        state.currentStep,
        state.tableFocus,
        state.wasCardPreviewOpened,
        currentSpeechIndex
    ) { mutableStateOf(true) }
    var showTapHint by remember(
        state.currentStep,
        state.tableFocus,
        state.wasCardPreviewOpened,
        currentSpeechIndex
    ) { mutableStateOf(false) }
    val continueInteractionSource = remember { MutableInteractionSource() }

    LaunchedEffect(
        state.currentStep,
        state.tableFocus,
        state.wasCardPreviewOpened,
        currentSpeechIndex,
        canTapToContinue,
        isSpeechTyping
    ) {
        if (canTapToContinue && !isSpeechTyping) {
            delay(TAP_HINT_DELAY_MILLIS.milliseconds)
            showTapHint = true
        }
    }

    val continueOnboarding = {
        if (isSpeechTyping) {
            revealSpeechImmediately = true
            showTapHint = false
        } else if (canTapToContinue) {
            showTapHint = false
            if (currentSpeechIndex < speechResources.lastIndex) {
                speechIndex = currentSpeechIndex + 1
            } else if (state.currentStep == OnboardingStep.FINISH) {
                onFinished()
            } else {
                viewModel.nextStep()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OnboardingBackground)
    ) {
        BackgroundBlur()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = Color.Transparent
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                OnboardingHeader(
                    step = state.currentStep,
                    onBack = viewModel::previousStep,
                    onSkip = viewModel::showSkipDialog
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable(
                            enabled = (isSpeechTyping || canTapToContinue) &&
                                !state.isSkipDialogVisible &&
                                !state.isCardPreviewVisible,
                            interactionSource = continueInteractionSource,
                            indication = null,
                            onClick = continueOnboarding
                        )
                ) {
                    key(state.currentStep, state.tableFocus) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            OnboardingStepContent(
                                state = state,
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    OnboardingSpeechBubble(
                        text = stringResource(speechResources[currentSpeechIndex]),
                        showTapHint = showTapHint,
                        revealTextImmediately = revealSpeechImmediately,
                        onTypingChanged = { isTyping ->
                            isSpeechTyping = isTyping
                        }
                    )
                }
            }
        }

        if (state.isSkipDialogVisible) {
            OnboardingSkipDialog(
                onConfirm = {
                    viewModel.dismissSkipDialog()
                    onSkipped()
                },
                onDismiss = viewModel::dismissSkipDialog
            )
        }

        if (state.isCardPreviewVisible) {
            CardPreviewDialog(
                card = previewCard,
                onDismiss = viewModel::dismissCardPreview
            )
        }
    }
}

@Composable
private fun OnboardingStepContent(
    state: OnboardingState,
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier
) {
    when (state.currentStep) {
        OnboardingStep.WELCOME -> WelcomeStep(modifier = modifier)
        OnboardingStep.OBJECTIVE -> ObjectiveStep(modifier = modifier)
        OnboardingStep.PLAY_CARD -> PlayCardStep(
            onCardApplied = viewModel::applyHadamard,
            modifier = modifier
        )
        OnboardingStep.CARD_RESULT -> CardResultStep(modifier = modifier)
        OnboardingStep.CARD_PREVIEW -> CardPreviewStep(
            onLongPress = viewModel::showCardPreview,
            modifier = modifier
        )
        OnboardingStep.FULL_TABLE -> FullTableStep(
            focus = state.tableFocus,
            modifier = modifier
        )
        OnboardingStep.FINISH -> FinishStep(modifier = modifier)
    }
}

@Composable
private fun WelcomeStep(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(138.dp)
        )
        Text(
            text = stringResource(R.string.onboarding_welcome_title),
            color = OnboardingText,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.onboarding_welcome_body),
            color = OnboardingMutedText,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.86f)
        )
    }
}

@Composable
private fun ObjectiveStep(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StepTitle(title = stringResource(R.string.onboarding_objective_title))
        OnboardingTaskChip(stringResource(R.string.onboarding_objective_task))
        OnboardingTrainingStage(
            diceState = OnboardingDiceState.ZERO,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun PlayCardStep(
    onCardApplied: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var restingCardBounds by remember { mutableStateOf<Rect?>(null) }
    var slotBounds by remember { mutableStateOf<Rect?>(null) }
    var isDragging by remember { mutableStateOf(false) }
    var isOverSlot by remember { mutableStateOf(false) }
    val displayedOffset by animateOffsetAsState(
        targetValue = dragOffset,
        animationSpec = if (isDragging) snap() else tween(durationMillis = 180),
        label = "onboarding-card-offset"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StepTitle(title = stringResource(R.string.onboarding_play_card_title))
        OnboardingTaskChip(stringResource(R.string.onboarding_play_card_task))
        OnboardingTrainingStage(
            diceState = OnboardingDiceState.ZERO,
            highlighted = isOverSlot,
            onSlotBoundsChanged = { slotBounds = it },
            hint = if (isOverSlot) { stringResource(R.string.onboarding_play_card_release_hint) } else null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { scale ->
            Spacer(modifier = Modifier.height(5.dp * scale))
            Box(
                modifier = Modifier.height(29.dp * scale),
                contentAlignment = Alignment.Center
            ) {
                if (!isDragging && displayedOffset == Offset.Zero) {
                    Text(
                        text = stringResource(R.string.onboarding_drag_arrow),
                        color = OnboardingAccentLight,
                        style = MaterialTheme.typography.h5.copy(
                            fontSize = MaterialTheme.typography.h5.fontSize * scale,
                            lineHeight = MaterialTheme.typography.h5.lineHeight * scale
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp * scale))
            OnboardingCard(
                selected = isDragging,
                scale = scale,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = displayedOffset.x.roundToInt(),
                            y = displayedOffset.y.roundToInt()
                        )
                    }
                    .onGloballyPositioned { coordinates ->
                        if (!isDragging && dragOffset == Offset.Zero && displayedOffset == Offset.Zero) {
                            restingCardBounds = coordinates.boundsInRoot()
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                isDragging = true
                                isOverSlot = false
                            },
                            onDragEnd = {
                                val shouldApplyCard = isOverSlot
                                isDragging = false
                                isOverSlot = false
                                dragOffset = Offset.Zero
                                if (shouldApplyCard) onCardApplied()
                            },
                            onDragCancel = {
                                isDragging = false
                                isOverSlot = false
                                dragOffset = Offset.Zero
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val nextOffset = dragOffset + dragAmount
                                dragOffset = nextOffset
                                isOverSlot = isCardOverSlot(
                                    restingCardBounds = restingCardBounds,
                                    slotBounds = slotBounds,
                                    dragOffset = nextOffset
                                )
                            }
                        )
                    }
            )
        }
    }
}

private fun isCardOverSlot(
    restingCardBounds: Rect?,
    slotBounds: Rect?,
    dragOffset: Offset
): Boolean {
    if (restingCardBounds == null || slotBounds == null) return false

    val movedCard = restingCardBounds.translate(dragOffset)
    val intersectionWidth = (
        minOf(movedCard.right, slotBounds.right) - maxOf(movedCard.left, slotBounds.left)
    ).coerceAtLeast(0f)
    val intersectionHeight = (
        minOf(movedCard.bottom, slotBounds.bottom) - maxOf(movedCard.top, slotBounds.top)
    ).coerceAtLeast(0f)
    val intersectionArea = intersectionWidth * intersectionHeight
    val cardArea = movedCard.width * movedCard.height

    return cardArea > 0f && intersectionArea / cardArea >= 0.25f
}

@Composable
private fun CardResultStep(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StepTitle(title = stringResource(R.string.onboarding_result_title))
        OnboardingTaskChip(stringResource(R.string.onboarding_play_card_task))
        OnboardingTrainingStage(
            diceState = OnboardingDiceState.PLUS,
            appliedCard = true,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun OnboardingTrainingStage(
    diceState: OnboardingDiceState,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    appliedCard: Boolean = false,
    onSlotBoundsChanged: ((Rect) -> Unit)? = null,
    hint: String? = null,
    content: @Composable ColumnScope.(scale: Float) -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier) {
        val scale = trainingScale(maxWidth = maxWidth, maxHeight = maxHeight)

        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingSlot(
                diceState = diceState,
                highlighted = highlighted,
                appliedCard = appliedCard,
                onSlotBoundsChanged = onSlotBoundsChanged,
                scale = scale
            )
            content(scale)
        }

        if (hint != null) {
            Text(
                text = hint,
                color = OnboardingAccentLight,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp)
            )
        }
    }
}

private fun trainingScale(maxWidth: Dp, maxHeight: Dp): Float {
    val widthScale = maxWidth.value / TRAINING_DESIGN_WIDTH_DP
    val heightScale = (maxHeight.value - TRAINING_HINT_RESERVED_HEIGHT_DP) / TRAINING_CONTENT_HEIGHT_DP

    return minOf(widthScale, heightScale, TRAINING_MAX_SCALE).coerceAtLeast(TRAINING_MIN_SCALE)
}

@Composable
private fun CardPreviewStep(
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        StepTitle(title = stringResource(R.string.onboarding_preview_title))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = { onLongPress() })
                    }
            ) {
                OnboardingCard()
            }
        }
    }
}

@Composable
private fun FullTableStep(
    focus: OnboardingTableFocus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StepTitle(title = stringResource(R.string.onboarding_table_title))
        OnboardingTableGuide(
            focus = focus,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FinishStep(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepTitle(title = stringResource(R.string.onboarding_finish_title))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(96.dp)
            )
            OnboardingAppSections()
        }
    }
}

@Composable
private fun StepTitle(title: String) {
    Text(
        text = title,
        color = OnboardingText,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}

private fun speechResourcesForState(state: OnboardingState): List<Int> {
    return when (state.currentStep) {
        OnboardingStep.WELCOME -> listOf(
            R.string.onboarding_welcome_speech
        )
        OnboardingStep.OBJECTIVE -> listOf(
            R.string.onboarding_objective_speech,
            R.string.onboarding_objective_state_speech
        )
        OnboardingStep.PLAY_CARD -> listOf(R.string.onboarding_play_card_speech)
        OnboardingStep.CARD_RESULT -> listOf(
            R.string.onboarding_result_speech,
            R.string.onboarding_result_complete_speech
        )
        OnboardingStep.CARD_PREVIEW -> listOf(
            if (state.wasCardPreviewOpened) {
                R.string.onboarding_preview_opened_speech
            } else {
                R.string.onboarding_preview_speech
            }
        )
        OnboardingStep.FULL_TABLE -> listOf(
            when (state.tableFocus) {
                OnboardingTableFocus.OVERVIEW -> R.string.onboarding_table_overview_speech
                OnboardingTableFocus.PLAYERS_AND_TIMER -> R.string.onboarding_table_timer_speech
                OnboardingTableFocus.OBJECTIVE -> R.string.onboarding_table_objective_speech
                OnboardingTableFocus.OPPONENT_SLOTS -> R.string.onboarding_table_opponent_speech
                OnboardingTableFocus.PLAYER_SLOTS -> R.string.onboarding_table_player_speech
                OnboardingTableFocus.HAND -> R.string.onboarding_table_hand_speech
            }
        )
        OnboardingStep.FINISH -> listOf(
            R.string.onboarding_finish_speech,
            R.string.onboarding_finish_hint_speech
        )
    }
}

private fun OnboardingState.canContinueByTap(): Boolean {
    return when (currentStep) {
        OnboardingStep.PLAY_CARD -> false
        OnboardingStep.CARD_PREVIEW -> wasCardPreviewOpened && !isCardPreviewVisible
        else -> true
    }
}
