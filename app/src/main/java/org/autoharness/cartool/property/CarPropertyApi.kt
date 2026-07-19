/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.cartool.property

import androidx.appfunctions.AppFunctionSchemaDefinition

const val RESULT_SUCCESS = "success"

@AppFunctionSchemaDefinition(name = "getPropertyList", version = 1, category = "car-property-full")
interface GetPropertyList {
    fun getPropertyList(
        category: String? = "ALL_CATEGORIES",
    ): String
}

@AppFunctionSchemaDefinition(name = "getStringProperty", version = 1, category = "car-property-full")
interface GetStringProperty {
    fun getStringProperty(
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setStringProperty", version = 1, category = "car-property-full")
interface SetStringProperty {
    fun setStringProperty(
        propertyName: String,
        areaId: Int,
        value: String,
    ): String
}

@AppFunctionSchemaDefinition(name = "getBooleanProperty", version = 1, category = "car-property-full")
interface GetBooleanProperty {
    fun getBooleanProperty(
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setBooleanProperty", version = 1, category = "car-property-full")
interface SetBooleanProperty {
    fun setBooleanProperty(
        propertyName: String,
        areaId: Int,
        value: Boolean,
    ): String
}

@AppFunctionSchemaDefinition(name = "getIntProperty", version = 1, category = "car-property-full")
interface GetIntProperty {
    fun getIntProperty(
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setIntProperty", version = 1, category = "car-property-full")
interface SetIntProperty {
    fun setIntProperty(
        propertyName: String,
        areaId: Int,
        value: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "getIntArrayProperty", version = 1, category = "car-property-full")
interface GetIntArrayProperty {
    fun getIntArrayProperty(
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setIntArrayProperty", version = 1, category = "car-property-full")
interface SetIntArrayProperty {
    fun setIntArrayProperty(
        propertyName: String,
        areaId: Int,
        value: IntArray,
    ): String
}

@AppFunctionSchemaDefinition(name = "getLongProperty", version = 1, category = "car-property-full")
interface GetLongProperty {
    fun getLongProperty(
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setLongProperty", version = 1, category = "car-property-full")
interface SetLongProperty {
    fun setLongProperty(
        propertyName: String,
        areaId: Int,
        value: Long,
    ): String
}

@AppFunctionSchemaDefinition(name = "getLongArrayProperty", version = 1, category = "car-property-full")
interface GetLongArrayProperty {
    fun getLongArrayProperty(
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setLongArrayProperty", version = 1, category = "car-property-full")
interface SetLongArrayProperty {
    fun setLongArrayProperty(
        propertyName: String,
        areaId: Int,
        value: LongArray,
    ): String
}

@AppFunctionSchemaDefinition(name = "getFloatProperty", version = 1, category = "car-property-full")
interface GetFloatProperty {
    fun getFloatProperty(
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setFloatProperty", version = 1, category = "car-property-full")
interface SetFloatProperty {
    fun setFloatProperty(
        propertyName: String,
        areaId: Int,
        value: Float,
    ): String
}

@AppFunctionSchemaDefinition(name = "getFloatArrayProperty", version = 1, category = "car-property-full")
interface GetFloatArrayProperty {
    fun getFloatArrayProperty(
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setFloatArrayProperty", version = 1, category = "car-property-full")
interface SetFloatArrayProperty {
    fun setFloatArrayProperty(
        propertyName: String,
        areaId: Int,
        value: FloatArray,
    ): String
}
