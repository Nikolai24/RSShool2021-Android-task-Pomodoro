package com.example.rsshool2021_android_task_pomodoro

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.databinding.StopwatchItemBinding

class TimerViewHolder(
    private val binding: StopwatchItemBinding,
    private val listener: TimerListener,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    private var timer: CountDownTimer? = null
    private var current = 0L

    fun bind(stopwatch: Timer) {
        binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()
        binding.customView.setPeriod(stopwatch.start)
        binding.customView.setCurrent(stopwatch.start - stopwatch.currentMs)
        if (stopwatch.isStarted) {
            binding.startPauseButton.text = "STOP"
            startTimer(stopwatch)
        } else {
            stopTimer(stopwatch)
            binding.startPauseButton.text = "START"
        }

        initButtonsListeners(stopwatch)
    }

    private fun initButtonsListeners(stopwatch: Timer) {
        binding.startPauseButton.setOnClickListener {
            if (stopwatch.isStarted) {
                listener.stop(stopwatch.id, stopwatch.currentMs, false)
            } else {
                listener.start(stopwatch.id)
            }
        }

        binding.deleteButton.setOnClickListener {
            binding.timer.background = getDrawable(binding.root.context, R.color.white)
            listener.delete(stopwatch.id) }
    }

    private fun startTimer(stopwatch: Timer) {
        binding.timer.background = getDrawable(binding.root.context, R.color.white)

        timer?.cancel()
        timer = getCountDownTimer(stopwatch)
        timer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(stopwatch: Timer) {
        timer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(stopwatch: Timer): CountDownTimer {
            return object : CountDownTimer(stopwatch.currentMs, UNIT_TEN_MS) {
                override fun onTick(millisUntilFinished: Long) {
                    stopwatch.currentMs = millisUntilFinished
                    binding.customView.setCurrent(stopwatch.start - millisUntilFinished)
                    binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()
                }

                override fun onFinish() {
                    binding.timer.background = getDrawable(binding.root.context, R.color.pomodoro_dark)
                    listener.stop(stopwatch.id, stopwatch.start, true)
                }
            }
        }
    }