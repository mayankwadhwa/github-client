package com.mayankwadhwa.github_client.coroutines

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface ManagedCoroutineScope : CoroutineScope {
    abstract fun launch(block: suspend CoroutineScope.() -> Unit) : Job
}

class LifecycleManagedCoroutineScope(val lifecycleCoroutineScope: LifecycleCoroutineScope,
                                     override val coroutineContext: CoroutineContext): ManagedCoroutineScope {
    override fun launch(block: suspend CoroutineScope.() -> Unit): Job = lifecycleCoroutineScope.launchWhenStarted(block)
}

