package com.mayankwadhwa.github_client

import com.mayankwadhwa.github_client.coroutines.ManagedCoroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlin.coroutines.CoroutineContext


@ExperimentalCoroutinesApi
class TestScope(override val coroutineContext: CoroutineContext) : ManagedCoroutineScope {
    val scope = TestCoroutineScope(coroutineContext)
    override fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch {
            block.invoke(this)
        }
    }
}
