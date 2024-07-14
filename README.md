# Equipment Weight

This mod adds a weight system for equipment.

This mod is an API for mod and data pack authors. It does nothing on its own.

## Implementation

The ratio of the two new entity attributes **_generic.equipment_weight_** and **_generic.max_equipment_weight_** is compared to a configurable list of values. When the ratio is equal or greater than one of the values the corresponding status effect is applied. Only one status effect will be applied.

The default attribute values are:
- 0.0 for **_generic.equipment_weight_**
- 10.0 for **_generic.max_equipment_weight_**

## Configuration

The server config contains a map of float values to strings. The strings should be valid identifiers for status effects. Invalid strings are ignored.

### Example

    "weight_effects": {
        "0.2": "minecraft:strength",
        "0.5": "minecraft:haste",
        "0.4": "minecraft:regeneration",
        "1.0": "minecraft:glowing"
    }

Using this config and the default value for **_generic.max_equipment_weight_** a LivingEntity with a **_generic.equipment_weight_** of 5.0 would be affected by the "Haste" effect.
Changing the value of **_generic.equipment_weight_** to 3.0 would remove the "Haste" effect and apply the "Strength" effect
