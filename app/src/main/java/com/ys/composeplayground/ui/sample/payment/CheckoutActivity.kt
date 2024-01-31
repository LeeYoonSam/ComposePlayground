package com.ys.composeplayground.ui.sample.payment

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposePlaygroundTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { moveToPayment(PaymentType.PRODUCT) }
                    ) {
                        Text(text = "작품 결제하기")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { moveToPayment(PaymentType.GIFT) }
                    ) {
                        Text(text = "선물 결제하기")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { moveToPayment(PaymentType.GIFT_CARD) }
                    ) {
                        Text(text = "기프트카드 결제하기")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { moveToPayment(PaymentType.DONATION) }
                    ) {
                        Text(text = "후원 결제하기")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { moveToPayment(PaymentType.CLASS) }
                    ) {
                        Text(text = "클래스 결제하기")
                    }
                }
            }
        }
    }

    private fun moveToPayment(paymentType: PaymentType) {
        when (paymentType) {
            PaymentType.PRODUCT -> PaymentActivity.start(this)
            PaymentType.GIFT -> PaymentActivity.start(this)
            PaymentType.GIFT_CARD -> PaymentActivity.start(this)
            PaymentType.DONATION -> PaymentActivity.start(this)
            PaymentType.CLASS -> PaymentActivity.start(this)
        }
    }
}