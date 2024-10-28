package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun RunHistoryRecordListScreen(selectedDay: Int, onClick: (Int) -> Unit) {
    // 아이템을 통해 높이가 완성 되면 작동

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // 선택된 날짜 헤더
            item {
                Text(
                    text = "2024-10-${selectedDay}",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp, start = 20.dp, bottom = 5.dp)
                )
            }

            // 그날 뛴 기록 확인
            items(3) { index ->
                DailyRouteView(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    day = selectedDay,
                    onClick = {
                        onClick(index)
                    }
                )
            }

    }
}

@Composable
fun DailyRouteView(modifier: Modifier, day: Int, onClick: () -> Unit) {
    Surface(
        onClick = {onClick() },
        modifier = modifier
            .padding(horizontal = 20.dp) // 패딩을 Surface 외부에 적용
            .clip(RoundedCornerShape(16.dp)) // 클립을 Surface 바로 다음에 적용
            .height(200.dp),
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.sample_map_history_icon),
                contentDescription = null,
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                DailyRouteText(modifier = Modifier.weight(1f), text = "시작", fontSize = 16.sp)
                DailyRouteText(modifier = Modifier.weight(1f), text = "오후 3:37", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(10.dp))
                DailyRouteText(modifier = Modifier.weight(1f), text = "종료", fontSize = 16.sp)
                DailyRouteText(modifier = Modifier.weight(1f), text = "오후 3:57", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}


@Composable
fun DailyRouteText(modifier: Modifier, text: String, fontSize: TextUnit) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        textAlign = TextAlign.Center
    )
}