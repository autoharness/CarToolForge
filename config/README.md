The `vehicle_properties.yaml` file specifies an **allowlist** of vehicle properties that the agent is permitted to access.

## Property Fields

Each property in the allowlist is described using the following fields:

- **`name`**: **String**
  - **Required**. A unique string identifier for the property.
- **`id`**: **Integer**
  - *Optional*. The unique integer identifier for the property. This is **required** for **vendor extended properties**.
- **`categories`**: **List\<String\>**
  - **Required**. One or more categories the property belongs to. Supported values:
    - `BODY_CONTROL`: Manages physical body components such as doors, windows, mirrors, and physical locks.
    - `HVAC_SYSTEM`: Manages heating, ventilation, air conditioning, and seat/steering thermal regulation.
    - `ENERGY_MANAGEMENT`: Manages energy sources, including EV battery states, fuel levels, and charging interfaces.
    - `CHASSIS_AND_DYNAMICS`: Manages vehicle movement metrics, speed, braking systems, and wheel/tire statuses.
    - `LIGHTING_SYSTEM`: Manages all interior and exterior illumination, including headlights, fog lights, and cabin lights..
    - `VEHICLE_INFO`: Represents static identity, hardware specifications, and manufacturing details.
- **`description`**: **String**
  - **Required**. A detailed, human-readable description. It should clearly explain the property's purpose, units, and potential applications.

## Examples

For [Supported System Properties](https://source.android.com/docs/automotive/vhal/system-properties), you need to specify the `name`, `categories`, and `description`. The `id` is not required.

```yaml
- name: "INFO_MODEL"
  categories: ["VEHICLE_INFO"]
  description: "The vehicle's model name"
```

For [Vendor-Extended Properties](https://source.android.com/docs/automotive/vhal/special-properties#vendor-properties), the `id` field must be included along with the `name`, `categories`, and `description`.

```yaml
- name: "CUSTOM_PROPERTY"
  id: 591397123
  categories: ["BODY_CONTROL"]
  description: "A custom property for feature XYZ"
```

## Add a New Property

When adding a new property to the list, please ensure the following:

- [ ] The property does not compromise the safe operation of the vehicle.
- [ ] The property does not involve user privacy.
- [ ] The required permission for the property is declared in `AndroidManifest.xml`. If the permission is a dangerous or privileged permission, ensure that `default-permissions-cartoolforge.xml` and `privapp-permissions-cartoolforge.xml` are updated accordingly.
