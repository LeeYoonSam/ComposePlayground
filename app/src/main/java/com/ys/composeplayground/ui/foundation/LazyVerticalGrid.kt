package com.ys.composeplayground.ui.foundation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * LazyVerticalGrid
 * Jetpack Compose는 그리드 또는 그리드 요소를 표시하기 위한 API를 제공합니다.
 *
 * 지연 그리드 레이아웃의 DSL 구현. 그리드의 보이는 행만 구성합니다.
 * 이 API는 안정적이지 않습니다. [LazyColumn] 및 [Row]와 같은 안정적인 구성 요소 사용을 고려하십시오.
 * 동일한 결과를 얻으려면.
 *
 * @param cells 셀이 열을 형성하는 방법을 설명하는 클래스, 자세한 내용은 [GridCells] 문서 참조
 * @param modifier 이 레이아웃에 적용할 수식어
 * @param state 목록의 상태를 제어하거나 관찰하는 데 사용할 상태 개체
 * @param contentPadding은 전체 콘텐츠 주위에 패딩을 지정합니다.
 * @param content 내용을 설명하는 [LazyListScope]
 *
 * fun LazyVerticalGrid(
 *    cells: GridCells,
 *    modifier: Modifier = Modifier,
 *    state: LazyListState = rememberLazyListState(),
 *    contentPadding: PaddingValues = PaddingValues(0.dp),
 *    content: LazyGridScope.() -> Unit
 * )
 *
 * 그리드에서 목록 항목을 정렬하기 위해 LazyVerticalGrid는 셀이 열로 형성되는 방식을 제어하는 cells 매개변수를 제공합니다.
 * 다음 예제에서는 GridCells.Adaptive를 사용하여 각 열의 너비를 128.dp 이상으로 설정하여 그리드에 항목을 표시합니다.
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyVerticalGridDemo() {
    val list = (1..10).map { it.toString() }

    LazyVerticalGrid(

        columns = GridCells.Adaptive(128.dp),

        /**
         * GridCells.Adaptive 외에도 행당 열 수를 제공하는 다른 유형의 셀이 있습니다.
         * 아래 코드는 1행에 2열을 표시합니다.
         */
//        cells = GridCells.Fixed(2),

        // contentPadding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(list.size) { index ->  
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Red),
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = list[index],
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

        }
    )
}