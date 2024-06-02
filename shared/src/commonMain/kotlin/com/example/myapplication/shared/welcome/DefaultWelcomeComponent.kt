package com.example.myapplication.shared.welcome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.myapplication.shared.welcome.WelcomeComponent.Model
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class DefaultWelcomeComponent(
    private val componentContext: ComponentContext,
    private val aStore: Store<AStore.Intent, AStore.State, AStore.Label>,
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

    init {
        aStore.labels
            .onEach(::handleLabel) // the callback is not called
            .launchIn(instanceScope)
//        aStore.labels(observer(onNext = ::handleLabel)) // This works
    }

    override fun plusA() {
        aStore.accept(AStore.Intent.PlusA)
    }

    override fun plusB() {
        bStore.accept(BStore.Intent.PlusB)
    }

    override fun onBackClicked() {
        onFinished()
    }

    private fun handleLabel(label: AStore.Label) {
        when (label) {
            is AStore.Label.LabelPublished -> {
                bStore.accept(BStore.Intent.PlusB)
            }
        }
    }
}

private fun model(aStoreState: AStore.State, bStoreState: BStore.State): Model =
    Model(
        a = aStoreState.a,
        b = bStoreState.b,
    )
