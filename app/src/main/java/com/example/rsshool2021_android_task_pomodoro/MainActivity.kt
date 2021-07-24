package com.example.rsshool2021_android_task_pomodoro

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TimerListener, LifecycleObserver {

    private lateinit var binding: ActivityMainBinding

    private val stopwatchAdapter = TimerAdapter(this)
    private val timers = mutableListOf<Timer>()
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }

        binding.addNewStopwatchButton.setOnClickListener {
            var minutes = binding.editTime.text.toString()
            if (timers.size < 12) {
                if (minutes != "" && minutes != "0") {
                    timers.add(
                        Timer(
                            nextId++,
                            minutes.toLong() * 60000,
                            false,
                            false,
                            minutes.toLong() * 60000
                        )
                    )
                    stopwatchAdapter.submitList(timers.toList())
                } else {
                    Toast.makeText(this, "Enter at least 1 minute", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Maximum number of timers created", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun start(id: Int) {
        changeStopwatch(id, null, true, false)
    }

    override fun stop(id: Int, currentMs: Long, isFinished: Boolean) {
        changeStopwatch(id, currentMs, false, true)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        stopwatchAdapter.submitList(timers.toList())
    }

    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean, isFinished: Boolean) {
            timers.replaceAll {
                when {
                    it.id == id -> Timer(it.id, currentMs ?: it.currentMs, isStarted, isFinished, it.start)
                    it.id != id && it.isStarted ->
                        Timer(it.id, currentMs ?: it.currentMs, false, isFinished, it.start)
                    else -> it
                }
            }
        stopwatchAdapter.submitList(timers.toList())
    }

}