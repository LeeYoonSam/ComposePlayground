# Album 형태 구현 샘플

## Grid 구현 방법

## LazyVerticalGrid 사용

- lazy grid layout 의 DSL 구현. 
- 그리드의 보이는 행만 구성합니다. 
- 이 API는 안정적이지 않습니다. 
- 동일한 결과를 얻으려면 LazyColumn 및 Row와 같은 안정적인 구성 요소를 사용하는 것이 좋습니다.

내부에서 `FixedLazyGrid` 를 구현해서 사용하고 그 내부에서는 `LazyColumn` 으로 구현이 되어있다.

```kotlin
private fun FixedLazyGrid(
    nColumns: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scope: LazyGridScopeImpl
) {
    val rows = (scope.totalSize + nColumns - 1) / nColumns
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding
    ) {
        items(rows) { rowIndex ->
            Row {
                for (columnIndex in 0 until nColumns) {
                    val itemIndex = rowIndex * nColumns + columnIndex
                    if (itemIndex < scope.totalSize) {
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            scope.contentFor(itemIndex, this@items).invoke()
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}
```
- 내부에서 `nColumns` 를 기준으로 `rows` 를 계산해서 반복하고 아이템을 `Box` 안에 `content` 를 표시 
- `LazyGridScopeImpl` 으로 컨텐츠를 관리

### GridCells 구현 방법

```kotlin
sealed class GridCells {
    /**
     * Combines cells with fixed number rows or columns.
     *
     * For example, for the vertical [LazyVerticalGrid] Fixed(3) would mean that there are 3 columns 1/3
     * of the parent wide.
     */
    @ExperimentalFoundationApi
    class Fixed(val count: Int) : GridCells()

    /**
     * Combines cells with adaptive number of rows or columns. It will try to position as many rows
     * or columns as possible on the condition that every cell has at least [minSize] space and
     * all extra space distributed evenly.
     *
     * For example, for the vertical [LazyVerticalGrid] Adaptive(20.dp) would mean that there will be as
     * many columns as possible and every column will be at least 20.dp and all the columns will
     * have equal width. If the screen is 88.dp wide then there will be 4 columns 22.dp each.
     */
    @ExperimentalFoundationApi
    class Adaptive(val minSize: Dp) : GridCells()
}
```

1. GridCells.Fixed(count: Int) -> 갯수를 지정
2. GridCells.Adaptive(minSize: Dp) -> 최소 크기를 지정

### 앨범 리스트 구현

참고
- [안드로이드 - MediaStore에서 미디어 파일 정보 읽는 방법](https://codechacha.com/ko/android-mediastore-read-media-files/)