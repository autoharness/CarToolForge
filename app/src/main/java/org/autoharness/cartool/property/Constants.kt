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
import android.car.VehiclePropertyType
import android.car.hardware.CarPropertyConfig

/**
 * Defines constants used for vehicle property configurations.
 */
object Constants {
    /** A set of supported vehicle property access types. */
    val SUPPORTED_ACCESS_VALUES = setOf(
        CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ,
        CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_WRITE,
        CarPropertyConfig.VEHICLE_PROPERTY_ACCESS_READ_WRITE,
    )

    /** A set of supported vehicle area types. */
    val SUPPORTED_AREA_TYPES = setOf(
        VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,
        VehicleAreaType.VEHICLE_AREA_TYPE_WINDOW,
        VehicleAreaType.VEHICLE_AREA_TYPE_SEAT,
        VehicleAreaType.VEHICLE_AREA_TYPE_DOOR,
        VehicleAreaType.VEHICLE_AREA_TYPE_MIRROR,
        VehicleAreaType.VEHICLE_AREA_TYPE_WHEEL,
    )

    /** A set of supported vehicle property change modes. */
    val SUPPORTED_CHANGE_MODES = setOf(
        CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC,
        CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_ONCHANGE,
        CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_CONTINUOUS,
    )

    /** A map of supported data types to their corresponding vehicle property types. */
    val SUPPORTED_DATA_TYPE_MAP: Map<Class<*>, Int> = mapOf(
        java.lang.Boolean::class.java to VehiclePropertyType.BOOLEAN,
        java.lang.Float::class.java to VehiclePropertyType.FLOAT,
        java.lang.Integer::class.java to VehiclePropertyType.INT32,
        java.lang.Long::class.java to VehiclePropertyType.INT64,
        Array<Float>::class.java to VehiclePropertyType.FLOAT_VEC,
        Array<Int>::class.java to VehiclePropertyType.INT32_VEC,
        Array<Long>::class.java to VehiclePropertyType.INT64_VEC,
        java.lang.String::class.java to VehiclePropertyType.STRING,
    )

    /** A bitmask representing all window areas. */
    private const val AREA_ALL_WINDOW: Int = (
        VehicleAreaWindow.WINDOW_FRONT_WINDSHIELD or
            VehicleAreaWindow.WINDOW_REAR_WINDSHIELD or
            VehicleAreaWindow.WINDOW_ROW_1_LEFT or
            VehicleAreaWindow.WINDOW_ROW_1_RIGHT or
            VehicleAreaWindow.WINDOW_ROW_2_LEFT or
            VehicleAreaWindow.WINDOW_ROW_2_RIGHT or
            VehicleAreaWindow.WINDOW_ROW_3_LEFT or
            VehicleAreaWindow.WINDOW_ROW_3_RIGHT or
            VehicleAreaWindow.WINDOW_ROOF_TOP_1 or
            VehicleAreaWindow.WINDOW_ROOF_TOP_2
        )

    /** A bitmask representing all seat areas. */
    private const val AREA_ALL_SEAT: Int = (
        VehicleAreaSeat.SEAT_ROW_1_LEFT or
            VehicleAreaSeat.SEAT_ROW_1_CENTER or
            VehicleAreaSeat.SEAT_ROW_1_RIGHT or
            VehicleAreaSeat.SEAT_ROW_2_LEFT or
            VehicleAreaSeat.SEAT_ROW_2_CENTER or
            VehicleAreaSeat.SEAT_ROW_2_RIGHT or
            VehicleAreaSeat.SEAT_ROW_3_LEFT or
            VehicleAreaSeat.SEAT_ROW_3_CENTER or
            VehicleAreaSeat.SEAT_ROW_3_RIGHT
        )

    /** A bitmask representing all door areas. */
    private const val AREA_ALL_DOOR: Int = (
        VehicleAreaDoor.DOOR_ROW_1_LEFT or
            VehicleAreaDoor.DOOR_ROW_1_RIGHT or
            VehicleAreaDoor.DOOR_ROW_2_LEFT or
            VehicleAreaDoor.DOOR_ROW_2_RIGHT or
            VehicleAreaDoor.DOOR_ROW_3_LEFT or
            VehicleAreaDoor.DOOR_ROW_3_RIGHT or
            VehicleAreaDoor.DOOR_HOOD or
            VehicleAreaDoor.DOOR_REAR
        )

    /** A bitmask representing all mirror areas. */
    private const val AREA_ALL_MIRROR: Int = (
        VehicleAreaMirror.MIRROR_DRIVER_LEFT or
            VehicleAreaMirror.MIRROR_DRIVER_RIGHT or
            VehicleAreaMirror.MIRROR_DRIVER_CENTER
        )

    /** A bitmask representing all wheel areas. */
    private const val AREA_ALL_WHEEL: Int = (
        VehicleAreaWheel.WHEEL_LEFT_FRONT or
            VehicleAreaWheel.WHEEL_RIGHT_FRONT or
            VehicleAreaWheel.WHEEL_LEFT_REAR or
            VehicleAreaWheel.WHEEL_RIGHT_REAR
        )

    /** A map of vehicle area types to their corresponding area ID bitmasks. */
    val AREA_ID_MASKS = mapOf(
        VehicleAreaType.VEHICLE_AREA_TYPE_WINDOW to AREA_ALL_WINDOW,
        VehicleAreaType.VEHICLE_AREA_TYPE_SEAT to AREA_ALL_SEAT,
        VehicleAreaType.VEHICLE_AREA_TYPE_DOOR to AREA_ALL_DOOR,
        VehicleAreaType.VEHICLE_AREA_TYPE_MIRROR to AREA_ALL_MIRROR,
        VehicleAreaType.VEHICLE_AREA_TYPE_WHEEL to AREA_ALL_WHEEL,
    )

    /** A description for a global area ID. */
    const val GLOBAL_AREA_DESCRIPTION =
        "Use this 'areaId' value to apply the property to the entire vehicle."

    /** Templates for generating descriptions for specific area IDs. */
    const val AREA_SPECIFIC_DESCRIPTION_TEMPLATE =
        "This 'areaId' value targets the vehicle property for %s."
    val AREA_DECODER_MAP = mapOf(
        VehicleAreaType.VEHICLE_AREA_TYPE_WINDOW to mapOf(
            VehicleAreaWindow.WINDOW_FRONT_WINDSHIELD to "front windshield",
            VehicleAreaWindow.WINDOW_REAR_WINDSHIELD to "rear windshield",
            VehicleAreaWindow.WINDOW_ROW_1_LEFT to "first row left window",
            VehicleAreaWindow.WINDOW_ROW_1_RIGHT to "first row right window",
            VehicleAreaWindow.WINDOW_ROW_2_LEFT to "second row left window",
            VehicleAreaWindow.WINDOW_ROW_2_RIGHT to "second row right window",
            VehicleAreaWindow.WINDOW_ROW_3_LEFT to "third row left window",
            VehicleAreaWindow.WINDOW_ROW_3_RIGHT to "third row right window",
            VehicleAreaWindow.WINDOW_ROOF_TOP_1 to "first top roof window",
            VehicleAreaWindow.WINDOW_ROOF_TOP_2 to "second top roof window",
        ),
        VehicleAreaType.VEHICLE_AREA_TYPE_SEAT to mapOf(
            VehicleAreaSeat.SEAT_ROW_1_LEFT to "first row left seat",
            VehicleAreaSeat.SEAT_ROW_1_CENTER to "first row center seat",
            VehicleAreaSeat.SEAT_ROW_1_RIGHT to "first row right seat",
            VehicleAreaSeat.SEAT_ROW_2_LEFT to "second row left seat",
            VehicleAreaSeat.SEAT_ROW_2_CENTER to "second row center seat",
            VehicleAreaSeat.SEAT_ROW_2_RIGHT to "second row right seat",
            VehicleAreaSeat.SEAT_ROW_3_LEFT to "third row left seat",
            VehicleAreaSeat.SEAT_ROW_3_CENTER to "third row center seat",
            VehicleAreaSeat.SEAT_ROW_3_RIGHT to "third row right seat",
        ),
        VehicleAreaType.VEHICLE_AREA_TYPE_DOOR to mapOf(
            VehicleAreaDoor.DOOR_ROW_1_LEFT to "first row left door",
            VehicleAreaDoor.DOOR_ROW_1_RIGHT to "first row right door",
            VehicleAreaDoor.DOOR_ROW_2_LEFT to "second row left door",
            VehicleAreaDoor.DOOR_ROW_2_RIGHT to "second row right door",
            VehicleAreaDoor.DOOR_ROW_3_LEFT to "third row left door",
            VehicleAreaDoor.DOOR_ROW_3_RIGHT to "third row right door",
            VehicleAreaDoor.DOOR_HOOD to "hood",
            VehicleAreaDoor.DOOR_REAR to "trunk lid",
        ),
        VehicleAreaType.VEHICLE_AREA_TYPE_MIRROR to mapOf(
            VehicleAreaMirror.MIRROR_DRIVER_LEFT to "left side mirror",
            VehicleAreaMirror.MIRROR_DRIVER_RIGHT to "right side mirror",
            VehicleAreaMirror.MIRROR_DRIVER_CENTER to "rearview mirror",
        ),
        VehicleAreaType.VEHICLE_AREA_TYPE_WHEEL to mapOf(
            VehicleAreaWheel.WHEEL_LEFT_FRONT to "left front wheel",
            VehicleAreaWheel.WHEEL_RIGHT_FRONT to "right front wheel",
            VehicleAreaWheel.WHEEL_LEFT_REAR to "left rear wheel",
            VehicleAreaWheel.WHEEL_RIGHT_REAR to "right rear wheel",
        ),
    )
}
