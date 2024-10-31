package com.zoku.watch.screen


import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.health.composables.ActiveDurationText
import com.zoku.ui.BaseYellow
import com.zoku.watch.component.PagerScreen
import com.zoku.watch.component.StatusText
import com.zoku.watch.model.ExerciseScreenState
import com.zoku.watch.util.formatDistanceKm
import com.zoku.watch.util.formatElapsedTime
import com.zoku.watch.viewmodel.RunViewModel
import timber.log.Timber

@Composable
fun RunningScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<RunViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val items: List<@Composable () -> Unit> =
        listOf({
            RunningStatus(uiState = uiState)
        }, { RunningPause(scrollState = scrollState) })
    val pagerState = rememberPagerState(pageCount = { items.size })
    PagerScreen(
        modifier = modifier,
        state = pagerState,
        items = items
    )

}

@Composable
fun RunningPause(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            RunningButton(icon = Icons.Rounded.Stop)

            RunningButton(icon = Icons.Rounded.PlayArrow)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatusText(value = "6.39", type = "km")
            StatusText(value = "5`28", type = "페이스")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatusText(value = "6:30", type = "시간")
            StatusText(value = "130", type = "BPM")
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun RunningStatus(
    modifier: Modifier = Modifier,
    uiState: ExerciseScreenState
) {
    val lastActiveDurationCheckpoint = uiState.exerciseState?.activeDurationCheckpoint
    val exerciseState = uiState.exerciseState?.exerciseState
    Timber.tag("runningStatus").d("$lastActiveDurationCheckpoint")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))
        Text(
            modifier = Modifier,
            text = formatDistanceKm(uiState.exerciseState?.exerciseMetrics?.distance),
            fontSize = 50.sp,
            color = BaseYellow
        )
        Text(
            modifier = Modifier,
            text = "KM",
            fontSize = 20.sp,
        )
        Spacer(Modifier.height(20.dp))
        if (lastActiveDurationCheckpoint != null && exerciseState != null) {
            ActiveDurationText(
                checkpoint = lastActiveDurationCheckpoint,
                state = exerciseState
            ) {
                Text(
                    modifier = Modifier,
                    text = formatElapsedTime(it),
                    fontSize = 20.sp,
                )
            }


        }

    }

}


@Composable
fun RunningButton(
    modifier: Modifier = Modifier,
    icon: ImageVector
) {
    OutlinedButton(
        modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = BaseYellow
        ),
        border = ButtonDefaults.outlinedButtonBorder(
            borderColor = BaseYellow
        ),
        onClick = {}
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "button trigger",
            modifier = modifier
        )
    }
}


@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun RunningPreview() {
    RunningScreen()
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun RunningStatusPreview() {

}

