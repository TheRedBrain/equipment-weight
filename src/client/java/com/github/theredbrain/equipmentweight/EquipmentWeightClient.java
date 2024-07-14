package com.github.theredbrain.equipmentweight;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class EquipmentWeightClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		// Packets
		ClientPlayNetworking.registerGlobalReceiver(EquipmentWeight.ServerConfigSync.ID, (client, handler, buf, responseSender) -> {
			EquipmentWeight.serverConfig = EquipmentWeight.ServerConfigSync.read(buf);
		});
	}
}