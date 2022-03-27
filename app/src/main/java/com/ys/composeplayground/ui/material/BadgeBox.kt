package com.ys.composeplayground.ui.material

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

/**
 * BadgeBox
 *
 * BadgeBox 는 material 1.1.1 에 등장
 *  - dependency - implementation "androidx.compose.material:material:1.1.1"
 *
 * BadgeBox는 새 알림의 존재 또는 보류 중인 요청의 수와 같은 동적 정보를 포함할 수 있는 배지로 콘텐츠를 장식하는 데 사용됩니다.
 * 배지는 아이콘만 가능하거나 짧은 텍스트를 포함할 수 있습니다.
 *
 * 일반적인 사용 사례는 Bottom Navigation 항목과 함께 배지를 표시하는 것입니다.
 *
 * 자세한 내용은 Bottom Navigation을 참조하세요.
 * 배지 예제가 있는 간단한 아이콘은 다음과 같습니다.
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BadgeBoxDemo() {

    val badgeCount = remember { mutableStateOf(0) }

    BottomNavigation {
        BottomNavigationItem(
            icon = {
                //
                BadgedBox(badge = { Badge { Text(badgeCount.value.toString()) } }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite"
                    )
                }
            },
            selected = false,
            onClick = { badgeCount.value += 1 }
        )
    }
}

@Preview
@Composable
fun PreviewBadgeBox() {
    BadgeBoxDemo()
}

