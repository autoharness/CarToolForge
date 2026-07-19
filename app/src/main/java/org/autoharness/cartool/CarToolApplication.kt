/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.cartool

import android.app.Application
import android.car.Car
import android.car.hardware.property.CarPropertyManager
import android.util.Log
import org.autoharness.cartool.property.CarPropertyRepository

class CarToolApplication : Application() {
    companion object {
        private const val TAG = "CarToolApplication"
    }

    private lateinit var car: Car
    private lateinit var carPropertyManager: CarPropertyManager

    @Volatile
    var carPropertyRepository: CarPropertyRepository? = null
        private set

    private val carServiceLifecycleListener: Car.CarServiceLifecycleListener =
        Car.CarServiceLifecycleListener { car, ready ->
            if (ready) {
                carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
                carPropertyRepository = CarPropertyRepository(carPropertyManager)
            } else {
                Log.e(TAG, "Car service is killed")
                carPropertyRepository = null
            }
        }

    override fun onCreate() {
        super.onCreate()
        prepareCar()
    }

    override fun onTerminate() {
        super.onTerminate()
        car.disconnect()
    }

    private fun prepareCar() {
        car = Car.createCar(
            this,
            null,
            Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER,
            carServiceLifecycleListener,
        )
    }
}
