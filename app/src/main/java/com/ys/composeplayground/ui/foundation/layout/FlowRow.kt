package com.ys.composeplayground.ui.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * FlowRow
 * FlowRow는 LTR 레이아웃에서는 왼쪽에서 오른쪽(ltr)으로, RTL 레이아웃에서는 오른쪽에서 왼쪽(rtl)으로 항목을 채우고 공간이 부족하면 하단에 위치한 다음 '행' 또는 '줄'로 이동한 다음 항목이 다 채워질 때까지 항목을 계속 채우는 레이아웃입니다.
 *
 * 함수 정의
 * @Composable
 * @ExperimentalLayoutApi
 * inline fun FlowRow(
 *     modifier: Modifier = Modifier,
 *     horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
 *     verticalArrangement: Arrangement.Vertical = Arrangement.Top,
 *     maxItemsInEachRow: Int = Int.MAX_VALUE,
 *     content: @Composable RowScope.() -> Unit
 * )
 *
 * Parameters
 *
 * modifier
 * 행에 적용할 수정자입니다.
 *
 * horizontalArrangement
 * 레이아웃 자식의 가로 배열입니다.
 *
 * verticalArrangement
 * 레이아웃의 가상 행의 수직 배열입니다.
 *
 * maxItemsInEachRow
 * 행당 최대 항목 수
 *
 * content
 * RowScope @ 참조 FlowColumn @ 참조 androidx.compose.foundation.layout.Row로서의 콘텐츠입니다.
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimpleFlowRow() {
    FlowRow(
        Modifier
            .fillMaxWidth(1f)
            .wrapContentHeight(align = Alignment.Top),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        maxItemsInEachRow = 3
    ) {
        repeat(10) {
            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    .width(50.dp)
                    .height(50.dp)
                    .background(Color.Green)
            ) {
                Text(text = it.toString(), fontSize = 18.sp, modifier = Modifier.padding(3.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimpleFlowRowWithWeights() {
    FlowRow(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        maxItemsInEachRow = 3
    ) {
        repeat(6) { index ->
            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    .width(50.dp)
                    .height(50.dp)
                    .background(Color.Green)
                    .weight(if (index % 2 == 0) 1f else 2f, fill = true)
            )
        }
    }
}

@Preview
@Composable
fun SimpleFlowRowPreview() {
    SimpleFlowRow()
}

@Preview
@Composable
fun SimpleFlowRowWithWeightsPreview() {
    SimpleFlowRowWithWeights()
}