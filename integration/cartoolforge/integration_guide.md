This guide explains how to integrate **CarToolForge** into an Android System Image as a prebuilt, privileged application.

Follow these steps to integrate:

1. Copy the current directory (`cartoolforge`) to the path `$ANDROID_BUILD_TOP/vendor/autoharness/packages/apps`.

   > [!NOTE]
   >
   > The path can be adjusted according to your project's source code organization; the one provided here is for example purposes only.

2. To ensure the application is included in your system image, add `CarToolForgePrebuilt` to the `PRODUCT_PACKAGES` variable in your target product's makefile.

   For example, you would add the following to `$ANDROID_BUILD_TOP/packages/services/Car/car_product/car_system.mk`:

   ```
   PRODUCT_PACKAGES += \
       CarToolForgePrebuilt \
   ```

3. Compile the system image. Build the AOSP source code from the root directory:

   ```
   source build/envsetup.sh && lunch <your_target_product> && m -j$(nproc)
   ```

   > [!NOTE]
   >
   > In the command above, replace `<your_target_product>` with the actual target for your build product (e.g., `sdk_car_x86_64-aosp_current-userdebug`).

   After the build completes, verify that the `CarToolForgePrebuilt` directory and its APK are located in the output directory: `$ANDROID_PRODUCT_OUT/system/priv-app/`.

4. Flash the newly built system image onto your target device. Once the device boots, confirm that the `CarToolForge` is installed and working correctly.
