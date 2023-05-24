package com.ys.composeplayground.ui.foundation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * VerticalPager
 *
 * @Composable
 * @ExperimentalFoundationApi
 * fun VerticalPager(
 *     pageCount: Int,
 *     modifier: Modifier = Modifier,
 *     state: PagerState = rememberPagerState(),
 *     contentPadding: PaddingValues = PaddingValues(0.dp),
 *     pageSize: PageSize = PageSize.Fill,
 *     beyondBoundsPageCount: Int = 0,
 *     pageSpacing: Dp = 0.dp,
 *     horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
 *     flingBehavior: SnapFlingBehavior = PagerDefaults.flingBehavior(state = state),
 *     userScrollEnabled: Boolean = true,
 *     reverseLayout: Boolean = false,
 *     key: ((index: Int) -> Any)? = null,
 *     pageNestedScrollConnection: NestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
 *         Orientation.Vertical
 *     ),
 *     pageContent: @Composable (page: Int) -> Unit
 * )
 *
 * Parameters
 *
 * pageCount
 *      Pager 에 포함될 페이지 수입니다.
 *
 * modifier
 *      Pager 외부 레이아웃에 적용할 수정자 인스턴스입니다.
 *
 * state
 *      Pager 를 제어할 상태입니다.
 *
 * contentPadding
 *      전체 콘텐츠 주위에 패딩을 추가합니다. 이렇게 하면 콘텐츠가 스크랩된 후 패딩이 추가되며, 수정자 매개변수로는 불가능합니다.
 *      이를 사용하여 첫 페이지 앞이나 마지막 페이지 뒤에 패딩을 추가할 수 있습니다.
 *      페이지 사이에 간격을 추가하려면 pageSpacing을 사용합니다.
 *
 * pageSize
 *      Pager  내에서 페이지가 표시되는 방식을 변경하는 데 사용합니다.
 *
 * beyondBoundsPageCount
 *      표시되는 페이지 목록 전후에 로드할 페이지 수입니다.
 *      참고: beyondBoundsPageCount에 큰 값을 사용하면 많은 페이지가 구성, 측정 및 배치되어 지연 로딩을 사용하는 목적에 어긋날 수 있다는 점에 유의하세요.
 *      이 값은 표시되는 페이지 전후에 몇 개의 페이지를 미리 로드하는 최적화에 사용해야 합니다.
 *
 * pageSpacing
 *      Pager 에서 페이지를 구분하는 데 사용할 공간의 양입니다.
 *
 * horizontalAlignment
 *      Pager 에서 페이지를 가로로 정렬하는 방식입니다.
 *
 * flingBehavior
 *      스크롤 후 제스처에 사용할 플링 비헤이비어입니다.
 *
 * userScrollEnabled
 *      사용자 제스처 또는 접근성 동작을 통한 스크롤이 허용되는지 여부입니다. 비활성화되어 있어도 PagerState.scroll을 사용하여 프로그래밍 방식으로 스크롤할 수 있습니다.
 *
 * reverseLayout
 *      스크롤 및 레이아웃 방향을 반전시킵니다.
 *
 * key
 *      페이지를 나타내는 안정적이고 고유한 키입니다.
 *      키를 지정하면 해당 키를 기준으로 스크롤 위치가 유지되므로 현재 표시되는 페이지 앞에 페이지를 추가/제거할 경우 지정된 키를 가진 페이지가 첫 번째로 표시되는 페이지로 유지됩니다.
 *
 * pageNestedScrollConnection
 *      Pager 가 중첩된 목록에서 작동하는 방식을 지정하는 NestedScrollConnection입니다. 기본 동작은 호출기가 중첩된 모든 델타를 소비하는 것입니다.
 *
 * pageContent
 *      Pager 의 컴포저블 페이지
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleVerticalPagerSample() {
    // 단일 페이지 스냅으로 1페이지/뷰포트 VerticalPager를 생성합니다.
    VerticalPager(
        modifier = Modifier.fillMaxSize(),
        pageCount = 10
    ) { page ->
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Blue)
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = page.toString(), fontSize = 32.sp)
        }
    }
}

@Preview
@Composable
private fun SimpleVerticalPagerSamplePreview() {
    SimpleVerticalPagerSample()
}