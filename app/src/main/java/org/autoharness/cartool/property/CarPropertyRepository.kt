/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.cartool.property

import android.car.VehicleAreaType
import android.car.hardware.CarPropertyConfig
import android.car.hardware.property.AreaIdConfig
import android.car.hardware.property.CarPropertyManager
import android.util.ArraySet
import android.util.Log
import androidx.appfunctions.AppFunctionInvalidArgumentException
import kotlinx.serialization.json.Json
import org.autoharness.cartool.VehiclePropertyConfig
import org.autoharness.cartool.VehiclePropertyConfig.Property
import org.autoharness.cartool.property.Constants.AREA_DECODER_MAP
import org.autoharness.cartool.property.Constants.AREA_ID_MASKS
import org.autoharness.cartool.property.Constants.AREA_SPECIFIC_DESCRIPTION_TEMPLATE
import org.autoharness.cartool.property.Constants.GLOBAL_AREA_DESCRIPTION
import org.autoharness.cartool.property.Constants.SUPPORTED_ACCESS_VALUES
import org.autoharness.cartool.property.Constants.SUPPORTED_AREA_TYPES
import org.autoharness.cartool.property.Constants.SUPPORTED_CHANGE_MODES
import org.autoharness.cartool.property.Constants.SUPPORTED_DATA_TYPE_MAP
import org.autoharness.cartool.property.data.AreaIdProfile
import org.autoharness.cartool.property.data.CarPropertyProfile

/**
 * A class that acts as a facade for the [CarPropertyManager].
 */
class CarPropertyRepository(
    private val carPropertyManager: CarPropertyManager,
    private val allowedProperties: Map<Int, Property> = VehiclePropertyConfig.allowedProperties,
) {

    companion object {
        private const val TAG = "CarPropertyRepository"
    }

    private val propertyIdByName: Map<String, Int> = allowedProperties.values.associate { it.name to it.id }

    fun getPropertyList(): String {
        val allowedPropertyIds = ArraySet(allowedProperties.keys)
        val configs = carPropertyManager.getPropertyList(allowedPropertyIds)

        val propertyProfiles = configs.mapNotNull { config ->
            if (verifyPropertyConfigCompatibility(config)) {
                toCarPropertyProfile(config)
            } else {
                Log.w(TAG, "Skipping incompatible property: ${allowedProperties[config.propertyId]}")
                null
            }
        }
        return Json.encodeToString(propertyProfiles)
    }

    private inline fun <T> getProperty(
        propertyName: String,
        areaId: Int,
        block: (Int, Int) -> T,
    ): T {
        val propertyId = getPropertyIdIfAvailable(propertyName, areaId)
        return block(propertyId, areaId)
    }

    private inline fun <T> setProperty(
        propertyName: String,
        areaId: Int,
        value: T,
        block: (Int, Int, T) -> Unit,
    ) {
        val propertyId = getPropertyIdIfAvailable(propertyName)
        block(propertyId, areaId, value)
    }

    fun getStringProperty(propertyName: String, areaId: Int): String = getProperty(propertyName, areaId) { pid, aid ->
        carPropertyManager.getProperty(
            java.lang.String::class.java,
            pid,
            aid,
        )?.value?.toString() ?: ""
    }

    fun setStringProperty(propertyName: String, areaId: Int, value: String) = setProperty(propertyName, areaId, value) { pid, aid, v ->
        carPropertyManager.setProperty(String::class.java, pid, aid, v)
    }

    fun getBooleanProperty(propertyName: String, areaId: Int): Boolean = getProperty(propertyName, areaId) { pid, aid ->
        carPropertyManager.getBooleanProperty(pid, aid)
    }

    fun setBooleanProperty(propertyName: String, areaId: Int, value: Boolean) = setProperty(propertyName, areaId, value) { pid, aid, v ->
        carPropertyManager.setBooleanProperty(pid, aid, v)
    }

    fun getIntProperty(propertyName: String, areaId: Int): Int = getProperty(propertyName, areaId) { pid, aid ->
        carPropertyManager.getIntProperty(pid, aid)
    }

    fun setIntProperty(propertyName: String, areaId: Int, value: Int) = setProperty(propertyName, areaId, value) { pid, aid, v ->
        carPropertyManager.setIntProperty(pid, aid, v)
    }

    fun getIntArrayProperty(propertyName: String, areaId: Int): IntArray = getProperty(propertyName, areaId) { pid, aid ->
        carPropertyManager.getIntArrayProperty(pid, aid)
    }

    fun setIntArrayProperty(propertyName: String, areaId: Int, value: IntArray) = setProperty(propertyName, areaId, value) { pid, aid, v ->
        carPropertyManager.setProperty(Array<Int>::class.java, pid, aid, v.toTypedArray())
    }

    fun getLongProperty(propertyName: String, areaId: Int): Long = getProperty(propertyName, areaId) { pid, aid ->
        carPropertyManager.getProperty(java.lang.Long::class.java, pid, aid)?.value?.toLong()
            ?: 0L
    }

    fun setLongProperty(propertyName: String, areaId: Int, value: Long) = setProperty(propertyName, areaId, value) { pid, aid, v ->
        carPropertyManager.setProperty(Long::class.java, pid, aid, v)
    }

    fun getLongArrayProperty(propertyName: String, areaId: Int): LongArray = getProperty(propertyName, areaId) { pid, aid ->
        carPropertyManager.getProperty(Array<Long>::class.java, pid, aid)?.value?.toLongArray()
            ?: LongArray(0)
    }

    fun setLongArrayProperty(propertyName: String, areaId: Int, value: LongArray) = setProperty(propertyName, areaId, value) { pid, aid, v ->
        carPropertyManager.setProperty(Array<Long>::class.java, pid, aid, v.toTypedArray())
    }

    fun getFloatProperty(propertyName: String, areaId: Int): Float = getProperty(propertyName, areaId) { pid, aid ->
        carPropertyManager.getFloatProperty(pid, aid)
    }

    fun setFloatProperty(propertyName: String, areaId: Int, value: Float) = setProperty(propertyName, areaId, value) { pid, aid, v ->
        carPropertyManager.setFloatProperty(pid, aid, v)
    }

    fun getFloatArrayProperty(propertyName: String, areaId: Int): FloatArray = getProperty(propertyName, areaId) { pid, aid ->
        carPropertyManager.getProperty(
            Array<Float>::class.java,
            pid,
            aid,
        )?.value?.toFloatArray()
            ?: FloatArray(0)
    }

    fun setFloatArrayProperty(propertyName: String, areaId: Int, value: FloatArray) = setProperty(propertyName, areaId, value) { pid, aid, v ->
        carPropertyManager.setProperty(Array<Float>::class.java, pid, aid, v.toTypedArray())
    }

    private fun toCarPropertyProfile(carPropertyConfig: CarPropertyConfig<*>): CarPropertyProfile {
        val allowedConfig = allowedProperties[carPropertyConfig.propertyId]
            ?: strongThrow("Property ID '${carPropertyConfig.propertyId}' is not in the allowed properties list")

        return with(carPropertyConfig) {
            CarPropertyProfile(
                propertyName = allowedConfig.name,
                propertyDescription = allowedConfig.description,
                access = access,
                dataType = toDataType(propertyType),
                changeMode = changeMode,
                areaType = areaType,
                areaIdProfiles = constructAreaProfile(this),
            )
        }
    }

    private fun verifyPropertyConfigCompatibility(config: CarPropertyConfig<*>): Boolean {
        // Helper function to encapsulate the check-and-log pattern.
        fun ensure(condition: Boolean, lazyMessage: () -> String): Boolean {
            if (!condition) {
                Log.w(TAG, lazyMessage())
            }
            return condition
        }

        with(config) {
            val property = allowedProperties[propertyId]
            return ensure(verifyAccessValue(access)) { "Property ($property) has incompatible access level: $access" } &&
                ensure(verifyAreaTypeValue(areaType)) { "Property ($property) has incompatible area type: $areaType" } &&
                ensure(verifyChangeMode(changeMode)) { "Property ($property) has incompatible change mode: $changeMode" } &&
                ensure(verifyDataType(propertyType)) { "Property ($property) has incompatible data type: $propertyType" } &&
                areaIds.all { areaId ->
                    ensure(
                        verifyAreaId(
                            areaType,
                            areaId,
                        ),
                    ) { "Property ($property) with area type ($areaType) has incompatible area id: $areaId" }
                }
        }
    }

    private fun verifyAccessValue(access: Int): Boolean = access in SUPPORTED_ACCESS_VALUES

    private fun verifyAreaTypeValue(areaType: Int): Boolean = areaType in SUPPORTED_AREA_TYPES

    private fun verifyChangeMode(changeMode: Int): Boolean = changeMode in SUPPORTED_CHANGE_MODES

    private fun verifyDataType(dataType: Class<*>) = dataType in SUPPORTED_DATA_TYPE_MAP.keys

    private fun verifyAreaId(areaType: Int, areaId: Int): Boolean = when (areaType) {
        VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL -> areaId == 0
        in AREA_ID_MASKS.keys -> {
            val mask = AREA_ID_MASKS.getValue(areaType)
            areaId != 0 && (areaId and mask.inv()) == 0
        }

        else -> strongThrow("Unsupported area type: $areaType")
    }

    private fun toDataType(clazz: Class<*>): Int = SUPPORTED_DATA_TYPE_MAP[clazz]
        ?: strongThrow("Unsupported class type: ${clazz.name}")

    private fun constructAreaProfile(carPropertyConfig: CarPropertyConfig<*>): List<AreaIdProfile> = carPropertyConfig.areaIdConfigs.map { areaIdConfig ->
        toAreaIdProfile(carPropertyConfig.areaType, areaIdConfig)
    }

    @Suppress("DEPRECATION")
    private fun toAreaIdProfile(areaType: Int, areaIdConfig: AreaIdConfig<*>): AreaIdProfile = AreaIdProfile(
        areaId = areaIdConfig.areaId,
        areaIdDescription = buildAreaDescription(areaType, areaIdConfig.areaId),
        minValue = (areaIdConfig.minValue as? Number)?.toString() ?: "",
        maxValue = (areaIdConfig.maxValue as? Number)?.toString() ?: "",
        supportedEnumValues = areaIdConfig.supportedEnumValues.mapNotNull {
            (it as? Number)?.toLong()
        },
    )

    private fun buildAreaDescription(areaType: Int, areaId: Int): String {
        if (areaType == VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL) {
            return GLOBAL_AREA_DESCRIPTION
        }

        val flagMap = AREA_DECODER_MAP[areaType] ?: strongThrow("Unsupported area type: $areaType")

        val decodedDescription = flagMap
            .filter { (flag, _) -> (areaId and flag) != 0 }
            .values
            .joinToString()

        return if (decodedDescription.isNotBlank()) {
            AREA_SPECIFIC_DESCRIPTION_TEMPLATE.format(decodedDescription)
        } else {
            strongThrow("Unhandled area id: $areaId for area type: $areaType")
        }
    }

    private fun getPropertyIdIfAvailable(propertyName: String, areaId: Int? = null): Int {
        val propertyId = propertyIdByName[propertyName]
            ?: throw AppFunctionInvalidArgumentException("Property '$propertyName' does not exist or is not authorized")

        if (areaId != null && !isPropertyAvailable(propertyId, areaId)) {
            throw AppFunctionInvalidArgumentException("Property '$propertyName' is currently not available")
        }
        return propertyId
    }

    private fun isPropertyAvailable(propertyId: Int, areaId: Int): Boolean = carPropertyManager.isPropertyAvailable(propertyId, areaId)

    /**
     * Used for an internal error.
     * Do not propagate the error to the client side.
     */
    private fun strongThrow(msg: String): Nothing = throw Error(msg)
}
