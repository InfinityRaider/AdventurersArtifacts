package com.infinityraider.adventurersartifacts.artifacts.scytheofvyse;

import com.infinityraider.adventurersartifacts.artifacts.ArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.registry.ModPotionRegistry;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleScytheOfVyse extends ArtifactModuleWeaponWithAbility {
    private static final ModuleScytheOfVyse INSTANCE = new ModuleScytheOfVyse();

    public static ModuleScytheOfVyse getInstance() {
        return INSTANCE;
    }

    public final ItemScytheOfVyse itemScytheOfVyse;
    public final PotionHex potionHex;

    private int duration;
    private int range;

    private ModuleScytheOfVyse() {
        super("scythe_of_vyse");
        this.itemScytheOfVyse = new ItemScytheOfVyse();
        this.potionHex = new PotionHex();
    }

    public int getDuration() {
        return duration;
    }

    public ModuleScytheOfVyse setDuration(int time) {
        this.duration = time <= 0 ? 0 : time;
        return this;
    }

    public int getRange() {
        return this.range;
    }

    public ModuleScytheOfVyse setRange(int range) {
        this.range = range < 0 ? 0 : range;
        return this;
    }

    @Override
    public ModuleScytheOfVyse loadConfiguration(Configuration modConfig) {
        this.setDuration(modConfig.getInt("scythe of vyse hex duration", this.getName(), 80, 0, 9000000,
                "The duration of scythe of vyse's hex in ticks"));
        this.setCooldown(modConfig.getInt("scythe of vyse cooldown", this.getName(), 400, 0, 9000000,
                "The cooldown of using scythe of vyse's hex in ticks"));
        this.setRange(modConfig.getInt("scythe of vyse range", this.getName(), 16, 0, 128,
                "The maximum range from which the scythe of vyse can hex a target"));
        this.setAttackDamage(modConfig.getInt("scythe of vyse attack damage", this.getName(), 12, 0, 20,
                "The attack damage of the scythe of vyse (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("scythe of vyse attack speed", this.getName(), 3.4F, 0, 10,
                "The attack cooldown of the scythe of vyse, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }

    @Override
    public ModuleScytheOfVyse registerMessages(INetworkWrapper networkWrapper) {
        networkWrapper.registerMessage(MessageHexed.class);
        return this;
    }

    @Override
    public ModuleScytheOfVyse registerEventHandlers(IProxyBase proxy) {
        proxy.registerEventHandler(HexedHandler.getInstance());
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModuleScytheOfVyse registerEventHandlersClient(IClientProxyBase proxy) {
        proxy.registerEventHandler(RenderHexedHandler.getInstance());
        return this;
    }

    @Override
    public ModuleScytheOfVyse registerPotions(ModPotionRegistry registry, Configuration modConfig) {
        registry.registerPotion(this.potionHex, this.getName(), modConfig, this.getName());
        return this;
    }
}
