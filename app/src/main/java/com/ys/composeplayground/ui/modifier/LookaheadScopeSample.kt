package com.ys.composeplayground.ui.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.DeferredTargetAnimation
import androidx.compose.animation.core.ExperimentalAnimatableApi
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ApproachLayoutModifierNode
import androidx.compose.ui.layout.ApproachMeasureScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.approachLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 코드 출처:
 * - https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/ui/ui/samples/src/main/java/androidx/compose/ui/samples/LookaheadScopeSample.kt
 */

/**
 * `LookaheadScope`는 **컴포저블의 크기나 위치 변경을 기반으로 복잡한 맞춤형 레이아웃 애니메이션**을 구현할 때 사용합니다.
 *
 * Compose의 기본 `animateContentSize`나 `AnimatedVisibility` 같은 모디파이어로 구현하기 어려운 고급 애니메이션(예: 공유 요소 전환)을 만들 때 필요한 핵심 구성 요소입니다.
 *
 * ---
 * ### 핵심 기능: 2단계 레이아웃 (Two-Pass Layout)
 *
 * `LookaheadScope`가 하는 가장 중요한 일은 Compose의 레이아웃 계산을 1단계에서 2단계로 확장하는 것입니다.
 *
 * 1.  **Lookahead Pass (미리보기 패스)**:
 *     * 애니메이션을 "무시"하고, 상태 변경 후 컴포저블이 **최종적으로 도달할 크기와 위치**를 미리 계산합니다.
 *     * 이것은 애니메이션의 **목표 지점(End State)** 이 됩니다.
 *
 * 2.  **Main Pass (실제 패스)**:
 *     * 애니메이션의 현재 프레임에 해당하는 **중간 크기와 위치**를 계산합니다.
 *     * `LookaheadScope`는 1번에서 계산한 최종 목표 지점과 현재 지점을 모두 알 수 있게 해줍니다.
 *
 * ---
 * ### 주요 사용 시점
 *
 * `LookaheadScope`는 주로 다음과 같은 고급 모디파이어를 작동시키기 위한 **필수 래퍼(Wrapper)** 로 사용됩니다.
 *
 * #### **1. `Modifier.approachLayout` (크기/위치 접근 애니메이션)**
 * 컴포저블의 레이아웃 **제약 조건(constraints)** 자체가 애니메이션 되도록 할 때 사용합니다. `LookaheadScope`가 최종 크기를 알려주면, `approachLayout`은 현재 크기에서 그 최종 크기까지 부드럽게 변하는 애니메이션을 만듭니다.
 *
 * #### **2. `Modifier.sharedElement` (공유 요소 전환)**
 * 안드로이드 뷰 시스템의 'Shared Element Transition'과 동일한 기능입니다.
 * * 예를 들어, 화면 A의 작은 이미지(`Box`)와 화면 B의 큰 이미지(`Box`)를 `LookaheadScope`로 감싸고 `sharedElement` 모디파이어로 연결하면, 화면이 전환될 때 이미지가 화면 A의 위치에서 화면 B의 위치로 부드럽게 이동하며 커지는 애니메이션이 구현됩니다.
 *
 * ---
 * ### 결론
 *
 * `LookaheadScope`는 직접 무언가를 하지는 않지만, 그 **내부에서 `approachLayout`이나 `sharedElement` 같은 고급 애니메이션 모디파이어를 사용할 수 있도록 기반 환경을 제공**하는 필수 스코프(Scope)입니다.
 *
 * **"복잡한 레이아웃 애니메이션이나 화면 전환 애니메이션을 구현하고 싶을 때"** 가장 먼저 `LookaheadScope`로 감싸야 한다고 생각하시면 됩니다.
 */
@Composable
fun LookaheadDemos() {
    var currentSample by remember { mutableStateOf("sample1") }

    Column(Modifier.fillMaxSize()) {
        Button(onClick = { currentSample = "sample1" }) {
            Text("ApproachLayoutSample")
        }
        Button(onClick = { currentSample = "sample2" }) {
            Text("LookaheadLayoutCoordinatesSample")
        }
        Button(onClick = { currentSample = "sample3" }) {
            Text("AnimateContentSizeAfterLookaheadPass")
        }

        when (currentSample) {
            "sample1" -> {
                Text("ApproachLayoutSample")
                LookaheadScope {
                    ApproachLayoutSample()
                }
            }

            "sample2" -> {
                Text("LookaheadLayoutCoordinatesSample")
                LookaheadLayoutCoordinatesSample()
            }

            "sample3" -> {
                Text("AnimateContentSizeAfterLookaheadPass")
                AnimateContentSizeAfterLookaheadPass()
            }
        }
    }
}

@OptIn(ExperimentalAnimatableApi::class)
@Composable
fun ApproachLayoutSample() {
    // Creates a custom modifier that animates the constraints and measures child with the
    // animated constraints. This modifier is built on top of `Modifier.approachLayout` to approach
    // th destination size determined by the lookahead pass. A resize animation will be kicked off
    // whenever the lookahead size changes, to animate children from current size to destination
    // size. Fixed constraints created based on the animation value will be used to measure
    // child, so the child layout gradually changes its animated constraints until the approach
    // completes.
    fun Modifier.animateConstraints(
        sizeAnimation: DeferredTargetAnimation<IntSize, AnimationVector2D>,
        coroutineScope: CoroutineScope,
    ) =
        this.approachLayout(
            isMeasurementApproachInProgress = { lookaheadSize ->
                // Update the target of the size animation.
                sizeAnimation.updateTarget(lookaheadSize, coroutineScope)
                // Return true if the size animation has pending target change or hasn't finished
                // running.
                !sizeAnimation.isIdle
            }
        ) { measurable, _ ->
            // In the measurement approach, the goal is to gradually reach the destination size
            // (i.e. lookahead size). To achieve that, we use an animation to track the current
            // size, and animate to the destination size whenever it changes. Once the animation
            // finishes, the approach is complete.

            // First, update the target of the animation, and read the current animated size.
            val (width, height) = sizeAnimation.updateTarget(lookaheadSize, coroutineScope)
            // Then create fixed size constraints using the animated size
            val animatedConstraints = Constraints.fixed(width, height)
            // Measure child with animated constraints.
            val placeable = measurable.measure(animatedConstraints)
            layout(placeable.width, placeable.height) { placeable.place(0, 0) }
        }

    var fullWidth by remember { mutableStateOf(false) }

    // Creates a size animation with a target unknown at the time of instantiation.
    val sizeAnimation = remember { DeferredTargetAnimation(IntSize.VectorConverter) }
    val coroutineScope = rememberCoroutineScope()
    Row(
        (if (fullWidth) Modifier.fillMaxWidth() else Modifier.width(100.dp))
            .clickable { fullWidth = !fullWidth }
            .height(200.dp)
            // Use the custom modifier created above to animate the constraints passed
            // to the child, and therefore resize children in an animation.
            .animateConstraints(sizeAnimation, coroutineScope)
    ) {
        Box(Modifier.weight(1f).fillMaxHeight().background(Color(0xffff6f69)))
        Box(Modifier.weight(2f).fillMaxHeight().background(Color(0xffffcc5c)))
    }
}

@OptIn(ExperimentalAnimatableApi::class)
@Composable
fun LookaheadLayoutCoordinatesSample() {
    /**
     * Creates a custom implementation of ApproachLayoutModifierNode to approach the placement of
     * the layout using an animation.
     */
    class AnimatedPlacementModifierNode(var lookaheadScope: LookaheadScope) :
        ApproachLayoutModifierNode, Modifier.Node() {
        // Creates an offset animation, the target of which will be known during placement.
        val offsetAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D> =
            DeferredTargetAnimation(IntOffset.VectorConverter)

        override fun isMeasurementApproachInProgress(lookaheadSize: IntSize): Boolean {
            // Since we only animate the placement here, we can consider measurement approach
            // complete.
            return false
        }

        // Returns true when the offset animation is in progress, false otherwise.
        override fun Placeable.PlacementScope.isPlacementApproachInProgress(
            lookaheadCoordinates: LayoutCoordinates
        ): Boolean {
            val target =
                with(lookaheadScope) {
                    lookaheadScopeCoordinates.localLookaheadPositionOf(lookaheadCoordinates).round()
                }
            offsetAnimation.updateTarget(target, coroutineScope)
            return !offsetAnimation.isIdle
        }

        override fun ApproachMeasureScope.approachMeasure(
            measurable: Measurable,
            constraints: Constraints,
        ): MeasureResult {
            val placeable = measurable.measure(constraints)
            return layout(placeable.width, placeable.height) {
                val coordinates = coordinates
                if (coordinates != null) {
                    // Calculates the target offset within the lookaheadScope
                    val target =
                        with(lookaheadScope) {
                            lookaheadScopeCoordinates.localLookaheadPositionOf(coordinates).round()
                        }

                    // Uses the target offset to start an offset animation
                    val animatedOffset = offsetAnimation.updateTarget(target, coroutineScope)
                    // Calculates the *current* offset within the given LookaheadScope
                    val placementOffset =
                        with(lookaheadScope) {
                            lookaheadScopeCoordinates
                                .localPositionOf(coordinates, Offset.Zero)
                                .round()
                        }
                    // Calculates the delta between animated position in scope and current
                    // position in scope, and places the child at the delta offset. This puts
                    // the child layout at the animated position.
                    val (x, y) = animatedOffset - placementOffset
                    placeable.place(x, y)
                } else {
                    placeable.place(0, 0)
                }
            }
        }
    }

    // Creates a custom node element for the AnimatedPlacementModifierNode above.
    data class AnimatePlacementNodeElement(val lookaheadScope: LookaheadScope) :
        ModifierNodeElement<AnimatedPlacementModifierNode>() {

        override fun update(node: AnimatedPlacementModifierNode) {
            node.lookaheadScope = lookaheadScope
        }

        override fun create(): AnimatedPlacementModifierNode {
            return AnimatedPlacementModifierNode(lookaheadScope)
        }
    }

    val colors = listOf(Color(0xffff6f69), Color(0xffffcc5c), Color(0xff264653), Color(0xff2a9d84))

    var isInColumn by remember { mutableStateOf(true) }
    LookaheadScope {
        // Creates movable content containing 4 boxes. They will be put either in a [Row] or in a
        // [Column] depending on the state
        val items = remember {
            movableContentOf {
                colors.forEach { color ->
                    Box(
                        Modifier.padding(15.dp)
                            .size(100.dp, 80.dp)
                            .then(AnimatePlacementNodeElement(this))
                            .background(color, RoundedCornerShape(20))
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().clickable { isInColumn = !isInColumn }) {
            // As the items get moved between Column and Row, their positions in LookaheadScope
            // will change. The `animatePlacementInScope` modifier created above will
            // observe that final position change via `localLookaheadPositionOf`, and create
            // a position animation.
            if (isInColumn) {
                Column(Modifier.fillMaxSize()) { items() }
            } else {
                Row { items() }
            }
        }
    }
}

/**
 * `modifier.layout`은 **단일 컴포저블의 측정(measure)과 배치(layout) 방식을 완전히 커스텀**하고 싶을 때 사용하는 가장 기본적인 로우레벨(low-level) 모디파이어입니다.
 *
 * 표준 `Modifier.size`, `Modifier.padding`, `Modifier.offset` 등으로 구현하기 어렵거나 불가능한, 매우 세밀한 제어가 필요할 때 사용합니다.
 *
 * -----
 *
 * ### 작동 방식 (2단계)
 *
 * `modifier.layout`은 람다 블록 안에서 **측정**과 **배치**라는 두 가지 핵심 단계를 직접 제어할 수 있게 해줍니다.
 *
 * 1.  **측정 (Measure)**: `measurable.measure(constraints)`
 *
 *       * 부모가 "넌 이 정도 공간(`constraints`) 안에서 그려져야 해"라고 알려줍니다.
 *       * 개발자는 이 `measurable`(측정할 컴포저블)을 `constraints`에 맞게, 혹은 `constraints`를 무시하고 원하는 크기로 측정합니다.
 *       * 측정이 끝나면 `placeable`(배치 가능한 객체)이 반환됩니다.
 *
 * 2.  **배치 (Layout & Place)**: `layout(width, height) { ... }`
 *
 *       * 측정된 값을 바탕으로, 이 모디파이어가 적용된 **컴포저블 자체의 최종 크기**(`width`, `height`)를 결정합니다.
 *       * `{...}` 블록 안에서, 측정 완료된 `placeable` 객체를 `placeable.place(x, y)`를 통해 **자신이 정한 크기 내의 특정 좌표(x, y)**에 배치시킵니다.
 *
 * -----
 *
 * ### 주요 사용 시점
 *
 * #### **1. 커스텀 모디파이어(Modifier)를 만들 때**
 *
 * Compose의 거의 모든 레이아웃 관련 모디파이어(예: `padding`, `size`, `offset` 등)는 내부적으로 `modifier.layout`을 기반으로 만들어졌습니다. 자신만의 독특한 레이아웃 규칙(예: `aspectRatio`처럼 비율을 맞추거나)을 가진 재사용 가능한 모디파이어를 만들 때 필수적입니다.
 *
 * **예시: `padding` 모디파이어를 직접 만든다면 (간략화된 버전)**
 *
 * ```kotlin
 * fun Modifier.myCustomPadding(all: Dp): Modifier =
 *     this.layout { measurable, constraints ->
 *         // 1. Dp를 Px로 변환
 *         val paddingPx = all.roundToPx()
 *         val horizontalPadding = paddingPx * 2
 *         val verticalPadding = paddingPx * 2
 *
 *         // 2. 측정: 자식에게는 부모 제약조건에서 패딩값을 뺀 만큼만 허용
 *         val placeable = measurable.measure(
 *             constraints.copy(
 *                 maxWidth = constraints.maxWidth - horizontalPadding,
 *                 maxHeight = constraints.maxHeight - verticalPadding
 *             )
 *         )
 *
 *         // 3. 배치: 부모의 크기는 자식 크기에 패딩값을 더한 것
 *         val width = placeable.width + horizontalPadding
 *         val height = placeable.height + verticalPadding
 *
 *         layout(width, height) {
 *             // 4. 자식을 (paddingPx, paddingPx) 위치에 배치
 *             placeable.place(paddingPx, paddingPx)
 *         }
 *     }
 * ```
 *
 * #### **2. 표준 모디파이어로 불가능한 배치가 필요할 때**
 *
 * 예를 들어, 자식의 크기를 측정한 뒤, 그 측정된 높이만큼 자식을 Y축으로 이동시켜 화면 밖에서 애니메이션으로 등장하게 하는 등의 복잡한 계산이 필요할 때 사용합니다.
 *
 * -----
 *
 * ### `Layout` 컴포저블과의 차이점
 *
 * `modifier.layout`과 `Layout` 컴포저블을 혼동하기 쉽지만, 역할이 명확히 다릅니다.
 *
 *   * **`modifier.layout`**: **단 하나**의 컴포저블에 붙어, **그 자신**의 측정/배치 방식을 정의합니다.
 *   * **`Layout` 컴포저블**: **여러** 자식 컴포저블을 감싸며, **자식들 전체**를 어떻게 측정하고 배치할지(마치 `Row`나 `Column`을 새로 만드는 것처럼)를 정의합니다.
 */
@Composable
fun AnimateContentSizeAfterLookaheadPass() {
    var sizeAnim by remember { mutableStateOf<Animatable<IntSize, AnimationVector2D>?>(null) }
    var lookaheadSize by remember { mutableStateOf<IntSize?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LookaheadScope {
        Box(
            Modifier
                // 애니메이션을 시각적으로 확인하기 위해 배경색과 테두리 추가
                .background(Color.LightGray.copy(alpha = 0.3f))
                .border(1.dp, Color.Gray)
                .clipToBounds()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)

                    val measuredSize = IntSize(placeable.width, placeable.height)
                    val (width, height) =
                        if (isLookingAhead) {
                            // Lookahead 패스: 콘텐츠의 최종(목표) 크기를 측정하고 기록합니다.
                            lookaheadSize = measuredSize
                            measuredSize
                        } else {
                            // Main 패스:
                            // 1. 목표 크기(lookaheadSize)를 가져옵니다.
                            val target = requireNotNull(lookaheadSize)

                            // 2. Animatable 객체를 생성하거나 가져옵니다.
                            val anim =
                                sizeAnim?.also {
                                    // 이미 Animatable이 있다면, animateTo로 새 목표 크기까지 애니메이션을 실행합니다.
                                    if (it.targetValue != target) {
                                        coroutineScope.launch { it.animateTo(target) }
                                    }
                                } ?: Animatable(target, IntSize.VectorConverter) // 처음이라면 목표 크기로 생성

                            sizeAnim = anim

                            // 3. 레이아웃 크기로 현재 애니메이션 값(anim.value)을 반환합니다.
                            anim.value
                        }

                    layout(width, height) { placeable.place(0, 0) }
                }
        ) {
            // 이 Column의 크기가 변하면, 부모 Box가 애니메이션으로 크기를 조절합니다.
            var expanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "줄이기" else "자세히 보기")
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = if (expanded) {
                        "이것은 확장되었을 때 나타나는 긴 텍스트입니다.\n" +
                                "LookaheadScope는 먼저 이 텍스트가 차지할\n" +
                                "전체 크기를 계산(Lookahead Pass)한 다음,\n" +
                                "부모 Box가 그 크기까지 부드럽게\n" +
                                "애니메이션(Main Pass) 되도록 합니다."
                    } else {
                        "짧은 텍스트."
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}