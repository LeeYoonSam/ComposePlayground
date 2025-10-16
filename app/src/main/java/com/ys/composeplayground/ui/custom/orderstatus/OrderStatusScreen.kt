package com.ys.composeplayground.ui.custom.orderstatus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 주문 상태 트래커 데모 화면
 *
 * 이 화면은 다양한 주문 상태를 시각적으로 확인할 수 있습니다.
 */
@Composable
fun OrderStatusScreen() {
    var currentStatus by remember { mutableStateOf(OrderStatus.PAYMENT_COMPLETED) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 타이틀
        Text(
            text = "주문 상태 트래커",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "주문의 현재 상태를 시각적으로 표시합니다.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 인터랙티브 데모
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "인터랙티브 데모",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "현재 상태: ${currentStatus.displayName}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                OrderStatusTracker(currentStatus = currentStatus)

                // 상태 변경 버튼들
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OrderStatus.getAllSteps().forEach { status ->
                        Button(
                            onClick = { currentStatus = status },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = currentStatus != status
                        ) {
                            Text("${status.displayName}로 변경")
                        }
                    }
                }
            }
        }

        // 모든 상태 프리뷰
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "모든 상태 프리뷰",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OrderStatus.getAllSteps().forEach { status ->
                    StatusPreviewCard(status = status)
                }
            }
        }

        // 컴포넌트 스펙 정보
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "컴포넌트 스펙",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                SpecItem(label = "전체 높이", value = "40dp")
                SpecItem(label = "배지 크기", value = "52x20dp")
                SpecItem(label = "프로그레스 노드", value = "12x12dp")
                SpecItem(label = "프로그레스 바 높이", value = "2dp")
                SpecItem(label = "활성 색상", value = "#EF7014")
                SpecItem(label = "비활성 색상", value = "#E0E0E0")
            }
        }
    }
}

/**
 * 개별 상태 프리뷰 카드
 */
@Composable
private fun StatusPreviewCard(status: OrderStatus) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = status.displayName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        OrderStatusTracker(currentStatus = status)
    }
}

/**
 * 스펙 아이템
 */
@Composable
private fun SpecItem(label: String, value: String) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 프리뷰: OrderStatusScreen
 */
@Preview(showBackground = true)
@Composable
fun PreviewOrderStatusScreen() {
    MaterialTheme {
        OrderStatusScreen()
    }
}
