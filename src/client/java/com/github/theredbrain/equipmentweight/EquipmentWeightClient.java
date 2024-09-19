package com.github.theredbrain.equipmentweight;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class EquipmentWeightClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(EquipmentWeight.ServerConfigSyncPacket.PACKET_ID, (payload, context) -> {
			EquipmentWeight.serverConfig = payload.serverConfig();
		});
	}
}