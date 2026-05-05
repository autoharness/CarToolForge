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
 * A data class representing the result of a property get operation.
 */
@Serializable
data class PropertyValueResult<T>(
    val propertyName: String,
    val areaId: Int,
    val value: T,
)

/**
 * A data class representing the result of a property set operation.
 */
@Serializable
data class PropertyActionResult(
    val propertyName: String,
    val areaId: Int,
    val result: String,
)
