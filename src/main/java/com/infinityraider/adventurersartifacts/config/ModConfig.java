package com.infinityraider.adventurersartifacts.config;

import com.infinityraider.adventurersartifacts.AdventurersArtifacts;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModConfig {
    private static final ModConfig INSTANCE = new ModConfig();

    private ModConfig() {}

    public static ModConfig getInstance() {
        return INSTANCE;
    }

    private Configuration config;

    //debug
    public boolean debug;

    public Configuration getConfiguration() {
        return this.config;
    }

    public void init(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
        loadConfiguration();
        AdventurersArtifacts.ARTIFACTS.forEach(a -> a.loadConfiguration(config));
        if(config.hasChanged()) {
            config.save();
        }
        AdventurersArtifacts.instance.getLogger().debug("Configuration Loaded");
    }

    @SideOnly(Side.CLIENT)
    public void initClientConfigs(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
        loadClientConfiguration(event);
        AdventurersArtifacts.ARTIFACTS.forEach(a -> a.loadClientConfiguration(config));
        if(config.hasChanged()) {
            config.save();
        }
        AdventurersArtifacts.instance.getLogger().debug("Client configuration Loaded");
    }

    private void loadConfiguration() {
        //debug
        debug = config.getBoolean("debug", Categories.DEBUG.getName(), false, "Set to true if you wish to enable debug mode");
    }

    @SideOnly(Side.CLIENT)
    private void loadClientConfiguration(FMLPreInitializationEvent event) {

    }

    public enum Categories {
        GENERAL("general"),
        CLIENT("client"),
        DEBUG("debug");

        private final String name;

        Categories(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
