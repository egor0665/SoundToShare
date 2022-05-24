package com.example.soundtoshare.workers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.soundtoshare.fragments.home.HomeViewModel
import com.example.soundtoshare.fragments.map.MapViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import java.util.concurrent.TimeUnit

class UserDataWorker(val context: Context, params: WorkerParameters) : Worker(context, params),
    KoinComponent {
    private val homeViewModel: HomeViewModel by inject()
    override fun doWork(): Result {
        homeViewModel.fetchVkMusic {
            Log.d("Worker", "Worker stop at:" + Calendar.getInstance().time.toString())
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag("VKMusic")
            WorkManager.getInstance(applicationContext)
                .enqueue(
                    OneTimeWorkRequest.Builder(UserDataWorker::class.java)
                        .addTag("VKMusic")
                        .setInitialDelay(20, TimeUnit.SECONDS)
                        .build()
                )
            homeViewModel.uploadUserData()
        }
        return Result.success()
    }
}
