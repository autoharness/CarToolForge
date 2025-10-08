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
 * Defines the configuration and constraints for a specific vehicle area within a property.
 * @see android.car.hardware.property.AreaIdConfig
 */
@Serializable
data class AreaIdProfile(
    /** @see android.car.hardware.property.AreaIdConfig.getAreaId */
    val areaId: Int,
    /** A description specifying the exact area or areas covered by the `areaId`. */
    val areaIdDescription: String,
    /** @see android.car.hardware.property.AreaIdConfig.getMinValue */
    val minValue: String? = null,
    /** @see android.car.hardware.property.AreaIdConfig.getMaxValue */
    val maxValue: String? = null,
    /** @see android.car.hardware.property.AreaIdConfig.getSupportedEnumValues */
    val supportedEnumValues: List<Long>,
)
