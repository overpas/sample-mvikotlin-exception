package com.example.myapplication.shared.welcome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

val ComponentContext.instanceScope: CoroutineScope
    get() =
        instanceKeeper.getOrCreate {
            InstanceScope(SupervisorJob() + Dispatchers.Main)
        }

private class InstanceScope(
    override val coroutineContext: CoroutineContext,
) : CoroutineScope, InstanceKeeper.Instance {

    override fun onDestroy() {
        cancel()
    }
}
