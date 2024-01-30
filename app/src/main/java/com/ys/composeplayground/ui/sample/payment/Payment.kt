package com.ys.composeplayground.ui.sample.payment

interface Payment {
    fun initiatePayment(paymentType: PaymentType, paymentUrl: String)
    fun cancelPayment(paymentType: PaymentType)
    fun handlePaymentFailure(paymentType: PaymentType, redirectionUrl: String)
}