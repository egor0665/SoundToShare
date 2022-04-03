package com.example.soundtoshare.workers

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.soundtoshare.apis.VkAPI
import java.util.concurrent.TimeUnit

class VkWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        try {
            val status = VkAPI.fetchVkMusic()
            if (status != null)
                Log.d("music:", status)
        } catch (ex: Exception) {
            return Result.failure()
        }
        WorkManager.getInstance(applicationContext)
            .enqueue( OneTimeWorkRequest.Builder(VkWorker::class.java)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build())
        return Result.success()
    }
}