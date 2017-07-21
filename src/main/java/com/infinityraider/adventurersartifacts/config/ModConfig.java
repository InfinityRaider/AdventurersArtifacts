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

    public Configuration getConfiguration() {
        return this.config;
    }

    public void init(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
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

    @SideOnly(Side.CLIENT)
    private void loadClientConfiguration(FMLPreInitializationEvent event) {

    }
}
