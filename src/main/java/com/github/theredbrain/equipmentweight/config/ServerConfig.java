package com.github.theredbrain.equipmentweight.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.LinkedHashMap;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("""
			When the ratio of 'generic.equipment_weight' and 'generic.max_equipment_weight' is equal or greater than
			a value, and the corresponding string is a valid identifier of a status effect, that status effect
			is applied.
			Only one status effect is applied.
			
			Example:
			"weight_effects": {
				"0.2": "minecraft:strength",
				"0.5": "minecraft:haste",
				"0.4": "minecraft:regeneration",
				"1.0": "minecraft:glowing"
			}
			""")
    public LinkedHashMap<String, String> weight_effects = new LinkedHashMap<>() {{

	}};
    public ServerConfig() {

    }
}
