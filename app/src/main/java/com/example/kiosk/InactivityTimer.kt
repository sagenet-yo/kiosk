package com.example.kiosk
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

class InactivityTimer(
    private val timeoutMillis: Long,
    private val onTimeout: () -> Unit,
    lifecycle: Lifecycle
) : LifecycleObserver {
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var isStarted = false

    init {
        lifecycle.addObserver(this)
    }

    fun start() {
        if (isStarted) {
            reset()
        } else {
            isStarted = true
            job?.cancel()
            job = scope.launch {
                delay(timeoutMillis)
                onTimeout()
            }
        }
    }

    fun reset() {
        if (isStarted) {
            job?.cancel()
            job = scope.launch {
                delay(timeoutMillis)
                onTimeout()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancel() {
        job?.cancel()
        isStarted = false
    }
}