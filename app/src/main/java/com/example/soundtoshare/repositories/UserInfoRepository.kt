package com.example.soundtoshare.repositories

import com.example.soundtoshare.external.FirestoreDatabase

class UserInfoRepository(private val fireStoreDatabase: FirestoreDatabase) {

    fun storeCurrentUserInfo(user: User) {
        fireStoreDatabase.updateUserInformation(user)
    }
}
