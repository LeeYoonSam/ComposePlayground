package com.ys.composeplayground.ui.foundation.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * 플로우컬럼은 항목을 위에서 아래로 채우는 레이아웃으로,
 * 하단의 공간이 부족하면 ltr 또는 rtl 레이아웃에 따라 오른쪽 또는 왼쪽의 다음 '열' 또는 '줄'로 이동한 다음 위에서 아래로 항목을 계속 채웁니다.
 *
 * @Composable
 * @ExperimentalLayoutApi
 * inline fun FlowColumn(
 *     modifier: Modifier = Modifier,
 *     verticalArrangement: Arrangement.Vertical = Arrangement.Top,
 *     horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
 *     maxItemsInEachColumn: Int = Int.MAX_VALUE,
 *     content: @Composable ColumnScope.() -> Unit
 * )
 *
 * modifier
 * 행에 적용할 수정자입니다.
 *
 * verticalArrangement
 * 레이아웃 자식의 세로 배열입니다.
 *
 * horizontalArrangement
 * 레이아웃의 가상 열의 가로 배열입니다.
 *
 * maxItemsInEachColumn
 * 열당 최대 항목 수
 *
 * content
 * ColumnScope @see FlowRow @see androidx.compose.foundation.layout.Column의 콘텐츠입니다.
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimpleFlowColumn() {
    FlowColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .requiredHeight(200.dp)
            .border(BorderStroke(2.dp, Color.Gray)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        maxItemsInEachColumn = 3
    ) {
        repeat(10) { index ->
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
                    .width(50.dp)
                    .height(50.dp)
                    .background(color = Color.Green)
            ) {
                Text(
                    text = index.toString(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(3.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimpleFlowColumnWithWeights() {
    FlowColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .requiredHeight(200.dp)
            .border(BorderStroke(2.dp, Color.Gray)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        maxItemsInEachColumn = 3
    ) {
        repeat(10) { index ->
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
                    .width(50.dp)
                    .height(50.dp)
                    .weight(if (index % 2 == 0) 1f else 2f, fill = true)
                    .background(color = Color.Green)
            ) {
                Text(
                    text = index.toString(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(3.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SimpleFlowColumnPreview() {
    SimpleFlowColumn()
}

@Preview
@Composable
fun SimpleFlowColumnWithWeightsPreview() {
    SimpleFlowColumnWithWeights()
}