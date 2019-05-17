package ru.observe.twits.servers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CoroutineExecutor {

    private lateinit var jobCoroutine: Job

    fun cancel() {
        if (jobCoroutine.isActive) {
            jobCoroutine.cancel()
        }
    }

    fun <T> run(block: suspend () -> T) {
        jobCoroutine = GlobalScope.launch(Dispatchers.Default) {
            block()
        }
    }
}