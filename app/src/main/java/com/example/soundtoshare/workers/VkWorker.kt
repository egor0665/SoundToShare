package com.example.soundtoshare.workers

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.soundtoshare.fragments.home.HomeViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class VkWorker(context: Context, params: WorkerParameters) : Worker(context, params),
    KoinComponent {
    private val viewModel: HomeViewModel by inject()
    override fun doWork(): Result {
        viewModel.fetchVkMusicViewModel {
            Log.d("VKmusic", this?.title ?: "no songs?")
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag("VKMusic")
            WorkManager.getInstance(applicationContext)
                .enqueue(
                    OneTimeWorkRequest.Builder(VkWorker::class.java)
                        .addTag("VKMusic")
                        .setInitialDelay(20, TimeUnit.SECONDS)
                        .build()
                )
        }
        return Result.success()
    }
}
