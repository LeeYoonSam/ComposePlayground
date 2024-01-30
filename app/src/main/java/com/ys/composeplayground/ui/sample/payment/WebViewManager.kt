package com.ys.composeplayground.ui.sample.payment

import android.webkit.WebView

class WebViewManager(
    private val webView: WebView,
    private val paymentSuccess: () -> Unit,
    private val paymentCancel: (exception: Exception?) -> Unit,
) {
    fun loadPaymentUrl(paymentUrl: String) {
        webView.loadUrl(paymentUrl)
    }

    private fun paymentResult(url: String) {
        try {
            if (url.contains("domain/payment/result/success")) {
                paymentSuccess()
            } else if (url.contains("domain/payment/result/fail")) {
                paymentCancel(null)
            }
        } catch (e: Exception) {
            paymentCancel(e)
        }
    }
}