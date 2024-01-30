package com.ys.composeplayground.ui.sample.payment

/**
 * 이 클래스는 전략을 선택하여 사용합니다.
 * 이를 통해 각 결제 유형에 특화된 기능을 캡슐화하고, 필요에 따라 동적으로 전략을 변경할 수 있도록 합니다.
 */
class ProductPaymentManager(
    private val paymentManager: PaymentManager,
    private val paymentUrl: String,
    private val paymentType: PaymentType = PaymentType.PRODUCT,
) {
    /**
     * 객체를 직접적으로 생성하는 것은 아니지만, 결제를 초기화하는 역할을 수행합니다.
     * 이러한 초기화 작업은 팩토리 메서드 패턴의 핵심 아이디어와 연관이 있습니다.
     *
     * 팩토리 메서드 패턴은 객체의 생성을 서브클래스로 미루는 패턴으로, 슈퍼클래스에서는 인터페이스를 정의하고, 서브클래스에서 이를 구현하여 객체 생성을 처리합니다.
     */
    fun initiatePayment() {
        paymentManager.initiatePayment(paymentType, paymentUrl)
    }

    fun cancelPayment(exception: Exception) {
        paymentManager.cancelPayment(paymentType)
    }

    fun handlePaymentFailure(redirectionUrl: String) {
        paymentManager.handlePaymentFailure(paymentType, redirectionUrl)
    }
}
