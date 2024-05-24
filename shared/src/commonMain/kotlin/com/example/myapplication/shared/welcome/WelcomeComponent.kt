package com.example.myapplication.shared.welcome

import kotlinx.coroutines.flow.StateFlow

interface WelcomeComponent {

    val model: StateFlow<Model>

    fun plusA()

    fun plusB()

    fun onBackClicked()

    data class Model(
        val a: String = "a",
        val b: String = "b",
    )
}
