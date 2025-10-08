/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.cartool.property.data

import kotlinx.serialization.Serializable

/**
 * Represents a detailed profile of a vehicle property.
 * @see android.car.hardware.CarPropertyConfig
 */
@Serializable
data class CarPropertyProfile(
    /** @see android.car.VehiclePropertyIds */
    val propertyName: String,
    /** A specific description of what the property represents. */
    val propertyDescription: String,
    /** @see android.car.hardware.CarPropertyConfig.getAccess */
    val access: Int,
    /** @see android.car.hardware.CarPropertyConfig.getPropertyType */
    val dataType: Int,
    /** @see android.car.hardware.CarPropertyConfig.getChangeMode */
    val changeMode: Int,
    /** @see android.car.hardware.CarPropertyConfig.getAreaType */
    val areaType: Int,
    /** @see android.car.hardware.CarPropertyConfig.getAreaIdConfigs */
    val areaIdProfiles: List<AreaIdProfile>,
)
