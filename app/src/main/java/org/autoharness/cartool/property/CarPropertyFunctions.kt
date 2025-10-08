/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.cartool.property

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction

class CarPropertyFunctions(private val repository: CarPropertyRepository) :
    GetPropertyList,
    GetStringProperty,
    SetStringProperty,
    GetBooleanProperty,
    SetBooleanProperty,
    GetIntProperty,
    SetIntProperty,
    GetIntArrayProperty,
    SetIntArrayProperty,
    GetLongProperty,
    SetLongProperty,
    GetLongArrayProperty,
    SetLongArrayProperty,
    GetFloatProperty,
    SetFloatProperty,
    GetFloatArrayProperty,
    SetFloatArrayProperty {

    /**
     * A list of supported vehicle properties, formatted as a JSON string.
     *
     * The vehicle functions are described as vehicle properties in JSON format.
     * To understand the data, the following data structure description is provided.
     * Please strictly adhere to this description when interpreting the meaning of JSON fields.
     *
     * | Field Path                             | Type             | Description                                                  |
     * | -------------------------------------- | ---------------- | ------------------------------------------------------------ |
     * | `propertyName`                         | String           | The unique, machine-readable name of the property, often in a standardized format (e.g., uppercase with underscores). |
     * | `propertyDescription`                  | String           | A specific description of what the property represents.      |
     * | `access`                               | Integer (Enum)   | Defines the property's access permissions. The integer value represents: `1` = Read-only, `2` = Write-only, `3` = Read & Write. |
     * | `dataType`                             | Integer (Enum)   | An integer code specifying the property's data type. The mappings are: `1`=STRING, `2`=BOOLEAN, `3`=INT32, `4`=INT32_VEC (Array of 32-bit integers), `5`=LONG, `6`=LONG_VEC (Array of 64-bit integers), `7`=FLOAT, `8`=FLOAT_VEC (Array of floats), `9`=BYTES. |
     * | `changeMode`                           | Integer (Enum)   | An integer enum that specifies how the property's value updates are reported. The possible values are: `0` (Static) - The property value is constant and never changes. `1` (On Change) - The property reports updates only when its value changes. `2` (Continuous) - The property's value changes continuously and is reported at a regular interval. |
     * | `areaType`                             | Integer (Enum)   | An integer enum that defines the specific physical area of the vehicle to which the property applies. The codes map to the following areas: `0` = Global (the entire vehicle), `2` = Window, `3` = Seat, `4` = Door, `5` = Mirror, `6` = Wheel. |
     * | `areaIdProfiles`                       | Array of Objects | A list of profiles defining how this property applies to different areas of the vehicle. |
     * | `areaIdProfiles[].areaId`              | Integer          | An integer bitmask that specifies a distinct physical location within the vehicle, such as a specific seat or window, allowing you to read from or write to a property for just that targeted zone. |
     * | `areaIdProfiles[].areaIdDescription`   | String           | A description specifying the exact area or areas covered by the `areaId`. |
     * | `areaIdProfiles[].minValue`            | Number           | The minimum allowed value for this property in this specific area. Empty value indicates no minimum is enforced. |
     * | `areaIdProfiles[].maxValue`            | Number           | The maximum allowed value for this property in this specific area. Empty value indicates no minimum is enforced. |
     * | `areaIdProfiles[].supportedEnumValues` | Array            | A list of specific enumeration values that are supported for this property in this area. An empty array suggests it's not an enum. |
     *
     * @return A JSON string representing a list of property profiles for supported vehicle properties.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getPropertyList(appFunctionContext: AppFunctionContext): String = repository.getPropertyList()

    /**
     * Gets the current value of a string type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to read.
     * @param areaId The specific area ID of the property to read.
     * @return The current string value, or an empty string if the property value is null.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getStringProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): String = repository.getStringProperty(propertyName, areaId)

    /**
     * Sets the value for a string type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to modify.
     * @param areaId The specific area ID of the property to modify.
     * @param value The new string value to set.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun setStringProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: String,
    ): String {
        repository.setStringProperty(propertyName, areaId, value)
        return RESULT_SUCCESS
    }

    /**
     * Gets the current value of a boolean type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to read.
     * @param areaId The specific area ID of the property to read.
     * @return The current boolean value of the property.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getBooleanProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): Boolean = repository.getBooleanProperty(propertyName, areaId)

    /**
     * Sets the value for a boolean type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to modify.
     * @param areaId The specific area ID of the property to modify.
     * @param value The new boolean value to set.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun setBooleanProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: Boolean,
    ): String {
        repository.setBooleanProperty(propertyName, areaId, value)
        return RESULT_SUCCESS
    }

    /**
     * Gets the current value of an integer type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to read.
     * @param areaId The specific area ID of the property to read.
     * @return The current integer value of the property.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getIntProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): Int = repository.getIntProperty(propertyName, areaId)

    /**
     * Sets the value for an integer type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to modify.
     * @param areaId The specific area ID of the property to modify.
     * @param value The new integer value to set.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun setIntProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: Int,
    ): String {
        repository.setIntProperty(propertyName, areaId, value)
        return RESULT_SUCCESS
    }

    /**
     * Gets the current value of an integer array type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to read.
     * @param areaId The specific area ID of the property to read.
     * @return The current integer array value of the property.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getIntArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): IntArray = repository.getIntArrayProperty(propertyName, areaId)

    /**
     * Sets the value for an integer array type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to modify.
     * @param areaId The specific area ID of the property to modify.
     * @param value The new integer array value to set.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun setIntArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: IntArray,
    ): String {
        repository.setIntArrayProperty(propertyName, areaId, value)
        return RESULT_SUCCESS
    }

    /**
     * Gets the current value of a long type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to read.
     * @param areaId The specific area ID of the property to read.
     * @return The current long value, or 0L if the property value is null.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getLongProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): Long = repository.getLongProperty(propertyName, areaId)

    /**
     * Sets the value for a long type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to modify.
     * @param areaId The specific area ID of the property to modify.
     * @param value The new long value to set.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun setLongProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: Long,
    ): String {
        repository.setLongProperty(propertyName, areaId, value)
        return RESULT_SUCCESS
    }

    /**
     * Gets the current value of a long array type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to read.
     * @param areaId The specific area ID of the property to read.
     * @return The current long array, or an empty array if the property value is null.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getLongArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): LongArray = repository.getLongArrayProperty(propertyName, areaId)

    /**
     * Sets the value for a long array type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to modify.
     * @param areaId The specific area ID of the property to modify.
     * @param value The new long array value to set.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun setLongArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: LongArray,
    ): String {
        repository.setLongArrayProperty(propertyName, areaId, value)
        return RESULT_SUCCESS
    }

    /**
     * Gets the current value of a float type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to read.
     * @param areaId The specific area ID of the property to read.
     * @return The current float value of the property.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getFloatProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): Float = repository.getFloatProperty(propertyName, areaId)

    /**
     * Sets the value for a float type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to modify.
     * @param areaId The specific area ID of the property to modify.
     * @param value The new float value to set.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun setFloatProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: Float,
    ): String {
        repository.setFloatProperty(propertyName, areaId, value)
        return RESULT_SUCCESS
    }

    /**
     * Gets the current value of a float array type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to read.
     * @param areaId The specific area ID of the property to read.
     * @return The current float array, or an empty array if the property value is null.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun getFloatArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
    ): FloatArray = repository.getFloatArrayProperty(propertyName, areaId)

    /**
     * Sets the value for a float array type vehicle property.
     *
     * @param propertyName The unique name of the vehicle property to modify.
     * @param areaId The specific area ID of the property to modify.
     * @param value The new float array value to set.
     */
    @AppFunction(isDescribedByKdoc = true)
    override fun setFloatArrayProperty(
        appFunctionContext: AppFunctionContext,
        propertyName: String,
        areaId: Int,
        value: FloatArray,
    ): String {
        repository.setFloatArrayProperty(propertyName, areaId, value)
        return RESULT_SUCCESS
    }
}
