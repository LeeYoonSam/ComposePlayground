package com.ys.composeplayground.ui.sample.payment

/**
 * 전략 패턴의 전략에 해당
 *
 * 전략 패턴은 알고리즘을 정의하고 각각을 캡슐화하여 상호교환이 가능하도록 만드는 디자인 패턴입니다.
 */
class PaymentManager(
    private val webViewManager: WebViewManager,
    private val logger: Logger,
) : Payment {
    override fun initiatePayment(paymentType: PaymentType, paymentUrl: String) {
        // 결제 초기화 및 관련 로직
        logger.log("Payment initiated for $paymentType")
        webViewManager.loadPaymentUrl(paymentUrl)
    }

    override fun cancelPayment(paymentType: PaymentType) {
        // 결제 취소 및 관련 로직
        logger.log("Payment canceled for $paymentType")
        // 취소에 따른 추가 로직 수행 가능
    }

    override fun handlePaymentFailure(paymentType: PaymentType, redirectionUrl: String) {
        // 결제 실패 및 관련 로직
        logger.log("Payment failed for $paymentType with error: $redirectionUrl")
        // 실패에 따른 추가 로직 수행 가능
    }
}