package com.github.theredbrain.equipmentweight.mixin.entity.attribute;

import com.github.theredbrain.equipmentweight.EquipmentWeight;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    static {
        EquipmentWeight.EQUIPMENT_WEIGHT = Registry.registerReference(Registries.ATTRIBUTE, EquipmentWeight.identifier("generic.equipment_weight"), new ClampedEntityAttribute("attribute.name.generic.equipment_weight", 0.0, 0.0, 1024.0).setTracked(true));
        EquipmentWeight.MAX_EQUIPMENT_WEIGHT = Registry.registerReference(Registries.ATTRIBUTE, EquipmentWeight.identifier("generic.max_equipment_weight"), new ClampedEntityAttribute("attribute.name.generic.max_equipment_weight", 10.0, 1.0, 1024.0).setTracked(true));
    }
}
