package com.ys.composeplayground.ui.sample.product.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.sample.product.components.MembershipBadge
import com.ys.composeplayground.ui.sample.product.components.VerticalSpaceColumn
import com.ys.composeplayground.ui.sample.product.components.VerticalSpacer
import com.ys.composeplayground.ui.sample.product.components.badge.BadgeModel
import com.ys.composeplayground.ui.sample.product.components.badge.Badges
import com.ys.composeplayground.ui.sample.product.components.badge.badges
import com.ys.composeplayground.ui.sample.product.components.ratingbar.StarRatingBar
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import com.ys.composeplayground.ui.theme.CustomTypography
import com.ys.composeplayground.ui.theme.Grey333
import com.ys.composeplayground.ui.theme.Navy500
import com.ys.composeplayground.ui.theme.Purple700
import com.ys.composeplayground.ui.theme.Red500

@Composable
fun ProductDetailScreen() {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .background(Color.White)
        .verticalScroll(scrollState)
    ) {
        ImageUnit()
        ProductBaseInfo()
        UnitPadding()
    }
}

@Composable
fun UnitPadding(height: Dp = 10.dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Color.Gray)
    )
}

@Composable
fun ImageUnit() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(50.dp),
            text = "Image Unit"
        )
    }
}

@Composable
fun ProductBaseInfo() {
    val productBadges = badges
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
    ) {
        ProductInfoArtist()
        ProductInfoBadges(productBadges)
        MembershipBenefit()
    }
}

@Composable
fun ProductInfoArtist() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(24.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.ava1),
                contentDescription = "ArtistName"
            )
            Text(
                text = "캬옹이",
                style = CustomTypography.body3BoldSmall,
                color = Grey333,
            )
            Image(
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Color.Gray),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "arrow right"
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            StarRatingBar(
                rating = 4.3f,
                startSize = 12f,
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "4.8",
                style = CustomTypography.body1BoldSmall,
                color = Navy500,
            )
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = "(521)",
                style = CustomTypography.body1RegularSmall,
                color = Navy500,
            )
            Image(
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Color.Gray),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "arrow right"
            )
        }
    }
}

@Composable
fun ProductInfoBadges(productBadges: List<BadgeModel>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Badges(
            badges = productBadges,
            badgeSpace = 4.dp
        )
    }
}

@Composable
fun MembershipBenefit() {

    val lazyListState = rememberLazyListState()

    VerticalSpaceColumn {
        Column(modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF55898),
                        Color(0xFF5352E7),
                        Color(0xFF11A1E5),
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "멤버십 가입시 혜택가",
                    color = Grey333,
                    style = CustomTypography.body1BoldSmall,
                )

                Text(
                    text = "65,000원",
                    color = Red500,
                    style = CustomTypography.body1BoldSmall,
                )
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
            ) {
                VerticalSpacer(20.dp)
                MembershipBenefitItem()
                VerticalSpacer(14.dp)
                MembershipBenefitItem()
                VerticalSpacer(14.dp)
                MembershipBenefitItem()
            }
        }
    }
}

@Composable
fun MembershipBenefitItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MembershipBadge {
            Text(
                text = "d+ 멤버십",
                color = Color.White,
                style = CustomTypography.caption1BoldSmall,
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp),
            text = "5% 추가할인",
            color = Grey333,
            style = CustomTypography.body2RegularSmall,
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "-10,000원",
            color = Grey333,
            style = CustomTypography.body2RegularSmall,
        )
    }
}

@Preview
@Composable
fun PreviewMembershipBenefitItem() {
    ComposePlaygroundTheme {
        MembershipBenefitItem()
    }
}


@Preview
@Composable
fun PreviewProductDetailScreen() {
    ComposePlaygroundTheme {
        ProductDetailScreen()
    }
}
