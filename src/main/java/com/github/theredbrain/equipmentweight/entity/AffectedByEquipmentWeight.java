package com.github.theredbrain.equipmentweight.entity;

public interface AffectedByEquipmentWeight {
	float equipmentweight$getEquipmentWeight();
	float equipmentweight$getMaxEquipmentWeight();
	String equipmentweight$getOldEquipmentWeightEffect();
	void equipmentweight$setOldEquipmentWeightEffect(String newEquipmentWeightEffect);
	float equipmentweight$getOldEquipmentWeightRatio();
	void equipmentweight$setOldEquipmentWeightRatio(float newEquipmentWeightRatio);
}
