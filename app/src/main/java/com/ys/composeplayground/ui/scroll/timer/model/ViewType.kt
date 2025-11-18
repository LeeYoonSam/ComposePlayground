package com.ys.composeplayground.ui.scroll.timer.model

/**
 * RecyclerView 및 LazyColumn에서 사용할 뷰 타입
 */
enum class ViewType {
    HEADER,          // 제목 영역 (상품명, 가격 등)
    IMAGE_GALLERY,   // 이미지 갤러리
    BADGE,           // 뱃지 영역
    DESCRIPTION,     // 상품 설명
    TIMER_BANNER,    // 타이머 배너
    PRODUCT_SPEC,    // 상품 스펙
    DIVIDER,         // 구분선
    REVIEW,          // 후기 미리보기
    VIDEO_PLAYER,    // 동영상 플레이어
    RELATED_PRODUCT  // 연관 상품
}

/**
 * 각 뷰타입에 대한 더미 데이터
 */
data class ScrollTimerItem(
    val viewType: ViewType,
    val data: Any? = null
)

/**
 * 더미 데이터 생성
 */
fun createDummyItems(): List<ScrollTimerItem> {
    return listOf(
        // 헤더
        ScrollTimerItem(ViewType.HEADER, "핸드메이드 프리미엄 가죽 지갑"),

        // 이미지 갤러리
        ScrollTimerItem(ViewType.IMAGE_GALLERY, listOf(
            "https://picsum.photos/400/400?random=1",
            "https://picsum.photos/400/400?random=2",
            "https://picsum.photos/400/400?random=3",
            "https://picsum.photos/400/400?random=4",
            "https://picsum.photos/400/400?random=5"
        )),

        // 뱃지 영역
        ScrollTimerItem(ViewType.BADGE, listOf("베스트셀러", "무료배송", "한정판", "친환경")),

        // 타이머 배너
        ScrollTimerItem(ViewType.TIMER_BANNER, TimerState()),

        // 상품 설명 1
        ScrollTimerItem(ViewType.DESCRIPTION,
            "이탈리아산 최고급 풀그레인 가죽으로 제작된 프리미엄 지갑입니다. " +
            "장인의 손길이 느껴지는 섬세한 스티칭과 완벽한 마감 처리로 고급스러움을 더했습니다. " +
            "시간이 지날수록 자연스러운 에이징이 진행되어 나만의 독특한 멋을 만들어갑니다. " +
            "총 8개의 카드 슬롯과 지폐 수납공간, 동전 지퍼 포켓으로 실용성까지 갖췄습니다."),

        // 구분선
        ScrollTimerItem(ViewType.DIVIDER, "제품 상세 정보"),

        // 상품 스펙
        ScrollTimerItem(ViewType.PRODUCT_SPEC, mapOf(
            "소재" to "이탈리아산 풀그레인 가죽",
            "크기" to "19cm x 10cm x 2.5cm",
            "무게" to "약 180g",
            "색상" to "다크 브라운, 블랙, 버건디",
            "카드슬롯" to "8개",
            "제조국" to "대한민국 (수공예)",
            "A/S" to "1년 무상 수선"
        )),

        // 동영상 플레이어
        ScrollTimerItem(ViewType.VIDEO_PLAYER, "제품 소개 영상"),

        // 상품 설명 2
        ScrollTimerItem(ViewType.DESCRIPTION,
            "【장인의 철학】\n\n" +
            "20년 경력의 가죽 장인이 하나하나 정성스럽게 제작합니다. " +
            "대량생산이 아닌 소량 주문 제작 방식으로 품질을 최우선으로 합니다. " +
            "모든 제품은 출고 전 3단계 품질 검수를 거쳐 완벽한 상태로 배송됩니다."),

        // 구분선
        ScrollTimerItem(ViewType.DIVIDER, "고객 후기"),

        // 후기 1
        ScrollTimerItem(ViewType.REVIEW,
            "⭐⭐⭐⭐⭐ 5.0\n\n" +
            "생일 선물로 구매했는데 퀄리티가 정말 좋네요! " +
            "가죽 냄새도 좋고 마감도 깔끔합니다. 포장도 정성스럽게 해주셔서 감동이었어요. " +
            "- 김**님"),

        // 후기 2
        ScrollTimerItem(ViewType.REVIEW,
            "⭐⭐⭐⭐⭐ 5.0\n\n" +
            "3년째 사용중인데 에이징 진행되는게 너무 멋있어요. " +
            "처음보다 더 멋있어지는 느낌? 관리만 잘하면 평생 쓸 것 같습니다. " +
            "- 이**님"),

        // 상품 설명 3 (관리 방법)
        ScrollTimerItem(ViewType.DESCRIPTION,
            "【관리 방법】\n\n" +
            "• 가죽 전용 크림으로 월 1회 관리 권장\n" +
            "• 물에 젖었을 경우 자연 건조\n" +
            "• 직사광선과 고온 다습한 환경 피하기\n" +
            "• 오염 시 부드러운 천으로 가볍게 닦기"),

        // 후기 3
        ScrollTimerItem(ViewType.REVIEW,
            "⭐⭐⭐⭐⭐ 5.0\n\n" +
            "남편 크리스마스 선물로 샀어요. 명품 부럽지 않은 퀄리티입니다. " +
            "무엇보다 AS가 1년이나 되는게 마음에 들어요. 강추합니다! " +
            "- 박**님"),

        // 구분선
        ScrollTimerItem(ViewType.DIVIDER, "배송 안내"),

        // 배송 정보
        ScrollTimerItem(ViewType.DESCRIPTION,
            "• 평일 오후 2시 이전 주문 시 당일 발송\n" +
            "• 택배 배송 (CJ대한통운)\n" +
            "• 제주/도서산간 지역 추가 배송비 3,000원\n" +
            "• 수령 후 7일 이내 교환/환불 가능 (미사용 시)"),

        // 후기 4
        ScrollTimerItem(ViewType.REVIEW,
            "⭐⭐⭐⭐ 4.5\n\n" +
            "색상이 사진보다 약간 더 진한 느낌이지만 실물이 더 고급스러워요. " +
            "카드가 많은 분들은 조금 두툼할 수 있으니 참고하세요. " +
            "- 최**님"),

        // 연관 상품
        ScrollTimerItem(ViewType.RELATED_PRODUCT, listOf(
            "가죽 카드 지갑" to "₩35,000",
            "프리미엄 키홀더" to "₩25,000",
            "가죽 벨트" to "₩68,000"
        )),

        // 후기 5
        ScrollTimerItem(ViewType.REVIEW,
            "⭐⭐⭐⭐⭐ 5.0\n\n" +
            "재구매입니다! 첫 번째 지갑이 너무 좋아서 색상 다르게 하나 더 샀어요. " +
            "이 가격에 이런 퀄리티면 정말 혜자입니다. " +
            "- 정**님"),

        // 상품 설명 4 (브랜드 스토리)
        ScrollTimerItem(ViewType.DESCRIPTION,
            "【브랜드 스토리】\n\n" +
            "작은 공방에서 시작한 우리의 이야기는 '완벽한 하나'를 만들고자 하는 " +
            "열정에서 출발했습니다. 빠르게 만드는 것이 아닌, 오래 쓸 수 있는 " +
            "제품을 만들기 위해 노력합니다. 고객 한 분 한 분께 최선을 다하는 " +
            "마음으로 매일 작업합니다."),

        // 후기 6
        ScrollTimerItem(ViewType.REVIEW,
            "⭐⭐⭐⭐⭐ 5.0\n\n" +
            "가성비 최고예요! 백화점에서 파는 명품 브랜드 저리가라입니다. " +
            "친구들한테 자랑하고 다니는 중ㅋㅋ 배송도 빠르고 포장도 예뻐서 좋았어요. " +
            "- 강**님"),
    )
}
