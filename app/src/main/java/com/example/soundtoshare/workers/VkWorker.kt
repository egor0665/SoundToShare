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

        val audio = VkAPI.fetchVkMusic()
        if (audio != null)
            Log.d("music:", audio.title)
        else
            Log.d("music:", "no info")
        WorkManager.getInstance(applicationContext).cancelAllWorkByTag("VKMusic")
        WorkManager.getInstance(applicationContext)
            .enqueue(
                OneTimeWorkRequest.Builder(VkWorker::class.java)
                    .addTag("VKMusic")
                    .setInitialDelay(10, TimeUnit.SECONDS)
                    .build()
            )
        return Result.failure()
    }
}