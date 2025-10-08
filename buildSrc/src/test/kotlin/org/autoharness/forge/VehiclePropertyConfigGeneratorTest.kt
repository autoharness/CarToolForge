/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package org.autoharness.forge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.FileNotFoundException

class VehiclePropertyConfigGeneratorTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    /**
     * Helper function to create a test YAML file with given content.
     */
    private fun createTestConfig(content: String): File {
        val inputFile = File(tempFolder.root, "test.yaml")
        inputFile.writeText(content.trimIndent())
        return inputFile
    }

    @Test
    fun `generate() should create kt file successfully for valid config`() {
        val content = """
            properties:
              - name: "INFO_VIN"
                description: "Vehicle Identification Number."
              - name: "CUSTOM_PROPERTY"
                id: 591397123
                description: "A custom property for feature XYZ"
        """
        val inputFile = createTestConfig(content)
        val expectedOutputFile = File(tempFolder.root, "org/autoharness/cartool/VehiclePropertyConfig.kt")
        VehiclePropertyConfigGenerator.generate(inputFile, tempFolder.root)

        assertTrue("The output Kotlin file should be created.", expectedOutputFile.exists())
        val generatedCode = expectedOutputFile.readText()
        assertTrue("Generated code should contain the object declaration.", generatedCode.contains("object VehiclePropertyConfig"))
        assertTrue("Generated code should contain the allowedProperties map.", generatedCode.contains("public val allowedProperties: Map<Int, Property>"))
        assertTrue("Generated code should contain the system property.", generatedCode.contains("VehiclePropertyIds.INFO_VIN to Property(name = \"INFO_VIN\""))
        assertTrue("Generated code should contain the custom property.", generatedCode.contains("591397123 to Property(name = \"CUSTOM_PROPERTY\""))
    }

    @Test
    fun `generate() should throw FileNotFoundException when input file does not exist`() {
        val nonExistentFile = File(tempFolder.root, "non_existent.yaml")

        val exception = assertThrows(FileNotFoundException::class.java) {
            VehiclePropertyConfigGenerator.generate(nonExistentFile, tempFolder.root)
        }
        assertEquals("Config file not found at: ${nonExistentFile.path}", exception.message)
    }

    @Test
    fun `generate() should throw exception for yaml missing 'properties' key`() {
        val content = """
            some_other_key:
              - name: "prop1"
        """
        val inputFile = createTestConfig(content)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            VehiclePropertyConfigGenerator.generate(inputFile, tempFolder.root)
        }
        assertEquals("YAML must contain a top-level key 'properties' with a list of properties.", exception.message)
    }

    @Test
    fun `generate() should throw exception for duplicate property names`() {
        val yamlContent = """
            properties:
              - name: "DUPLICATE_NAME"
                description: "desc 1"
              - name: "ANOTHER_PROP"
                description: "desc 2"
              - name: "DUPLICATE_NAME"
                description: "desc 3"
        """
        val inputFile = createTestConfig(yamlContent)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            VehiclePropertyConfigGenerator.generate(inputFile, tempFolder.root)
        }
        assertEquals("The same property name is declared multiple times in test.yaml: DUPLICATE_NAME", exception.message)
    }

    @Test
    fun `generate() should throw exception for duplicate property ids`() {
        val content = """
            properties:
              - name: "PROP1"
                description: "desc 1"
                id: 12345
              - name: "PROP2"
                description: "desc 2"
                id: 12345
        """
        val inputFile = createTestConfig(content)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            VehiclePropertyConfigGenerator.generate(inputFile, tempFolder.root)
        }
        assertEquals("The same property id is declared multiple times in test.yaml: 12345", exception.message)
    }

    @Test
    fun `generate() should throw exception for a system property with id`() {
        val systemId = 356516106
        val yamlContent = """
            properties:
              - name: "INFO_DRIVER_SEAT"
                description: "Driver's seat location"
                id: $systemId
        """
        val inputFile = createTestConfig(yamlContent)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            VehiclePropertyConfigGenerator.generate(inputFile, tempFolder.root)
        }
        assertEquals("An ID was provided for system property $systemId. Use the name only.", exception.message)
    }

    @Test
    fun `generate() should throw exception when a property is missing the 'name' key`() {
        val content = """
            properties:
              - description: "This property is missing its name"
                id: 999
        """
        val inputFile = createTestConfig(content)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            VehiclePropertyConfigGenerator.generate(inputFile, tempFolder.root)
        }
        assertEquals("A property is missing the required 'name' key.", exception.message)
    }

    @Test
    fun `generate() should throw exception when a property is missing the description`() {
        val content = """
            properties:
              - name: "CUSTOM"
                description: " "
                id: 999
        """
        val inputFile = createTestConfig(content)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            VehiclePropertyConfigGenerator.generate(inputFile, tempFolder.root)
        }
        assertEquals("A description is missing for property CUSTOM.", exception.message)
    }
}
