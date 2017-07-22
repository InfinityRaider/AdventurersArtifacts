package com.infinityraider.adventurersartifacts.registry;

import com.infinityraider.adventurersartifacts.artifacts.blinkdagger.ModuleBlinkDagger;
import com.infinityraider.adventurersartifacts.artifacts.butterfly.ModuleButterfly;
import com.infinityraider.adventurersartifacts.artifacts.etherealblade.ModuleEtherealBlade;
import com.infinityraider.adventurersartifacts.artifacts.hurricanepike.ModuleHurricanePike;
import com.infinityraider.adventurersartifacts.artifacts.mantastyle.ModuleMantaStyle;
import com.infinityraider.adventurersartifacts.artifacts.scytheofvyse.ModuleScytheOfVyse;
import com.infinityraider.adventurersartifacts.artifacts.shadowblade.ModuleShadowBlade;
import net.minecraft.item.Item;

public class ModItemRegistry {
    private static final ModItemRegistry INSTANCE = new ModItemRegistry();

    public static ModItemRegistry getInstance() {
        return INSTANCE;
    }

    private ModItemRegistry() {
        this.itemBlinkDagger = ModuleBlinkDagger.getInstance().itemBlinkDagger;
        this.itemButterfly = ModuleButterfly.getInstance().itemButterfly;
        this.itemEtherealBlade = ModuleEtherealBlade.getInstance().itemEtherealBlade;
        this.itemHurricanePike = ModuleHurricanePike.getInstance().itemHurricanePike;
        this.itemMantaStyle = ModuleMantaStyle.getInstance().itemMantaStyle;
        this.itemScytheOfVyse = ModuleScytheOfVyse.getInstance().itemScytheOfVyse;
        this.itemShadowBlade = ModuleShadowBlade.getInstance().itemShadowBlade;
    }

    public final Item itemBlinkDagger;
    public final Item itemButterfly;
    public final Item itemEtherealBlade;
    public final Item itemHurricanePike;
    public final Item itemMantaStyle;
    public final Item itemScytheOfVyse;
    public final Item itemShadowBlade;
}
