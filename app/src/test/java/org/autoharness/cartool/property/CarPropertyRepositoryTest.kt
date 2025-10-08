/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.cartool.property

import android.car.VehicleAreaDoor
import android.car.VehicleAreaMirror
import android.car.VehicleAreaSeat
import android.car.VehicleAreaType
import android.car.VehicleAreaWheel
import android.car.VehicleAreaWindow
import android.car.hardware.CarPropertyConfig
import android.car.hardware.property.AreaIdConfig
import android.car.hardware.property.CarPropertyManager
import android.os.Build
import androidx.appfunctions.AppFunctionInvalidArgumentException
import kotlinx.serialization.json.Json
import org.autoharness.cartool.VehiclePropertyConfig
import org.autoharness.cartool.property.Constants.AREA_ID_MASKS
import org.autoharness.cartool.property.data.CarPropertyProfile
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class CarPropertyRepositoryTest {

    private lateinit var carPropertyRepository: CarPropertyRepository

    @Test
    fun `getPropertyList returns serialized compatible properties`() {
        // Prepare data.
        val configAccessRead = mockConfig(
            propertyId = 1,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configAccessWrite = mockConfig(
            propertyId = 2,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_WRITE,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configAccessReadWrite = mockConfig(
            propertyId = 3,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configDataTypeBoolean = mockConfig(
            propertyId = 4,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configDataTypeFloat = mockConfig(
            propertyId = 5,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Float::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configDataTypeInteger = mockConfig(
            propertyId = 6,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Integer::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configDataTypeLong = mockConfig(
            propertyId = 7,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Long::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configDataTypeFloatArray = mockConfig(
            propertyId = 8,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = Array<Float>::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configDataTypeIntArray = mockConfig(
            propertyId = 9,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = Array<Int>::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configDataTypeLongArray = mockConfig(
            propertyId = 10,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = Array<Long>::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configDataTypeString = mockConfig(
            propertyId = 11,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configChangeModeStatic = mockConfig(
            propertyId = 12,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configChangeModeOnChange = mockConfig(
            propertyId = 13,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_ONCHANGE,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configChangeModeContinuous = mockConfig(
            propertyId = 14,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configAreaTypeGlobal = mockConfig(
            propertyId = 15,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = 0),
            ),
        )
        val configAreaTypeWindow = mockConfig(
            propertyId = 16,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_WINDOW,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_FRONT_WINDSHIELD),
            ),
        )
        val configAreaTypeSeat = mockConfig(
            propertyId = 17,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_SEAT,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = VehicleAreaSeat.SEAT_ROW_1_LEFT),
            ),
        )
        val configAreaTypeDoor = mockConfig(
            propertyId = 18,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_DOOR,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = VehicleAreaDoor.DOOR_ROW_1_LEFT),
            ),
        )
        val configAreaTypeMirror = mockConfig(
            propertyId = 19,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_MIRROR,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = VehicleAreaMirror.MIRROR_DRIVER_LEFT),
            ),
        )
        val configAreaTypeWheel = mockConfig(
            propertyId = 20,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_WHEEL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = VehicleAreaWheel.WHEEL_RIGHT_REAR),
            ),
        )
        val configAreaIdWindows = mockConfig(
            propertyId = 21,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_WINDOW,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_FRONT_WINDSHIELD),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_REAR_WINDSHIELD),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_ROW_1_LEFT),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_ROW_1_RIGHT),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_ROW_2_LEFT),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_ROW_2_RIGHT),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_ROW_3_LEFT),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_ROW_3_RIGHT),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_ROOF_TOP_1),
                mockAreaIdConfig(areaId = VehicleAreaWindow.WINDOW_ROOF_TOP_2),
            ),
        )
        val configAreaIdAllWindow = mockConfig(
            propertyId = 22,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_WINDOW,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = AREA_ID_MASKS[VehicleAreaType.VEHICLE_AREA_TYPE_WINDOW]!!),
            ),
        )
        val configAreaIdAllSeat = mockConfig(
            propertyId = 23,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_SEAT,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = AREA_ID_MASKS[VehicleAreaType.VEHICLE_AREA_TYPE_SEAT]!!),
            ),
        )
        val configAreaIdAllDoor = mockConfig(
            propertyId = 24,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_DOOR,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = AREA_ID_MASKS[VehicleAreaType.VEHICLE_AREA_TYPE_DOOR]!!),
            ),
        )
        val configAreaIdAllMirror = mockConfig(
            propertyId = 25,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_MIRROR,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = AREA_ID_MASKS[VehicleAreaType.VEHICLE_AREA_TYPE_MIRROR]!!),
            ),
        )
        val configAreaIdAllWheel = mockConfig(
            propertyId = 26,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.String::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_WHEEL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(areaId = AREA_ID_MASKS[VehicleAreaType.VEHICLE_AREA_TYPE_WHEEL]!!),
            ),
        )
        val configMinMaxValueFloat = mockConfig(
            propertyId = 27,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Float::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(
                    areaId = 0,
                    minValue = java.lang.Float.valueOf(1.0f) as java.lang.Float,
                    maxValue = java.lang.Float.valueOf(5.0f) as java.lang.Float,
                    supportedEnumValues = emptyList(),
                ),
            ),
        )
        val configMinMaxValueInteger = mockConfig(
            propertyId = 28,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Integer::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(
                    areaId = 0,
                    minValue = java.lang.Integer.valueOf(1) as java.lang.Integer,
                    maxValue = java.lang.Integer.valueOf(20) as java.lang.Integer,
                    supportedEnumValues = emptyList(),
                ),
            ),
        )
        val configMinMaxValueLong = mockConfig(
            propertyId = 29,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Long::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(
                    areaId = 0,
                    minValue = java.lang.Long.valueOf(500) as java.lang.Long,
                    maxValue = java.lang.Long.valueOf(2000) as java.lang.Long,
                    supportedEnumValues = emptyList(),
                ),
            ),
        )
        val configSupportedEnumValues = mockConfig(
            propertyId = 30,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
            propertyType = java.lang.Long::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(
                mockAreaIdConfig(
                    areaId = 0,
                    minValue = null,
                    maxValue = null,
                    supportedEnumValues = listOf(
                        java.lang.Long.valueOf(1) as java.lang.Long,
                        java.lang.Long.valueOf(2) as java.lang.Long,
                    ),
                ),
            ),
        )
        val testConfigs = listOf(
            configAccessRead,
            configAccessWrite,
            configAccessReadWrite,
            configDataTypeBoolean,
            configDataTypeFloat,
            configDataTypeInteger,
            configDataTypeLong,
            configDataTypeFloatArray,
            configDataTypeIntArray,
            configDataTypeLongArray,
            configDataTypeString,
            configChangeModeStatic,
            configChangeModeOnChange,
            configChangeModeContinuous,
            configAreaTypeGlobal,
            configAreaTypeWindow,
            configAreaTypeSeat,
            configAreaTypeDoor,
            configAreaTypeMirror,
            configAreaTypeWheel,
            configAreaIdWindows,
            configAreaIdAllWindow,
            configAreaIdAllSeat,
            configAreaIdAllDoor,
            configAreaIdAllMirror,
            configAreaIdAllWheel,
            configMinMaxValueFloat,
            configMinMaxValueInteger,
            configMinMaxValueLong,
            configSupportedEnumValues,
        )
        val allowedProperties = testConfigs.associate {
            it.propertyId to VehiclePropertyConfig.Property(
                name = "PROPERTY_NAME_${it.propertyId}",
                description = "A description for property ${it.propertyId}",
                id = it.propertyId,
            )
        }
        val mockCarPropertyManager: CarPropertyManager = mock()
        whenever(mockCarPropertyManager.getPropertyList(any())).thenReturn(testConfigs)
        carPropertyRepository = CarPropertyRepository(mockCarPropertyManager, allowedProperties)
        // Run test.
        val resultJson = carPropertyRepository.getPropertyList()
        val profiles = Json.decodeFromString<List<CarPropertyProfile>>(resultJson)
        assertEquals(testConfigs.size, profiles.size)
        profiles.forEachIndexed { index, profile ->
            assertEquals("PROPERTY_NAME_${testConfigs[index].propertyId}", profile.propertyName)
        }
        profiles.forEachIndexed { index, profile ->
            assertEquals(
                "A description for property ${testConfigs[index].propertyId}",
                profile.propertyDescription,
            )
        }
        // Validate all fields using hardcoded values for consistency with the API description.
        // Access.
        assertEquals(1, profiles[0].access)
        assertEquals(2, profiles[1].access)
        assertEquals(3, profiles[2].access)
        // Data type.
        assertEquals(2, profiles[3].dataType)
        assertEquals(7, profiles[4].dataType)
        assertEquals(3, profiles[5].dataType)
        assertEquals(5, profiles[6].dataType)
        assertEquals(8, profiles[7].dataType)
        assertEquals(4, profiles[8].dataType)
        assertEquals(6, profiles[9].dataType)
        assertEquals(1, profiles[10].dataType)
        // Change mode.
        assertEquals(0, profiles[11].changeMode)
        assertEquals(1, profiles[12].changeMode)
        assertEquals(2, profiles[13].changeMode)
        // Area type.
        assertEquals(0, profiles[14].areaType)
        assertEquals(2, profiles[15].areaType)
        assertEquals(3, profiles[16].areaType)
        assertEquals(4, profiles[17].areaType)
        assertEquals(5, profiles[18].areaType)
        assertEquals(6, profiles[19].areaType)
        // Area id.
        assertEquals(1, profiles[20].areaIdProfiles[0].areaId)
        assertTrue(profiles[20].areaIdProfiles[0].areaIdDescription.endsWith("front windshield."))
        assertEquals(2, profiles[20].areaIdProfiles[1].areaId)
        assertTrue(profiles[20].areaIdProfiles[1].areaIdDescription.endsWith("rear windshield."))
        assertEquals(16, profiles[20].areaIdProfiles[2].areaId)
        assertTrue(profiles[20].areaIdProfiles[2].areaIdDescription.endsWith("first row left window."))
        assertEquals(64, profiles[20].areaIdProfiles[3].areaId)
        assertTrue(profiles[20].areaIdProfiles[3].areaIdDescription.endsWith("first row right window."))
        assertEquals(256, profiles[20].areaIdProfiles[4].areaId)
        assertTrue(profiles[20].areaIdProfiles[4].areaIdDescription.endsWith("second row left window."))
        assertEquals(1024, profiles[20].areaIdProfiles[5].areaId)
        assertTrue(profiles[20].areaIdProfiles[5].areaIdDescription.endsWith("second row right window."))
        assertEquals(4096, profiles[20].areaIdProfiles[6].areaId)
        assertTrue(profiles[20].areaIdProfiles[6].areaIdDescription.endsWith("third row left window."))
        assertEquals(16384, profiles[20].areaIdProfiles[7].areaId)
        assertTrue(profiles[20].areaIdProfiles[7].areaIdDescription.endsWith("third row right window."))
        assertEquals(65536, profiles[20].areaIdProfiles[8].areaId)
        assertTrue(profiles[20].areaIdProfiles[8].areaIdDescription.endsWith("first top roof window."))
        assertEquals(131072, profiles[20].areaIdProfiles[9].areaId)
        assertTrue(profiles[20].areaIdProfiles[9].areaIdDescription.endsWith("second top roof window."))
        assertEquals(configAreaIdAllWindow.areaIds[0], profiles[21].areaIdProfiles[0].areaId)
        assertTrue(profiles[21].areaIdProfiles[0].areaIdDescription.endsWith("front windshield, rear windshield, first row left window, first row right window, second row left window, second row right window, third row left window, third row right window, first top roof window, second top roof window."))
        assertEquals(configAreaIdAllSeat.areaIds[0], profiles[22].areaIdProfiles[0].areaId)
        assertTrue(profiles[22].areaIdProfiles[0].areaIdDescription.endsWith("first row left seat, first row center seat, first row right seat, second row left seat, second row center seat, second row right seat, third row left seat, third row center seat, third row right seat."))
        assertEquals(configAreaIdAllDoor.areaIds[0], profiles[23].areaIdProfiles[0].areaId)
        assertTrue(profiles[23].areaIdProfiles[0].areaIdDescription.endsWith("first row left door, first row right door, second row left door, second row right door, third row left door, third row right door, hood, trunk lid."))
        assertEquals(configAreaIdAllMirror.areaIds[0], profiles[24].areaIdProfiles[0].areaId)
        assertTrue(profiles[24].areaIdProfiles[0].areaIdDescription.endsWith("left side mirror, right side mirror, rearview mirror."))
        assertEquals(configAreaIdAllWheel.areaIds[0], profiles[25].areaIdProfiles[0].areaId)
        assertTrue(profiles[25].areaIdProfiles[0].areaIdDescription.endsWith("left front wheel, right front wheel, left rear wheel, right rear wheel."))
        // Min and max values.
        assertEquals(
            configMinMaxValueFloat.areaIdConfigs[0].minValue.toString(),
            profiles[26].areaIdProfiles[0].minValue,
        )
        assertEquals(
            configMinMaxValueFloat.areaIdConfigs[0].maxValue.toString(),
            profiles[26].areaIdProfiles[0].maxValue,
        )
        assertEquals(
            configMinMaxValueInteger.areaIdConfigs[0].minValue.toString(),
            profiles[27].areaIdProfiles[0].minValue,
        )
        assertEquals(
            configMinMaxValueInteger.areaIdConfigs[0].maxValue.toString(),
            profiles[27].areaIdProfiles[0].maxValue,
        )
        assertEquals(
            configMinMaxValueLong.areaIdConfigs[0].minValue.toString(),
            profiles[28].areaIdProfiles[0].minValue,
        )
        assertEquals(
            configMinMaxValueLong.areaIdConfigs[0].maxValue.toString(),
            profiles[28].areaIdProfiles[0].maxValue,
        )
        // Supported enums.
        assertEquals(
            configSupportedEnumValues.areaIdConfigs[0].supportedEnumValues,
            profiles[29].areaIdProfiles[0].supportedEnumValues,
        )
    }

    @Test
    fun `getPropertyList with incompatible properties returns empty list`() {
        // Prepare data with various incompatibilities.
        val configIncompatibleAccess = mockConfig(
            propertyId = 101,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_NONE,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(mockAreaIdConfig(0)),
        )

        val configIncompatibleDataType = mockConfig(
            propertyId = 102,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
            propertyType = java.lang.Double::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(mockAreaIdConfig(0)),
        )

        val configIncompatibleChangeMode = mockConfig(
            propertyId = 103,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
            propertyType = java.lang.Boolean::class.java,
            changeMode = -1,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(mockAreaIdConfig(0)),
        )

        val configIncompatibleAreaType = mockConfig(
            propertyId = 104,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = 1,
            areaIdConfigs = listOf(mockAreaIdConfig(0)),
        )

        val configIncompatibleGlobalAreaId = mockConfig(
            propertyId = 105,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
            areaIdConfigs = listOf(mockAreaIdConfig(1)), // must be 0 for GLOBAL.
        )

        val configIncompatibleZonedAreaIdZero = mockConfig(
            propertyId = 106,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_WINDOW,
            areaIdConfigs = listOf(mockAreaIdConfig(0)), // must not be 0 for zoned.
        )

        val configIncompatibleZonedAreaIdMask = mockConfig(
            propertyId = 107,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
            propertyType = java.lang.Boolean::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_DOOR,
            areaIdConfigs = listOf(mockAreaIdConfig(0x40000000)),
        )

        val configIncompatibleMixedAreaIds = mockConfig(
            propertyId = 108,
            access = CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
            propertyType = java.lang.Integer::class.java,
            changeMode = CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
            areaType = VehicleAreaType.VEHICLE_AREA_TYPE_SEAT,
            areaIdConfigs = listOf(
                mockAreaIdConfig(VehicleAreaSeat.SEAT_ROW_1_LEFT),
                mockAreaIdConfig(0), // Invalid, should cause the whole config to be rejected.
            ),
        )

        val incompatibleConfigs = listOf(
            configIncompatibleAccess,
            configIncompatibleDataType,
            configIncompatibleChangeMode,
            configIncompatibleAreaType,
            configIncompatibleGlobalAreaId,
            configIncompatibleZonedAreaIdZero,
            configIncompatibleZonedAreaIdMask,
            configIncompatibleMixedAreaIds,
        )

        val allowedProperties = incompatibleConfigs.associate {
            it.propertyId to VehiclePropertyConfig.Property(
                name = "INCOMPATIBLE_PROP_${it.propertyId}",
                description = "Description for incompatible property ${it.propertyId}",
                id = it.propertyId,
            )
        }

        val mockCarPropertyManager: CarPropertyManager = mock()
        whenever(mockCarPropertyManager.getPropertyList(any())).thenReturn(incompatibleConfigs)
        carPropertyRepository = CarPropertyRepository(mockCarPropertyManager, allowedProperties)

        // Run test.
        val resultJson = carPropertyRepository.getPropertyList()
        // Assert that all incompatible properties were filtered out.
        assertEquals("[]", resultJson)
    }

    @Test
    fun `getters when property is available return correct value`() {
        val testAreaId = 1
        val testPropId = 201
        val testPropName = "TEST_PROPERTY"
        val testProperty = VehiclePropertyConfig.Property(id = testPropId, name = testPropName, description = "")
        val allowedProperties = mapOf(testPropId to testProperty)
        val mockCarPropertyManager: CarPropertyManager = mock()
        carPropertyRepository = CarPropertyRepository(mockCarPropertyManager, allowedProperties)

        whenever(mockCarPropertyManager.isPropertyAvailable(testPropId, testAreaId))
            .thenReturn(true)
        whenever(mockCarPropertyManager.getBooleanProperty(testPropId, testAreaId))
            .thenReturn(true)
        whenever(mockCarPropertyManager.getIntProperty(testPropId, testAreaId))
            .thenReturn(123)
        whenever(mockCarPropertyManager.getFloatProperty(testPropId, testAreaId))
            .thenReturn(123.45f)

        assertEquals(true, carPropertyRepository.getBooleanProperty(testPropName, testAreaId))
        assertEquals(123, carPropertyRepository.getIntProperty(testPropName, testAreaId))
        assertEquals(123.45f, carPropertyRepository.getFloatProperty(testPropName, testAreaId))
    }

    @Test
    fun `getters for nullable types return default value when manager returns null`() {
        val testAreaId = 1
        val testPropId = 201
        val testPropName = "TEST_PROPERTY"
        val testProperty = VehiclePropertyConfig.Property(id = testPropId, name = testPropName, description = "")
        val allowedProperties = mapOf(testPropId to testProperty)
        val mockCarPropertyManager: CarPropertyManager = mock()
        carPropertyRepository = CarPropertyRepository(mockCarPropertyManager, allowedProperties)

        whenever(mockCarPropertyManager.isPropertyAvailable(testPropId, testAreaId)).thenReturn(true)
        whenever(mockCarPropertyManager.getProperty(any<Class<*>>(), eq(testPropId), eq(testAreaId))).thenReturn(null)

        assertEquals("", carPropertyRepository.getStringProperty(testPropName, testAreaId))
        assertEquals(0L, carPropertyRepository.getLongProperty(testPropName, testAreaId))
        assertArrayEquals(longArrayOf(), carPropertyRepository.getLongArrayProperty(testPropName, testAreaId))
        assertArrayEquals(floatArrayOf(), carPropertyRepository.getFloatArrayProperty(testPropName, testAreaId), 0.0f)
    }

    @Test
    fun `getters throw exception when property is not authorized`() {
        val testAreaId = 1
        val unauthorizedPropName = "UNAUTHORIZED_PROPERTY"
        val allowedProperties = emptyMap<Int, VehiclePropertyConfig.Property>() // No properties are allowed.
        val mockCarPropertyManager: CarPropertyManager = mock()
        carPropertyRepository = CarPropertyRepository(mockCarPropertyManager, allowedProperties)

        val testActions = listOf<() -> Unit>(
            { carPropertyRepository.getBooleanProperty(unauthorizedPropName, testAreaId) },
            { carPropertyRepository.getStringProperty(unauthorizedPropName, testAreaId) },
            { carPropertyRepository.getIntProperty(unauthorizedPropName, testAreaId) },
            { carPropertyRepository.getIntArrayProperty(unauthorizedPropName, testAreaId) },
            { carPropertyRepository.getLongProperty(unauthorizedPropName, testAreaId) },
            { carPropertyRepository.getLongArrayProperty(unauthorizedPropName, testAreaId) },
            { carPropertyRepository.getFloatProperty(unauthorizedPropName, testAreaId) },
            { carPropertyRepository.getFloatArrayProperty(unauthorizedPropName, testAreaId) },
        )

        testActions.forEach { action ->
            try {
                action()
                fail("Expected AppFunctionInvalidArgumentException was not thrown.")
            } catch (e: AppFunctionInvalidArgumentException) {
                assertTrue(e.message!!.contains("does not exist or is not authorized"))
            }
        }
    }

    @Test
    fun `getters throw exception when property is not available`() {
        val testAreaId = 1
        val testPropId = 201
        val testPropName = "TEST_PROPERTY"
        val testProperty = VehiclePropertyConfig.Property(id = testPropId, name = testPropName, description = "")
        val allowedProperties = mapOf(testPropId to testProperty)
        val mockCarPropertyManager: CarPropertyManager = mock()
        carPropertyRepository = CarPropertyRepository(mockCarPropertyManager, allowedProperties)

        whenever(mockCarPropertyManager.isPropertyAvailable(testPropId, testAreaId)).thenReturn(false)

        val testActions = listOf<() -> Unit>(
            { carPropertyRepository.getBooleanProperty(testPropName, testAreaId) },
            { carPropertyRepository.getStringProperty(testPropName, testAreaId) },
            { carPropertyRepository.getIntProperty(testPropName, testAreaId) },
            { carPropertyRepository.getIntArrayProperty(testPropName, testAreaId) },
            { carPropertyRepository.getLongProperty(testPropName, testAreaId) },
            { carPropertyRepository.getLongArrayProperty(testPropName, testAreaId) },
            { carPropertyRepository.getFloatProperty(testPropName, testAreaId) },
            { carPropertyRepository.getFloatArrayProperty(testPropName, testAreaId) },
        )

        testActions.forEach { action ->
            try {
                action()
                fail("Expected AppFunctionInvalidArgumentException was not thrown.")
            } catch (e: AppFunctionInvalidArgumentException) {
                assertTrue(e.message!!.contains("is currently not available"))
            }
        }
    }

    @Test
    fun `setters call CarPropertyManager with correct values`() {
        val testAreaId = 1
        val testPropId = 201
        val testPropName = "TEST_PROPERTY"
        val testProperty = VehiclePropertyConfig.Property(id = testPropId, name = testPropName, description = "")
        val allowedProperties = mapOf(testPropId to testProperty)
        val mockCarPropertyManager: CarPropertyManager = mock()
        carPropertyRepository = CarPropertyRepository(mockCarPropertyManager, allowedProperties)

        carPropertyRepository.setBooleanProperty(testPropName, testAreaId, true)
        carPropertyRepository.setIntProperty(testPropName, testAreaId, 99)
        carPropertyRepository.setFloatProperty(testPropName, testAreaId, 99.9f)
        carPropertyRepository.setStringProperty(testPropName, testAreaId, "New Value")
        carPropertyRepository.setLongProperty(testPropName, testAreaId, 999L)
        val testIntArray = intArrayOf(4, 5, 6)
        carPropertyRepository.setIntArrayProperty(testPropName, testAreaId, testIntArray)
        val testLongArray = longArrayOf(4L, 5L, 6L)
        carPropertyRepository.setLongArrayProperty(testPropName, testAreaId, testLongArray)
        val testFloatArray = floatArrayOf(4.4f, 5.5f, 6.6f)
        carPropertyRepository.setFloatArrayProperty(testPropName, testAreaId, testFloatArray)

        verify(mockCarPropertyManager).setBooleanProperty(testPropId, testAreaId, true)
        verify(mockCarPropertyManager).setIntProperty(testPropId, testAreaId, 99)
        verify(mockCarPropertyManager).setFloatProperty(testPropId, testAreaId, 99.9f)
        verify(mockCarPropertyManager).setProperty(String::class.java, testPropId, testAreaId, "New Value")
        verify(mockCarPropertyManager).setProperty(Long::class.java, testPropId, testAreaId, 999L)
        verify(mockCarPropertyManager).setProperty(Array<Int>::class.java, testPropId, testAreaId, testIntArray.toTypedArray())
        verify(mockCarPropertyManager).setProperty(Array<Long>::class.java, testPropId, testAreaId, testLongArray.toTypedArray())
        verify(mockCarPropertyManager).setProperty(Array<Float>::class.java, testPropId, testAreaId, testFloatArray.toTypedArray())
        verify(mockCarPropertyManager, never()).isPropertyAvailable(any(), any()) // do not check availability for setters.
    }

    @Test
    fun `setters throw exception when property is not authorized`() {
        val testAreaId = 1
        val unauthorizedPropName = "UNAUTHORIZED_PROPERTY"
        val allowedProperties = emptyMap<Int, VehiclePropertyConfig.Property>()
        val mockCarPropertyManager: CarPropertyManager = mock()
        carPropertyRepository = CarPropertyRepository(mockCarPropertyManager, allowedProperties)

        val testActions = listOf(
            { carPropertyRepository.setBooleanProperty(unauthorizedPropName, testAreaId, false) },
            { carPropertyRepository.setStringProperty(unauthorizedPropName, testAreaId, "val") },
            { carPropertyRepository.setIntProperty(unauthorizedPropName, testAreaId, 1) },
            { carPropertyRepository.setIntArrayProperty(unauthorizedPropName, testAreaId, intArrayOf(1, 2, 3)) },
            { carPropertyRepository.setLongProperty(unauthorizedPropName, testAreaId, 1L) },
            { carPropertyRepository.setLongArrayProperty(unauthorizedPropName, testAreaId, longArrayOf(1L)) },
            { carPropertyRepository.setFloatProperty(unauthorizedPropName, testAreaId, 1.0f) },
            { carPropertyRepository.setFloatArrayProperty(unauthorizedPropName, testAreaId, floatArrayOf(1.0f)) },
        )

        testActions.forEach { action ->
            try {
                action()
                fail("Expected AppFunctionInvalidArgumentException was not thrown.")
            } catch (e: AppFunctionInvalidArgumentException) {
                assertTrue(e.message!!.contains("does not exist or is not authorized"))
            }
        }
    }

    /**
     * Helper functions to create mocks for testing.
     */
    private fun <T> mockConfig(
        propertyId: Int,
        access: Int,
        propertyType: Class<T>,
        changeMode: Int,
        areaType: Int,
        areaIdConfigs: List<AreaIdConfig<T>>,
    ): CarPropertyConfig<T> {
        val mockConfig: CarPropertyConfig<T> = mock()
        val areaIds = areaIdConfigs.map { it.areaId }.toIntArray()

        whenever(mockConfig.propertyId).thenReturn(propertyId)
        whenever(mockConfig.propertyType).thenReturn(propertyType)
        whenever(mockConfig.access).thenReturn(access)
        whenever(mockConfig.areaType).thenReturn(areaType)
        whenever(mockConfig.changeMode).thenReturn(changeMode)
        whenever(mockConfig.areaIds).thenReturn(areaIds)
        whenever(mockConfig.areaIdConfigs).thenReturn(areaIdConfigs)

        return mockConfig
    }

    private fun <T> mockAreaIdConfig(
        areaId: Int,
        minValue: T?,
        maxValue: T?,
        supportedEnumValues: List<T>,
    ): AreaIdConfig<T> {
        val mockAreaIdConfig: AreaIdConfig<T> = mock()
        whenever(mockAreaIdConfig.areaId).thenReturn(areaId)
        whenever(mockAreaIdConfig.minValue).thenReturn(minValue)
        whenever(mockAreaIdConfig.maxValue).thenReturn(maxValue)
        whenever(mockAreaIdConfig.supportedEnumValues).thenReturn(supportedEnumValues)
        return mockAreaIdConfig
    }

    private fun <T> mockAreaIdConfig(areaId: Int): AreaIdConfig<T> = mockAreaIdConfig(areaId, null, null, emptyList())
}
