package com.example.soundtoshare.workers

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.soundtoshare.external.ObservableUserSongInfo
import com.example.soundtoshare.external.VkAPI
import java.util.concurrent.TimeUnit

class VkWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        VkAPI.fetchVkMusic {
            ObservableUserSongInfo.songData.postValue(this)
            if (ObservableUserSongInfo.songData.value != null)
                ObservableUserSongInfo.songData.value?.title?.let { Log.d("musicTitle", it) }
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
        }
        return Result.success()
    }
}
