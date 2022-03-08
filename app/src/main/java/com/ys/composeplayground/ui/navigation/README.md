# Bottom Navagation Animation Activity

## rememberVectorPainter
```kotlin
@Composable
fun rememberVectorPainter(image: ImageVector) =
    rememberVectorPainter(
        defaultWidth = image.defaultWidth,
        defaultHeight = image.defaultHeight,
        viewportWidth = image.viewportWidth,
        viewportHeight = image.viewportHeight,
        name = image.name,
        tintColor = image.tintColor,
        tintBlendMode = image.tintBlendMode,
        content = { _, _ -> RenderVectorGroup(group = image.root) }
    )
```

주어진 ImageVector로 VectorPainter를 만듭니다. ImageVector의 트리 구조가 주어지면 벡터 계층 구조의 하위 구성이 생성됩니다.

매개변수:
- image - 벡터 그래픽 하위 구성을 만드는 데 사용되는 ImageVector

## animateDpAsState

```kotlin
@Composable
fun animateDpAsState(
    targetValue: Dp,
    animationSpec: AnimationSpec<Dp> = dpDefaultSpring,
    finishedListener: ((Dp) -> Unit)? = null
): State<Dp> {
    return animateValueAsState(
        targetValue,
        Dp.VectorConverter,
        animationSpec,
        finishedListener = finishedListener
    )
}
```

- Dp를 위한 Fire-and-Forget 애니메이션 기능. 이 Composable 함수는 Float, Color, Offset 등과 같은 다양한 매개변수 유형에 대해 오버로드됩니다. 
- 제공된 `targetValue가 변경되면 애니메이션이 자동으로 실행`됩니다. 
- targetValue가 변경될 때 진행 중인 애니메이션이 이미 있는 경우 진행 중인 애니메이션은 새 대상 값을 향해 애니메이션 방향을 조정합니다.
- animateDpAsState는 State 객체를 반환합니다. 상태 개체의 값은 애니메이션이 끝날 때까지 애니메이션에 의해 계속 업데이트됩니다.
- 트리에서 이 구성 가능한 함수를 제거하지 않고는 animateDpAsState를 취소/중지할 수 없습니다. 취소 가능한 애니메이션은 애니메이션 가능을 참조하세요.


## AnimationSpec
AnimationSpec은 1) 애니메이션할 데이터 유형, 2) 데이터(유형 T)가 AnimationVector로 변환되면 사용할 애니메이션 구성(즉, VectorizedAnimationSpec)을 포함하여 애니메이션 사양을 저장합니다.

TwoWayConverter가 데이터 유형 T를 AnimationVector에서 또는 AnimationVector로 변환하기 위해 제공되는 한 모든 유형 T는 시스템에 의해 애니메이션될 수 있습니다.
기본적으로 사용할 수 있는 변환기가 많이 있습니다. 
예를 들어 androidx.compose.ui.unit.IntOffset에 애니메이션을 적용하기 위해 시스템은 IntOffset.VectorConverter를 사용하여 개체를 AnimationVector2D로 변환하므로 x 및 y 차원이 별도의 속도 추적으로 독립적으로 애니메이션됩니다. 
이를 통해 다차원 개체를 진정한 다차원 방식으로 애니메이션할 수 있습니다.
애니메이션 중단을 부드럽게 처리하는 데 특히 유용합니다(예: 애니메이션 중에 대상이 변경되는 경우).

### spring

```kotlin
@Stable
fun <T> spring(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    stiffness: Float = Spring.StiffnessMedium,
    visibilityThreshold: T? = null
): SpringSpec<T> =
    SpringSpec(dampingRatio, stiffness, visibilityThreshold)

```

- 주어진 스프링 상수(예: DampingRatio 및 강성)를 사용하는 SpringSpec을 생성합니다. 
- 선택적 VisibilityThreshold는 애니메이션이 대상에 반올림하기에 충분히 시각적으로 가까운 것으로 간주되어야 하는 시점을 정의합니다.

매개변수:
- DampingRatio - 스프링의 감쇠비. 기본적으로 Spring.DampingRatioNoBounce.
- stiffness - 스프링의 강성. 기본적으로 Spring.StiffnessMedium.
- visibilityThreshold - 선택적으로 가시성 임계값을 지정합니다. 