package com.example.myapplication.shared.welcome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.myapplication.shared.welcome.WelcomeComponent.Model
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DefaultWelcomeComponent(
    private val componentContext: ComponentContext,
    private val aStore: Store<AStore.Intent, AStore.State, Nothing>,
    private val bStore: Store<BStore.Intent, BStore.State, Nothing>,
    private val onFinished: () -> Unit,
) : WelcomeComponent, ComponentContext by componentContext {

    override val model: StateFlow<Model> = aStore.stateFlow
        .combine(bStore.stateFlow, ::model)
        .stateIn(
            instanceScope,
            SharingStarted.WhileSubscribed(),
            Model(),
        )

    override fun plusA() {
        aStore.accept(AStore.Intent.PlusA)
    }

    override fun plusB() {
        bStore.accept(BStore.Intent.PlusB)
    }

    override fun onBackClicked() {
        onFinished()
    }
}

private fun model(aStoreState: AStore.State, bStoreState: BStore.State): Model =
    Model(
        a = aStoreState.a,
        b = bStoreState.b,
    )
