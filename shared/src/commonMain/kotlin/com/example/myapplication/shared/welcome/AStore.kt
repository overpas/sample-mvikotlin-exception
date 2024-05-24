package com.example.myapplication.shared.welcome

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

interface AStore {

    sealed interface Intent {

        data object PlusA : Intent
    }

    data class State(
        val a: String = "a",
    )
}

private sealed interface AMessage {

    data object AAdded : AMessage
}

fun StoreFactory.aStore(): Store<AStore.Intent, AStore.State, Nothing> = create(
    name = "AStore",
    initialState = AStore.State(),
    executorFactory = coroutineExecutorFactory {
        onIntent<AStore.Intent.PlusA> {
            dispatch(AMessage.AAdded)
        }
    },
    reducer = { msg ->
        when (msg) {
            is AMessage.AAdded -> copy(a = a + "a")
            else -> this
        }
    },
)
