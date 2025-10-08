/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.cartool.property

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.AppFunctionSchemaDefinition

const val RESULT_SUCCESS = "success"

@AppFunctionSchemaDefinition(name = "getPropertyList", version = 1, category = "car-property-full")
interface GetPropertyList {
    fun getPropertyList(appFunctionContext: AppFunctionContext): String
}

@AppFunctionSchemaDefinition(name = "getStringProperty", version = 1, category = "car-property-full")
interface GetStringProperty {
    fun getStringProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "setStringProperty", version = 1, category = "car-property-full")
interface SetStringProperty {
    fun setStringProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: String,
    ): String
}

@AppFunctionSchemaDefinition(name = "getBooleanProperty", version = 1, category = "car-property-full")
interface GetBooleanProperty {
    fun getBooleanProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): Boolean
}

@AppFunctionSchemaDefinition(name = "setBooleanProperty", version = 1, category = "car-property-full")
interface SetBooleanProperty {
    fun setBooleanProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: Boolean,
    ): String
}

@AppFunctionSchemaDefinition(name = "getIntProperty", version = 1, category = "car-property-full")
interface GetIntProperty {
    fun getIntProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): Int
}

@AppFunctionSchemaDefinition(name = "setIntProperty", version = 1, category = "car-property-full")
interface SetIntProperty {
    fun setIntProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: Int,
    ): String
}

@AppFunctionSchemaDefinition(name = "getIntArrayProperty", version = 1, category = "car-property-full")
interface GetIntArrayProperty {
    fun getIntArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): IntArray
}

@AppFunctionSchemaDefinition(name = "setIntArrayProperty", version = 1, category = "car-property-full")
interface SetIntArrayProperty {
    fun setIntArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: IntArray,
    ): String
}

@AppFunctionSchemaDefinition(name = "getLongProperty", version = 1, category = "car-property-full")
interface GetLongProperty {
    fun getLongProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): Long
}

@AppFunctionSchemaDefinition(name = "setLongProperty", version = 1, category = "car-property-full")
interface SetLongProperty {
    fun setLongProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: Long,
    ): String
}

@AppFunctionSchemaDefinition(name = "getLongArrayProperty", version = 1, category = "car-property-full")
interface GetLongArrayProperty {
    fun getLongArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): LongArray
}

@AppFunctionSchemaDefinition(name = "setLongArrayProperty", version = 1, category = "car-property-full")
interface SetLongArrayProperty {
    fun setLongArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: LongArray,
    ): String
}

@AppFunctionSchemaDefinition(name = "getFloatProperty", version = 1, category = "car-property-full")
interface GetFloatProperty {
    fun getFloatProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): Float
}

@AppFunctionSchemaDefinition(name = "setFloatProperty", version = 1, category = "car-property-full")
interface SetFloatProperty {
    fun setFloatProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: Float,
    ): String
}

@AppFunctionSchemaDefinition(name = "getFloatArrayProperty", version = 1, category = "car-property-full")
interface GetFloatArrayProperty {
    fun getFloatArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): FloatArray
}

@AppFunctionSchemaDefinition(name = "setFloatArrayProperty", version = 1, category = "car-property-full")
interface SetFloatArrayProperty {
    fun setFloatArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: FloatArray,
    ): String
}
