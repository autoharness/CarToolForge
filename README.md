# 🍃Car Tool Forge✨

**CarToolForge** is a tool built on **Android Automotive OS (AAOS)** that connects a Large Language Model (LLM) to vehicle systems. It uses the [Car API](https://developer.android.com/reference/android/car/Car) to access vehicle functions and makes them available to the LLM through the [App Functions](https://developer.android.com/reference/android/app/appfunctions/AppFunctionManager) framework.

## Features

- **Query and Control Vehicle Properties:** Enables an LLM to query the status of [vehicle properties](https://developer.android.com/reference/android/car/VehiclePropertyIds) and perform control actions (e.g., turning on the A/C) via a simple API.

To see this in action, check out the **[CarToolPlayground](https://github.com/autoharness/CarToolPlayground)** project, which provides an agent-based chat experience built on **CarToolForge**.

## Build

You can build the project using Gradle:

```
./gradlew clean build
```

## Installation

### Prerequisites

As of [android-16.0.0_r2](https://source.android.com/docs/setup/reference/build-numbers#source-code-tags-and-builds), **App Functions** is an experimental feature and may be disabled by default in your system.

You can check whether the essential feature flags are enabled on your device or emulator using the following command：

```
adb shell aflags list | grep "enable_app_functions_schema_parser"
```

If this flag is not enabled, you need to enable them in your AOSP build. For detailed instructions, refer to the official guide on [Set feature launch flag values](https://source.android.com/docs/setup/build/feature-flagging/set-values).

> [!NOTE]
>
> You will likely need to enable both `enable_app_functions` and `enable_app_functions_schema_parser`. For example：
>
> ```
> flag_value {
>      package: "com.android.appsearch.flags"
>      name: "enable_app_functions_schema_parser"
>      state: ENABLED
>      permission: READ_ONLY
> }
>
> flag_value {
>      package: "com.android.appsearch.flags"
>      name: "enable_app_functions"
>      state: ENABLED
>      permission: READ_ONLY
> }
> ```

### System Pre-installation

**CarToolForge** must be installed as a privileged system application to access vehicle APIs.

To facilitate out-of-the-box integration, the necessary configuration and build files are already included in the `integration/cartoolforge` directory. These files are bundled into a package for each release, which you can download from the **[Releases](https://github.com/autoharness/CarToolForge/releases)** page. For step-by-step instructions, follow the [integration_guide.md](integration/cartoolforge/integration_guide.md).

### Debug Installation

For development and debugging, you can sideload the application onto a rooted device or emulator.

1. **Disable Permission Enforcement:** First, modify the `build.prop` file to disable privileged permission enforcement.

```
adb root
adb remount
adb shell "sed -i 's/ro.control_privapp_permissions=enforce/ro.control_privapp_permissions=log/g' /vendor/build.prop"
```

2. **Install as a Privileged App:** Push the APK to the privileged apps directory, such as `/system/priv-app`.

```
adb push app-debug.apk /system/priv-app
adb reboot
```

3. **Grant Runtime Permissions:** After rebooting, use the `adb install -g` command to reinstall the app and grant all permissions defined in its manifest.

```
adb install -g app-debug.apk
```

## Vehicle Properties Configuration

For security, **CarToolForge** uses an allowlist to control the agent's access to vehicle properties. This list is defined in the [`vehicle_properties.yaml`](config/vehicle_properties.yaml) file.  For details, see [`config/README.md`](config/README.md).

## Limitations

1. **Simplified API Surface:**  To make the API easier for an LLM to consume, **CarToolForge** intentionally omits some of the more complex features of the Car API. This may affect results in certain cases. Unsupported features include：
   - Area-level access, see [AreaIdConfig#getAccess](https://developer.android.com/reference/android/car/hardware/property/AreaIdConfig#getAccess()).
   - Dynamic min/max supported values, see [CarPropertyManager#getMinMaxSupportedValue](https://developer.android.com/reference/android/car/hardware/property/CarPropertyManager#getMinMaxSupportedValue(int,%20int)).
   - [`BYTES`](https://android.googlesource.com/platform/packages/services/Car/+/refs/heads/android16-release/car-lib/src/android/car/VehiclePropertyType.java#39) and [`MIXED`](https://android.googlesource.com/platform/packages/services/Car/+/refs/heads/android16-release/car-lib/src/android/car/VehiclePropertyType.java#40) property data types.
   - [VEHICLE_AREA_TYPE_VENDOR](https://developer.android.com/reference/android/car/VehicleAreaType#VEHICLE_AREA_TYPE_VENDOR) area type.

2. **Model Requirements:**  Achieving good results with the current function complexity requires a model with excellent reasoning capabilities and a large parameter size (>20B). Further work is required to explore potential avenues for optimization.

## Contributing

Take a look at the [`CONTRIBUTING.md`](CONTRIBUTING.md).
