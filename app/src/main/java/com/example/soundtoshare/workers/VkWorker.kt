package com.example.soundtoshare.workers

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.soundtoshare.apis.VkAPI
import com.vk.sdk.api.audio.dto.AudioAudio
import java.util.concurrent.TimeUnit

class VkWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private var audio: AudioAudio? = null

    override fun doWork(): Result {
        VkAPI.fetchVkMusic {
            audio = this
            if (audio != null) {
                Log.d("musicTitle", audio!!.title)
                Log.d("musicURI", "https://vk.com/audio" + audio!!.ownerId + "_" + audio!!.id)
            } else
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
