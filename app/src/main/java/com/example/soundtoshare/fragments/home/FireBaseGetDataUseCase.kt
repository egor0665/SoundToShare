package com.example.soundtoshare.fragments.home

import com.example.soundtoshare.external.FireBaseDatabase

class FireBaseGetDataUseCase {
    private val firebaseDatabase = FireBaseDatabase()

    fun getReactions(vkId:String){
        firebaseDatabase.getReactions(vkId){

        }

        firebase
    }

}