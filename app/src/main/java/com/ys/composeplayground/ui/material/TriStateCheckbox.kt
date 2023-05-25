package com.ys.composeplayground.ui.material

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * TriStateCheckbox
 *
 * @Composable
 * fun TriStateCheckbox(
 *     state: ToggleableState,
 *     onClick: (() -> Unit)?,
 *     modifier: Modifier = Modifier,
 *     enabled: Boolean = true,
 *     colors: CheckboxColors = CheckboxDefaults.colors(),
 *     interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
 * )
 *
 * Parameters
 * state
 * 이 확인란이 선택되어 있는지, 선택되지 않았는지 또는 불확실한 상태인지 여부를 나타냅니다.
 *
 * onClick
 * 이 체크박스를 클릭할 때 호출됩니다. null`이면 이 체크박스는 다른 요소가 입력 이벤트를 처리하고 상태를 업데이트하지 않는 한 상호 작용할 수 없습니다.
 *
 * modifier
 * 이 체크박스에 적용할 모디파이어입니다.
 *
 * enabled
 * 는 이 체크박스의 활성화 상태를 제어합니다. false`일 경우 이 컴포넌트는 사용자 입력에 응답하지 않으며 시각적으로 비활성화되고 접근성 서비스에서 사용할 수 없게 표시됩니다.
 *
 * colors
 * 이 체크박스에 사용되는 색상을 다른 상태로 해결하는 데 사용되는 CheckboxColors입니다. CheckboxDefaults.colors를 참조하세요.
 *
 * interactionSource
 * 이 체크박스에 대한 인터랙션의 스트림을 나타내는 뮤터블인터랙션소스입니다. 인터랙션을 관찰하고 다른 상태에서 이 체크박스의 모양/동작을 사용자 정의하기 위해 자신만의 '기억'된 인스턴스를 생성하고 전달할 수 있습니다.
 */

@Composable
fun TriStateCheckboxSample() {
    Column {
        val (state, onStateChange) = remember { mutableStateOf(true) }
        val (state2, onStateChange2) = remember { mutableStateOf(true) }

        val parentState = remember(state, state2) {
            if (state && state2) ToggleableState.On
            else if (!state && !state2) ToggleableState.Off
            else ToggleableState.Indeterminate
        }

        val onParentClick = {
            val s = parentState != ToggleableState.On
            onStateChange(s)
            onStateChange2(s)
        }

        TriStateCheckbox(
            state = parentState,
            onClick = onParentClick
        )
        Spacer(modifier = Modifier.size(25.dp))
        Column(
            modifier = Modifier
                .padding(10.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Checkbox(checked = state, onCheckedChange = onStateChange)
            Spacer(modifier = Modifier.size(25.dp))
            Checkbox(checked = state2, onCheckedChange = onStateChange2)
        }
    }
}

@Preview
@Composable
fun TriStateCheckboxSamplePreview() {
    TriStateCheckboxSample()
}