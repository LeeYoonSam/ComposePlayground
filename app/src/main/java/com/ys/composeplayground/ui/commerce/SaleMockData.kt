package com.ys.composeplayground.ui.commerce

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CardMembership
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.Color

/**
 * 세일 탭 목록 (6개)
 */
val saleTabs = listOf(
    SaleTab("랭킹"),
    SaleTab("추천"),
    SaleTab("세일"),
    SaleTab("쿠폰"),
    SaleTab("특가"),
    SaleTab("신상품")
)

/**
 * 카테고리 목록 (8개)
 */
val saleCategories = listOf(
    CategoryItem(
        id = 1,
        name = "전체",
        icon = Icons.Outlined.GridView,
        iconBackgroundColor = CategoryBgPink
    ),
    CategoryItem(
        id = 2,
        name = "타임특가",
        icon = Icons.Outlined.FlashOn,
        iconBackgroundColor = CategoryBgPurple
    ),
    CategoryItem(
        id = 3,
        name = "멤버십",
        icon = Icons.Outlined.CardMembership,
        iconBackgroundColor = CategoryBgBlue
    ),
    CategoryItem(
        id = 4,
        name = "오늘발송",
        icon = Icons.Outlined.CalendarMonth,
        iconBackgroundColor = CategoryBgGreen
    ),
    CategoryItem(
        id = 5,
        name = "한정수량",
        icon = Icons.Outlined.Timer,
        iconBackgroundColor = CategoryBgYellow
    ),
    CategoryItem(
        id = 6,
        name = "뷰티",
        icon = Icons.Outlined.Spa,
        iconBackgroundColor = CategoryBgOrange
    ),
    CategoryItem(
        id = 7,
        name = "브랜드관",
        icon = Icons.Outlined.Store,
        iconBackgroundColor = CategoryBgMint
    ),
    CategoryItem(
        id = 8,
        name = "신상품",
        icon = Icons.Outlined.NewReleases,
        iconBackgroundColor = CategoryBgLavender
    )
)

/**
 * 필터 칩 목록 (7개)
 */
val saleFilterChips = listOf(
    SaleFilterChip(
        id = 1,
        label = "필터",
        leadingIcon = Icons.Outlined.GridView,
        hasDropdown = false
    ),
    SaleFilterChip(
        id = 2,
        label = "배송",
        hasDropdown = true
    ),
    SaleFilterChip(
        id = 3,
        label = "가격",
        hasDropdown = true
    ),
    SaleFilterChip(
        id = 4,
        label = "혜택",
        hasDropdown = true
    ),
    SaleFilterChip(
        id = 5,
        label = "브랜드",
        hasDropdown = true
    ),
    SaleFilterChip(
        id = 6,
        label = "평점",
        hasDropdown = true
    ),
    SaleFilterChip(
        id = 7,
        label = "카테고리",
        hasDropdown = true
    )
)

/**
 * 세일 상품 목록 (24개)
 */
val saleProducts = listOf(
    SaleProduct(
        id = 1,
        name = "프리미엄 무선 이어폰 노이즈캔슬링",
        sellerName = "오디오마켓",
        originalPrice = 189000,
        discountPercent = 47,
        salePrice = 99900,
        membershipPrice = 94900,
        badges = listOf(ProductBadge.NEW, ProductBadge.COUPON),
        rating = 4.8f,
        reviewCount = 2341,
        reviewPreview = "음질이 정말 좋아요! 노캔 성능도 훌륭합니다",
        imageUrl = "https://picsum.photos/seed/1/400/400",
        imageColor = Color(0xFFFFE8F0)
    ),
    SaleProduct(
        id = 2,
        name = "스마트워치 GPS 심박측정",
        sellerName = "테크스토어",
        originalPrice = 349000,
        discountPercent = 60,
        salePrice = 139000,
        membershipPrice = 129000,
        badges = listOf(ProductBadge.COUPON, ProductBadge.FREE_SHIPPING),
        rating = 4.6f,
        reviewCount = 1823,
        reviewPreview = "가성비 최고! 기능도 충분합니다",
        imageUrl = "https://picsum.photos/seed/2/400/400",
        imageColor = Color(0xFFF0E8FF)
    ),
    SaleProduct(
        id = 3,
        name = "휴대용 블루투스 스피커 IPX7 방수",
        sellerName = "사운드존",
        originalPrice = 129000,
        discountPercent = 38,
        salePrice = 79900,
        membershipPrice = null,
        badges = listOf(ProductBadge.NEW),
        rating = 4.7f,
        reviewCount = 892,
        reviewPreview = "방수 기능 좋고 음질도 깔끔해요",
        imageUrl = "https://picsum.photos/seed/3/400/400",
        imageColor = Color(0xFFE8F4FF)
    ),
    SaleProduct(
        id = 4,
        name = "프리미엄 기계식 키보드 RGB",
        sellerName = "게이밍기어",
        originalPrice = 159000,
        discountPercent = 43,
        salePrice = 89900,
        membershipPrice = 84900,
        badges = listOf(ProductBadge.COUPON),
        rating = 4.9f,
        reviewCount = 3102,
        reviewPreview = "타건감 최고! RGB 효과도 예뻐요",
        imageUrl = "https://picsum.photos/seed/4/400/400",
        imageColor = Color(0xFFE8FFF4)
    ),
    SaleProduct(
        id = 5,
        name = "무선 충전 패드 고속충전",
        sellerName = "충전마스터",
        originalPrice = 59000,
        discountPercent = 50,
        salePrice = 29500,
        membershipPrice = 27500,
        badges = listOf(ProductBadge.FREE_SHIPPING),
        rating = 4.5f,
        reviewCount = 1567,
        reviewPreview = "충전 속도 빠르고 발열도 적어요",
        imageUrl = "https://picsum.photos/seed/5/400/400",
        imageColor = Color(0xFFFFFBE8)
    ),
    SaleProduct(
        id = 6,
        name = "HD 웹캠 1080P 마이크 내장",
        sellerName = "캠프로",
        originalPrice = 89000,
        discountPercent = 33,
        salePrice = 59000,
        membershipPrice = null,
        badges = listOf(ProductBadge.NEW, ProductBadge.FREE_SHIPPING),
        rating = 4.4f,
        reviewCount = 723,
        reviewPreview = "화질 선명하고 설치 간편합니다",
        imageUrl = "https://picsum.photos/seed/6/400/400",
        imageColor = Color(0xFFFFF0E8)
    ),
    SaleProduct(
        id = 7,
        name = "무선 마우스 게이밍 RGB",
        sellerName = "게이밍존",
        originalPrice = 79000,
        discountPercent = 44,
        salePrice = 44000,
        membershipPrice = 41000,
        badges = listOf(ProductBadge.COUPON),
        rating = 4.7f,
        reviewCount = 2134,
        reviewPreview = "손에 잘 맞고 반응속도 빠름",
        imageUrl = "https://picsum.photos/seed/7/400/400",
        imageColor = Color(0xFFE8FFFF)
    ),
    SaleProduct(
        id = 8,
        name = "USB 허브 멀티포트 7in1",
        sellerName = "액세서리몰",
        originalPrice = 45000,
        discountPercent = 40,
        salePrice = 27000,
        membershipPrice = 25000,
        badges = listOf(ProductBadge.FREE_SHIPPING),
        rating = 4.6f,
        reviewCount = 1456,
        reviewPreview = "포트 많고 안정적이에요",
        imageUrl = "https://picsum.photos/seed/8/400/400",
        imageColor = Color(0xFFF4E8FF)
    ),
    SaleProduct(
        id = 9,
        name = "보조배터리 20000mAh 고속충전",
        sellerName = "파워뱅크",
        originalPrice = 69000,
        discountPercent = 42,
        salePrice = 39900,
        membershipPrice = 37900,
        badges = listOf(ProductBadge.NEW, ProductBadge.COUPON),
        rating = 4.8f,
        reviewCount = 3421,
        reviewPreview = "용량 크고 충전 빨라요",
        imageUrl = "https://picsum.photos/seed/9/400/400",
        imageColor = Color(0xFFFFE8F0)
    ),
    SaleProduct(
        id = 10,
        name = "노트북 거치대 알루미늄",
        sellerName = "오피스플러스",
        originalPrice = 39000,
        discountPercent = 35,
        salePrice = 25000,
        membershipPrice = null,
        badges = listOf(ProductBadge.FREE_SHIPPING),
        rating = 4.5f,
        reviewCount = 891,
        reviewPreview = "각도 조절 편하고 튼튼해요",
        imageUrl = "https://picsum.photos/seed/10/400/400",
        imageColor = Color(0xFFF0E8FF)
    ),
    SaleProduct(
        id = 11,
        name = "무선 충전 차량용 거치대",
        sellerName = "카액세서리",
        originalPrice = 55000,
        discountPercent = 45,
        salePrice = 30000,
        membershipPrice = 28000,
        badges = listOf(ProductBadge.COUPON),
        rating = 4.6f,
        reviewCount = 1234,
        reviewPreview = "차에서 사용하기 편리합니다",
        imageUrl = "https://picsum.photos/seed/11/400/400",
        imageColor = Color(0xFFE8F4FF)
    ),
    SaleProduct(
        id = 12,
        name = "태블릿 스탠드 접이식",
        sellerName = "스마트몰",
        originalPrice = 29000,
        discountPercent = 31,
        salePrice = 19900,
        membershipPrice = 18900,
        badges = listOf(ProductBadge.NEW),
        rating = 4.4f,
        reviewCount = 567,
        reviewPreview = "접어서 휴대하기 좋아요",
        imageUrl = "https://picsum.photos/seed/12/400/400",
        imageColor = Color(0xFFE8FFF4)
    ),
    SaleProduct(
        id = 13,
        name = "LED 데스크 조명 USB 충전",
        sellerName = "라이팅코리아",
        originalPrice = 49000,
        discountPercent = 39,
        salePrice = 29900,
        membershipPrice = 27900,
        badges = listOf(ProductBadge.COUPON, ProductBadge.FREE_SHIPPING),
        rating = 4.7f,
        reviewCount = 1678,
        reviewPreview = "밝기 조절 좋고 디자인 깔끔",
        imageUrl = "https://picsum.photos/seed/13/400/400",
        imageColor = Color(0xFFFFFBE8)
    ),
    SaleProduct(
        id = 14,
        name = "블루투스 트랙패드 무선",
        sellerName = "입력장치몰",
        originalPrice = 79000,
        discountPercent = 37,
        salePrice = 49000,
        membershipPrice = null,
        badges = listOf(ProductBadge.NEW),
        rating = 4.5f,
        reviewCount = 823,
        reviewPreview = "맥북처럼 사용 가능해요",
        imageUrl = "https://picsum.photos/seed/14/400/400",
        imageColor = Color(0xFFFFF0E8)
    ),
    SaleProduct(
        id = 15,
        name = "케이블 정리함 5구",
        sellerName = "정리왕",
        originalPrice = 19000,
        discountPercent = 47,
        salePrice = 9900,
        membershipPrice = 8900,
        badges = listOf(ProductBadge.FREE_SHIPPING),
        rating = 4.3f,
        reviewCount = 456,
        reviewPreview = "책상 정리에 딱이에요",
        imageUrl = "https://picsum.photos/seed/15/400/400",
        imageColor = Color(0xFFE8FFFF)
    ),
    SaleProduct(
        id = 16,
        name = "USB-C 케이블 100W 고속충전",
        sellerName = "케이블프로",
        originalPrice = 25000,
        discountPercent = 40,
        salePrice = 15000,
        membershipPrice = 14000,
        badges = listOf(ProductBadge.COUPON),
        rating = 4.8f,
        reviewCount = 2891,
        reviewPreview = "내구성 좋고 충전 빨라요",
        imageUrl = "https://picsum.photos/seed/16/400/400",
        imageColor = Color(0xFFF4E8FF)
    ),
    SaleProduct(
        id = 17,
        name = "모니터 암 듀얼 거치대",
        sellerName = "모니터존",
        originalPrice = 89000,
        discountPercent = 44,
        salePrice = 49900,
        membershipPrice = 46900,
        badges = listOf(ProductBadge.NEW, ProductBadge.FREE_SHIPPING),
        rating = 4.7f,
        reviewCount = 1345,
        reviewPreview = "설치 간편하고 튼튼합니다",
        imageUrl = "https://picsum.photos/seed/17/400/400",
        imageColor = Color(0xFFFFE8F0)
    ),
    SaleProduct(
        id = 18,
        name = "메모리폼 손목받침대",
        sellerName = "편안한작업실",
        originalPrice = 29000,
        discountPercent = 34,
        salePrice = 19000,
        membershipPrice = null,
        badges = listOf(ProductBadge.COUPON),
        rating = 4.6f,
        reviewCount = 789,
        reviewPreview = "손목이 편해졌어요",
        imageUrl = "https://picsum.photos/seed/18/400/400",
        imageColor = Color(0xFFF0E8FF)
    ),
    SaleProduct(
        id = 19,
        name = "무선 프레젠터 레이저 포인터",
        sellerName = "프레젠몰",
        originalPrice = 45000,
        discountPercent = 42,
        salePrice = 26000,
        membershipPrice = 24000,
        badges = listOf(ProductBadge.FREE_SHIPPING),
        rating = 4.5f,
        reviewCount = 634,
        reviewPreview = "발표할 때 유용해요",
        imageUrl = "https://picsum.photos/seed/19/400/400",
        imageColor = Color(0xFFE8F4FF)
    ),
    SaleProduct(
        id = 20,
        name = "스마트폰 삼각대 블루투스 리모컨",
        sellerName = "셀카마스터",
        originalPrice = 35000,
        discountPercent = 37,
        salePrice = 21900,
        membershipPrice = 20900,
        badges = listOf(ProductBadge.NEW, ProductBadge.COUPON),
        rating = 4.7f,
        reviewCount = 1567,
        reviewPreview = "영상 촬영에 최고예요",
        imageUrl = "https://picsum.photos/seed/20/400/400",
        imageColor = Color(0xFFE8FFF4)
    ),
    SaleProduct(
        id = 21,
        name = "USB 선풍기 저소음",
        sellerName = "쿨링존",
        originalPrice = 19000,
        discountPercent = 36,
        salePrice = 12000,
        membershipPrice = 11000,
        badges = listOf(ProductBadge.FREE_SHIPPING),
        rating = 4.4f,
        reviewCount = 923,
        reviewPreview = "소음 없고 시원해요",
        imageUrl = "https://picsum.photos/seed/21/400/400",
        imageColor = Color(0xFFFFFBE8)
    ),
    SaleProduct(
        id = 22,
        name = "노트북 파우치 방수 13인치",
        sellerName = "백패킹",
        originalPrice = 25000,
        discountPercent = 40,
        salePrice = 15000,
        membershipPrice = null,
        badges = listOf(ProductBadge.COUPON),
        rating = 4.6f,
        reviewCount = 1234,
        reviewPreview = "두툼하고 보호 잘 돼요",
        imageUrl = "https://picsum.photos/seed/22/400/400",
        imageColor = Color(0xFFFFF0E8)
    ),
    SaleProduct(
        id = 23,
        name = "HDMI 케이블 4K 3m",
        sellerName = "영상기기",
        originalPrice = 29000,
        discountPercent = 48,
        salePrice = 14900,
        membershipPrice = 13900,
        badges = listOf(ProductBadge.NEW),
        rating = 4.8f,
        reviewCount = 2156,
        reviewPreview = "화질 깨끗하고 길이 적당",
        imageUrl = "https://picsum.photos/seed/23/400/400",
        imageColor = Color(0xFFE8FFFF)
    ),
    SaleProduct(
        id = 24,
        name = "무선 이어폰 케이스 실리콘",
        sellerName = "케이스샵",
        originalPrice = 15000,
        discountPercent = 33,
        salePrice = 9900,
        membershipPrice = 8900,
        badges = listOf(ProductBadge.FREE_SHIPPING),
        rating = 4.5f,
        reviewCount = 678,
        reviewPreview = "재질 부드럽고 예뻐요",
        imageUrl = "https://picsum.photos/seed/24/400/400",
        imageColor = Color(0xFFF4E8FF)
    )
)
