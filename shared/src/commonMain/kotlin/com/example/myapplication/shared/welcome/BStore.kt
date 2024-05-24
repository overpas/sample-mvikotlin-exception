package com.example.myapplication.shared.welcome

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

interface BStore {

    sealed interface Intent {

        data object PlusB : Intent
    }

    data class State(
        val b: String = "b",
    )
}

private sealed interface BMessage {

    data object BAdded : BMessage
}

fun StoreFactory.bStore(): Store<BStore.Intent, BStore.State, Nothing> = create(
    name = "BStore",
    initialState = BStore.State(),
    executorFactory = coroutineExecutorFactory {
        onIntent<BStore.Intent.PlusB> {
            dispatch(BMessage.BAdded)
        }
    },
    reducer = { msg ->
        when (msg) {
            is BMessage.BAdded -> copy(b = b + "b")
            else -> this
        }
    },
)
