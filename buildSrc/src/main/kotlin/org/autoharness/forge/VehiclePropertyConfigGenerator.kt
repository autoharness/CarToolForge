/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.forge

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.joinToCode
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileNotFoundException

object VehiclePropertyConfigGenerator {

    private const val PACKAGE_NAME = "org.autoharness.cartool"
    private const val OUTPUT_CLASS_NAME = "VehiclePropertyConfig"
    private const val INNER_DATA_CLASS_NAME = "Property"
    private const val OUTPUT_PROPERTY_NAME = "allowedProperties"
    private const val KEY_LIST_PROPERTIES = "properties"
    private const val KEY_PROPERTY_NAME = "name"
    private const val KEY_PROPERTY_DESCRIPTION = "description"
    private const val  KEY_PROPERTY_ID = "id"

    // A private data class to safely hold parsed config data.
    private data class PropertyDefinition(val name: String, val description: String, val id: Int?)

    /**
     * Generates the VehiclePropertyConfig.kt file from a config file.
     *
     * @param input The configuration file.
     * @param outDir The directory to save the generated file.
     */
    fun generate(input: File, outDir: File) {
        if (!input.exists()) {
            throw FileNotFoundException("Config file not found at: ${input.path}")
        }

        val properties = parseYaml(input)
        val fileSpec = buildFileSpec(properties)

        outDir.mkdirs()
        fileSpec.writeTo(outDir)
        println("Successfully generated ${OUTPUT_CLASS_NAME}.kt in $outDir")
    }

    private fun parseYaml(inputFile: File): List<PropertyDefinition> {
        val yaml = Yaml()
        val data: Map<String, Any> = inputFile.reader().use { yaml.load(it) }

        @Suppress("UNCHECKED_CAST")
        val propertyMaps = data[KEY_LIST_PROPERTIES] as? List<Map<String, Any>>
            ?: throw IllegalArgumentException("YAML must contain a top-level key '${KEY_LIST_PROPERTIES}' with a list of properties.")

        // Check if there are duplicated properties defined.
        val names = propertyMaps.map {
            it[KEY_PROPERTY_NAME] as? String
                ?: throw IllegalArgumentException("A property is missing the required '$KEY_PROPERTY_NAME' key.")
        }
        val duplicateNames = names.groupBy { it }
            .filter { it.value.size > 1 }
            .keys
        if (duplicateNames.isNotEmpty()) {
            throw IllegalArgumentException(
                "The same property name is declared multiple times in ${inputFile.name}: ${
                    duplicateNames.joinToString(
                        ", ",
                    )
                }",
            )
        }

        val ids = propertyMaps.mapNotNull { it[KEY_PROPERTY_ID] as? Int }
        val duplicateIds = ids.groupBy { it }
            .filter { it.value.size > 1 }
            .keys
        if (duplicateIds.isNotEmpty()) {
            throw IllegalArgumentException(
                "The same property id is declared multiple times in ${inputFile.name}: ${
                    duplicateIds.joinToString(
                        ", ",
                    )
                }",
            )
        }
        // Verify the validity of non-system property Id.
        ids.map {
            // See https://android.googlesource.com/platform/hardware/interfaces/+/refs/heads/android16-release/automotive/vehicle/aidl_property/android/hardware/automotive/vehicle/VehiclePropertyGroup.aidl.
            if (it and 0xf0000000L.toInt() == 0x10000000) {
                throw IllegalArgumentException("An ID was provided for system property $it. Use the name only.")
            }
        }

        // Return metadata parsing from the configuration.
        return propertyMaps.map { prop ->
            val name = prop[KEY_PROPERTY_NAME] as String
            val description = prop[KEY_PROPERTY_DESCRIPTION] as String?
            if (description.isNullOrBlank()) {
                throw IllegalArgumentException("A description is missing for property $name.")
            }
            PropertyDefinition(
                name = name,
                description = description,
                id = prop[KEY_PROPERTY_ID] as Int?,
            )
        }
    }

    /**
     * Builds the entire Kotlin FileSpec using the parsed property data.
     */
    private fun buildFileSpec(properties: List<PropertyDefinition>): FileSpec {
        val configClassName = ClassName(PACKAGE_NAME, OUTPUT_CLASS_NAME)
        val propertyDataClassName = configClassName.nestedClass(INNER_DATA_CLASS_NAME)

        val propertyDataClass = buildPropertyDataClass(propertyDataClassName)
        val propertyMap = buildPropertyMap(properties, propertyDataClassName)

        val configObject = TypeSpec.objectBuilder(configClassName)
            .addType(propertyDataClass)
            .addProperty(
                PropertySpec.builder(
                    OUTPUT_PROPERTY_NAME,
                    MAP.parameterizedBy(Int::class.asTypeName(), propertyDataClassName),
                )
                    .initializer(propertyMap)
                    .build(),
            )
            .build()

        return FileSpec.builder(configClassName.packageName, configClassName.simpleName)
            .addImport("android.car", "VehiclePropertyIds")
            .addType(configObject)
            .build()
    }

    private fun buildPropertyDataClass(className: ClassName): TypeSpec = TypeSpec.classBuilder(className)
        .addModifiers(KModifier.DATA)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter(KEY_PROPERTY_NAME, String::class)
                .addParameter(KEY_PROPERTY_DESCRIPTION, String::class)
                .addParameter(KEY_PROPERTY_ID, Int::class)
                .build(),
        )
        .addProperty(
            PropertySpec.builder(KEY_PROPERTY_NAME, String::class)
                .initializer(KEY_PROPERTY_NAME).build(),
        )
        .addProperty(
            PropertySpec.builder(KEY_PROPERTY_DESCRIPTION, String::class)
                .initializer(KEY_PROPERTY_DESCRIPTION).build(),
        )
        .addProperty(
            PropertySpec.builder(KEY_PROPERTY_ID, Int::class)
                .initializer(KEY_PROPERTY_ID).build(),
        )
        .build()

    private fun buildPropertyMap(
        properties: List<PropertyDefinition>,
        propertyClassName: ClassName,
    ): CodeBlock {
        val mapEntries = properties.map { prop ->
            val idCode = prop.id?.toString() ?: "VehiclePropertyIds.${prop.name}"

            val propertyConstructor = CodeBlock.of(
                "%T(name = %S, description = %S, id = %L)",
                propertyClassName,
                prop.name,
                prop.description,
                idCode,
            )
            CodeBlock.of("%L to %L", idCode, propertyConstructor)
        }.joinToCode(separator = ",\n")

        return CodeBlock.builder()
            .add("mapOf(\n")
            .indent()
            .add(mapEntries)
            .unindent()
            .add("\n)")
            .build()
    }
}
