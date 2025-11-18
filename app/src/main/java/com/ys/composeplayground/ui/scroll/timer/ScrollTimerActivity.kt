package com.ys.composeplayground.ui.scroll.timer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.scroll.timer.adapter.ScrollTimerAdapter
import com.ys.composeplayground.ui.scroll.timer.model.createDummyItems
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlinx.coroutines.launch

/**
 * RecyclerView를 사용한 스크롤 기반 타이머 Activity
 */
class ScrollTimerActivity : AppCompatActivity() {

    private val viewModel: ScrollTimerViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScrollTimerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_timer)

        setupToolbar()
        setupDebugInfo()
        setupRecyclerView()
        observeTimerCompletion()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onActivityPaused()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onActivityResumed()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun setupToolbar() {
        findViewById<ComposeView>(R.id.toolbarView).setContent {
            ComposePlaygroundTheme {
                TopAppBar(
                    title = { Text("스크롤 타이머 (RecyclerView)") },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(Icons.Default.ArrowBack, "뒤로가기")
                        }
                    }
                )
            }
        }
    }

    private fun setupDebugInfo() {
        findViewById<ComposeView>(R.id.debugInfoView).setContent {
            ComposePlaygroundTheme {
                val timerState by viewModel.timerState.collectAsState()
                DebugInfo(timerState, viewModel)
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        val items = createDummyItems()

        adapter = ScrollTimerAdapter(items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 스크롤 리스너 등록
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        // 스크롤 정지
                        viewModel.onScrollStopped()
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING,
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        // 스크롤 시작
                        viewModel.onScrollStarted()
                    }
                }
            }
        })

        // 타이머 상태 관찰 및 adapter 업데이트
        lifecycleScope.launch {
            viewModel.timerState.collect { timerState ->
                adapter.updateTimerState(timerState)
            }
        }
    }

    private fun observeTimerCompletion() {
        lifecycleScope.launch {
            viewModel.completionEvent.collect { event ->
                // 인앱 메시지 표시
                showInAppMessage(event.message, event.imageUrl)

                // 타이머 배너 아이템 제거
                removeTimerBannerItem()
            }
        }
    }

    private fun showInAppMessage(message: String, imageUrl: String) {
        // ComposeView를 root view에 overlay로 추가
        val rootView = findViewById<android.view.ViewGroup>(android.R.id.content)

        val composeView = ComposeView(this).apply {
            setContent {
                ComposePlaygroundTheme {
                    ToastStyleInAppMessage(
                        message = message,
                        imageUrl = imageUrl,
                        onDismiss = {
                            rootView.removeView(this@apply)
                        }
                    )
                }
            }
        }

        rootView.addView(composeView)
    }

    private fun removeTimerBannerItem() {
        // Adapter에 타이머 완료 알림 (아이템 제거 처리)
        adapter.notifyTimerCompleted()
    }
}
