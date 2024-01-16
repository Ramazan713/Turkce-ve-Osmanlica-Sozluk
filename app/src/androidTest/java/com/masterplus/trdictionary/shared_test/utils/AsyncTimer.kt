package com.masterplus.trdictionary.shared_test.utils

import java.util.Timer
import kotlin.concurrent.schedule

object AsyncTimer {
    var expired = false
    fun start(delay: Long = 1000){
        expired = false
        Timer().schedule(delay) {
            expired = true
        }
    }
}