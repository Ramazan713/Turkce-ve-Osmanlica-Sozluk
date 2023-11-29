package com.masterplus.trdictionary.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule constructor(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
): BeforeEachCallback,AfterEachCallback {

    override fun beforeEach(p0: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(p0: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}