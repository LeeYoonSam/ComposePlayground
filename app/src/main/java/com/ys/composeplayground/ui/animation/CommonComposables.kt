package com.ys.composeplayground.ui.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DemoSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}

@Composable
fun DemoSectionWithBox(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            content()
        }
    }
}


@Composable
fun TitleSection(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Color(0xFF333333)
    )
}


@Composable
fun CodeSection(
    title: String,
    code: String
) {
    Column {
        TitleSection(title)

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .padding(12.dp)
        ) {
            Text(
                text = code,
                fontSize = 11.sp,
                color = Color(0xFF37474F),
                fontFamily = FontFamily.Monospace,
                lineHeight = 16.sp
            )
        }
    }
}

enum class FeatureTextType {
    TIP,
    CAUTION,
    NONE
}

@Composable
fun FeatureSection(
    features: String,
    customTitle: String? = null,
    type: FeatureTextType = FeatureTextType.NONE
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val title = when (type) {
            FeatureTextType.TIP -> "💡 팁"
            FeatureTextType.CAUTION -> "⚠️ 주의사항"
            else -> customTitle
        }

        title?.let {
            val titleColor = when (type) {
                FeatureTextType.TIP -> Color(0xFF1976D2)
                FeatureTextType.CAUTION -> Color(0xFFE65100)
                else -> Color(0xFF333333)
            }
            Text(
                text = it,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = titleColor
            )
        }

        Text(
            text = features,
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )
    }
}