package com.example.rsshool2021_android_task_pomodoro

interface TimerListener {

    fun start(id: Int)

    fun stop(id: Int, currentMs:Long, isFinished: Boolean)

    fun delete(id: Int)
}