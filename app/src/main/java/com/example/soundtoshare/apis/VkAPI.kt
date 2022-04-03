package com.example.soundtoshare.apis

import android.util.Log
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.status.StatusService
import com.vk.sdk.api.status.dto.StatusStatus

object VkAPI {
    var status : String?  = null
    fun fetchVkMusic() : String? {
        VK.execute(StatusService().statusGet(VK.getUserId()), object: VKApiCallback<StatusStatus> {
            override fun success(result: StatusStatus) {
               status = result.text
            }
            override fun fail(error: Exception) {
                Log.e("error", error.toString())
            }
        })
        return status
    }
}