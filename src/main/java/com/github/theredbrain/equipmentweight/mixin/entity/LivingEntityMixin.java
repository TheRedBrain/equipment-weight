package com.github.theredbrain.equipmentweight.mixin.entity;

import com.github.theredbrain.equipmentweight.EquipmentWeight;
import com.github.theredbrain.equipmentweight.entity.AffectedByEquipmentWeight;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements AffectedByEquipmentWeight {

	@Shadow
	public abstract double getAttributeValue(EntityAttribute attribute);

	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow public abstract boolean removeStatusEffect(StatusEffect type);

	@Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

	@Unique
	private static final TrackedData<Float> OLD_EQUIPMENT_WEIGHT_RATIO = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	protected void equipmentweight$initDataTracker(CallbackInfo ci) {
		this.dataTracker.startTracking(OLD_EQUIPMENT_WEIGHT_RATIO, 0.0F);

	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void equipmentweight$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(EquipmentWeight.EQUIPMENT_WEIGHT)
				.add(EquipmentWeight.MAX_EQUIPMENT_WEIGHT)
		;
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void equipmentweight$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

		if (nbt.contains("old_equipment_weight_ratio", NbtElement.FLOAT_TYPE)) {
			this.equipmentweight$setOldEquipmentWeightRatio(nbt.getFloat("old_equipment_weight_ratio"));
		}

	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void equipmentweight$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

		float old_equipment_weight_ratio = this.equipmentweight$getOldEquipmentWeightRatio();
		if (old_equipment_weight_ratio != 0.0F) {
			nbt.putFloat("old_equipment_weight_ratio", old_equipment_weight_ratio);
		}

	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void equipmentweight$tick(CallbackInfo ci) {
		if (!this.getWorld().isClient) {
			var serverConfig = EquipmentWeight.serverConfig;
			float equipment_load = Math.max(0.0F, this.equipmentweight$getEquipmentWeight()) / (Math.max(1.0F, this.equipmentweight$getMaxEquipmentWeight()));
			if (equipment_load == this.equipmentweight$getOldEquipmentWeightRatio()) {
				return;
			}
			List<String> list = new ArrayList<>(serverConfig.weight_effects.keySet());
			List<StatusEffect> effectsToBeRemoved = new ArrayList<>();
			StatusEffect new_weight_status_effect = null;
			StatusEffect remove_this_status_effect = null;
			float current_threshold = 0.0F;
			String effect = null;
			String current_effect = null;

			for (String key : list) {
				float f = Float.parseFloat(key);
				if (equipment_load >= f && f >= current_threshold) {
					effect = serverConfig.weight_effects.get(key);
					current_effect = serverConfig.weight_effects.get(String.valueOf(current_threshold));
					if (effect != null) {
						new_weight_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(effect));
					}
					if (current_effect != null) {
						remove_this_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(current_effect));
					}
					current_threshold = f;
				} else {
					effect = serverConfig.weight_effects.get(key);
					if (effect != null) {
						remove_this_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(effect));
					}
				}
				if (remove_this_status_effect != null) {
					effectsToBeRemoved.add(remove_this_status_effect);
				}
			}

			if (new_weight_status_effect != null) {
				if (!this.hasStatusEffect(new_weight_status_effect)) {
					this.addStatusEffect(new StatusEffectInstance(new_weight_status_effect, -1, 0, true, false, true));
				}
			}

			for (StatusEffect statusEffect : effectsToBeRemoved) {
				this.removeStatusEffect(statusEffect);
			}

			this.equipmentweight$setOldEquipmentWeightRatio(equipment_load);
		}
	}

	@Override
	public float equipmentweight$getEquipmentWeight() {
		return (float) this.getAttributeValue(EquipmentWeight.EQUIPMENT_WEIGHT);
	}

	@Override
	public float equipmentweight$getMaxEquipmentWeight() {
		return (float) this.getAttributeValue(EquipmentWeight.MAX_EQUIPMENT_WEIGHT);
	}

	@Override
	public float equipmentweight$getOldEquipmentWeightRatio() {
		return this.dataTracker.get(OLD_EQUIPMENT_WEIGHT_RATIO);
	}

	@Override
	public void equipmentweight$setOldEquipmentWeightRatio(float newEquipmentWeightRatio) {
		this.dataTracker.set(OLD_EQUIPMENT_WEIGHT_RATIO, MathHelper.clamp(newEquipmentWeightRatio, 0.0F, 10.0F));
	}

}
