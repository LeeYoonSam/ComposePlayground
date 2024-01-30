package com.ys.composeplayground.ui.sample.payment

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposePlaygroundTheme {
                Column {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "작품 결제하기")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "선물 결제하기")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "기프트카드 결제하기")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "후원 결제하기")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "클래스 결제하기")
                    }
                }
            }
        }
    }
}