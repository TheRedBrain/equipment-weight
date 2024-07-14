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
			WIP
			""")
    public LinkedHashMap<Float, String> weight_effects = new LinkedHashMap<>() {

    };
    public ServerConfig() {

    }
}
