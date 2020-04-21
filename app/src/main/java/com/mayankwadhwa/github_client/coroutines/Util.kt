package com.mayankwadhwa.github_client.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal fun executeBackground(scope: CoroutineScope, action: suspend () -> Unit) {
    scope.launch(context = Dispatchers.IO) { action() }
}

internal fun executeMain(scope: CoroutineScope, action: suspend () -> Unit) {
    scope.launch(context = Dispatchers.Main) { action() }
}
