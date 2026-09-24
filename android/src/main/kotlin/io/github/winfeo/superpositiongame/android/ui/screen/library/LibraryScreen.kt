package io.github.winfeo.superpositiongame.android.ui.screen.library

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.repository.CardsRepositoryImpl
import io.github.winfeo.superpositiongame.android.domain.library.Card
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur
import kotlin.math.absoluteValue
import kotlin.math.sign

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
) {
    val cards by viewModel.cards.collectAsState()

    val startPage = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = startPage,
        pageCount = { Int.MAX_VALUE }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0813))
    ) {
        BackgroundBlur()

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.015f))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            Text(
                text = stringResource(R.string.library_title),
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.library_info),
                color = Color.White.copy(alpha = 0.45f),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(42.dp))

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 64.dp),
                pageSpacing = (-24).dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                //Зацикливаение
                val actualPage = page % cards.size
                val card = cards[actualPage]

                //Анимация
                val pageOffset = (
                    (pagerState.currentPage - page) +
                        pagerState.currentPageOffsetFraction
                    ).absoluteValue.coerceIn(0f, 1f)

                val direction = sign((page - pagerState.currentPage).toFloat())

                val scale = lerp(
                    start = 0.78f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )

                val alpha = lerp(
                    start = 0.35f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )

                val rotation = pageOffset * 12f * direction

                Box(
                    modifier = Modifier
                        .zIndex(1f - pageOffset)
                        .graphicsLayer {
                            //масштаб
                            scaleX = scale
                            scaleY = scale
                            //прозрачность
                            this.alpha = alpha
                            //наклон соседних карт
                            rotationZ = rotation
                        }
                ) {
                    CardView(card)
                }
            }
        }
    }
}

@Composable
fun CardView(
    card: Card
) {
    Box(
        modifier = Modifier
            .width(270.dp)
            .height(430.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1B2E),
                            Color(0xFF10111D)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(32.dp)
                )
        ) {
            Image(
                painter = painterResource(card.imageRes),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
private fun lerp(
    start: Float,
    stop: Float,
    fraction: Float
): Float {
    return start + (stop - start) * fraction
}



@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LibraryScreenPrev() {
    LibraryScreen(
        viewModel = LibraryViewModel(CardsRepositoryImpl())
    )
}
