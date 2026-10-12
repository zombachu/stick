package com.zombachu.stick.integration.fixtures

import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException

class TestExecutor(private val name: String) : Executor {
    private val tasks: ArrayDeque<Runnable> = ArrayDeque()
    private var isShutdown = false

    override fun execute(task: Runnable) {
        if (isShutdown) throw RejectedExecutionException()
        tasks.addLast(task)
    }

    fun shutdown() {
        isShutdown = true
    }

    fun drain() {
        while (tasks.isNotEmpty()) {
            running = name
            tasks.removeFirst().run()
            running = null
        }
    }

    companion object {
        var running: String? = null
            private set
    }
}
