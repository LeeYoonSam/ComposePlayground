package com.ys.composeplayground.ui.commerce

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ys.composeplayground.ui.sample.product.components.MembershipBadge
import com.ys.composeplayground.ui.sample.product.components.badge.Badge
import com.ys.composeplayground.ui.sample.product.components.badge.BadgeModel
import com.ys.composeplayground.ui.sample.product.components.ratingbar.StarRatingBar
import com.ys.composeplayground.ui.theme.Blue100
import com.ys.composeplayground.ui.theme.Blue500
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import com.ys.composeplayground.ui.theme.CustomTypography
import com.ys.composeplayground.ui.theme.Grey100
import com.ys.composeplayground.ui.theme.Grey666
import com.ys.composeplayground.ui.theme.Grey900
import com.ys.composeplayground.ui.theme.Navy50
import com.ys.composeplayground.ui.theme.Navy500
import java.text.NumberFormat
import java.util.Locale

/**
 * 세일 상품 카드
 * - 이미지: AsyncImage (Coil), 1:1 비율, RoundedCornerShape 8dp
 * - 찜하기: 우측 하단 28dp 원형 하트 버튼 + scale 애니메이션
 * - 할인율 + 가격 같은 Row
 * - 멤버십 혜택가: MembershipBadge 재사용
 * - 배지: Badge 재사용
 * - 별점: StarRatingBar 재사용
 * - 리뷰 미리보기: 12sp Grey666
 */
@Composable
fun ProductCard(
    product: SaleProduct,
    onProductClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(false) }
    val heartScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 300f
        ),
        label = "heart_scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
    ) {
        // 상품 이미지 + 찜하기 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .size(400)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(product.imageColor)
            )

            // 찜하기 버튼
            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(28.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
                    .scale(heartScale)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "찜하기",
                    tint = if (isFavorite) SaleRed else Grey666,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 배지들
        if (product.badges.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                product.badges.forEach { badge ->
                    Badge(badgeModel = badge.toBadgeModel())
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // 상품명
        Text(
            text = product.name,
            style = CustomTypography.body1RegularSmall,
            color = Grey900,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 할인율 + 판매가
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${product.discountPercent}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SaleRed
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = formatPrice(product.salePrice),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Grey900
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // 정가 (취소선)
        Text(
            text = formatPrice(product.originalPrice),
            style = CustomTypography.caption2RegularSmall,
            color = Grey666,
            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
        )

        // 멤버십 혜택가
        product.membershipPrice?.let { membershipPrice ->
            Spacer(modifier = Modifier.height(4.dp))
            MembershipBadge {
                Text(
                    text = "멤버십 ${formatPrice(membershipPrice)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 별점 + 리뷰 수
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            StarRatingBar(
                rating = product.rating,
                startSize = 12f,
                spacing = 2f
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "(${NumberFormat.getNumberInstance(Locale.KOREA).format(product.reviewCount)})",
                fontSize = 11.sp,
                color = Grey666
            )
        }

        // 리뷰 미리보기
        product.reviewPreview?.let { preview ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = preview,
                fontSize = 12.sp,
                color = Grey666,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * ProductBadge를 BadgeModel로 변환
 */
private fun ProductBadge.toBadgeModel(): BadgeModel {
    return when (this) {
        ProductBadge.NEW -> BadgeModel(
            badgeText = "NEW",
            badgeTextColor = BadgeNewText,
            backgroundColor = BadgeNewBg
        )
        ProductBadge.COUPON -> BadgeModel(
            badgeText = "쿠폰",
            badgeTextColor = BadgeCouponText,
            backgroundColor = BadgeCouponBg
        )
        ProductBadge.FREE_SHIPPING -> BadgeModel(
            badgeText = "무료배송",
            badgeTextColor = BadgeFreeShippingText,
            backgroundColor = BadgeFreeShippingBg
        )
    }
}

/**
 * 가격 포맷팅 (천 단위 콤마)
 */
private fun formatPrice(price: Int): String {
    return "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원"
}

@Preview
@Composable
fun PreviewProductCard() {
    ComposePlaygroundTheme {
        Box(
            modifier = Modifier
                .width(180.dp)
                .padding(8.dp)
        ) {
            ProductCard(
                product = saleProducts[0],
                onProductClick = {}
            )
        }
    }
}
