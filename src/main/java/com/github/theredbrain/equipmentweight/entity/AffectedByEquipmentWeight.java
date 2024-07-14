package com.github.theredbrain.equipmentweight.entity;

public interface AffectedByEquipmentWeight {
	float equipmentweight$getEquipmentWeight();
	float equipmentweight$getMaxEquipmentWeight();
	float equipmentweight$getOldEquipmentWeightRatio();
	void equipmentweight$setOldEquipmentWeightRatio(float newEquipmentWeightRatio);
}
