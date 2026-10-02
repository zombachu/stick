package com.zombachu.stick.integration.fixtures

import java.util.concurrent.Executor

class TestExecutor(private val name: String) : Executor {
    private val tasks: ArrayDeque<Runnable> = ArrayDeque()

    override fun execute(task: Runnable) {
        tasks.addLast(task)
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
