package com.yuman.anotherexercise

import android.app.Application
import com.yuman.anotherexercise.data.IVolumeRepository

class VolumeApplication : Application() {

    val volumeRepository: IVolumeRepository
        get() = ServiceLocator.provideVolumeRepository(this)

}