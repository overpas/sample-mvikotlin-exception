package com.example.myapplication.shared.welcome

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface AStore {

    sealed interface Intent {

        data object PlusA : Intent
    }

    data class State(
        val a: String = "a",
    )

    sealed interface Label {

        data object LabelPublished : Label
    }
}

private sealed interface AMessage {

    data object AAdded : AMessage
}

private sealed interface Action {

    data object PublishLabel : Action
}

fun StoreFactory.aStore(): Store<AStore.Intent, AStore.State, AStore.Label> = create(
    name = "AStore",
    initialState = AStore.State(),
    bootstrapper = coroutineBootstrapper {
        dispatch(Action.PublishLabel)
    },
    executorFactory = coroutineExecutorFactory {
        onIntent<AStore.Intent.PlusA> {
            dispatch(AMessage.AAdded)
        }
        onAction<Action.PublishLabel> {
            launch {
//                delay(100) // With delay it works
                publish(AStore.Label.LabelPublished)
            }
        }
    },
    reducer = { msg ->
        when (msg) {
            is AMessage.AAdded -> copy(a = a + "a")
            else -> this
        }
    },
)
