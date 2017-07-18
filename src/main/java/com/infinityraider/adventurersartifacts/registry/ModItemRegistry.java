package com.infinityraider.adventurersartifacts.registry;

import com.infinityraider.adventurersartifacts.artifacts.debugger.ItemDebugger;
import com.infinityraider.adventurersartifacts.artifacts.etherealblade.ModuleEtherealBlade;
import com.infinityraider.adventurersartifacts.artifacts.mantastyle.ModuleMantaStyle;
import com.infinityraider.adventurersartifacts.artifacts.shadowblade.ModuleShadowBlade;
import net.minecraft.item.Item;

public class ModItemRegistry {
    private static final ModItemRegistry INSTANCE = new ModItemRegistry();

    public static ModItemRegistry getInstance() {
        return INSTANCE;
    }

    private ModItemRegistry() {
        this.itemEtherealBlade = ModuleEtherealBlade.getInstance().itemEtherealBlade;
        this.itemMantaStyle = ModuleMantaStyle.getInstance().itemMantaStyle;
        this.itemShadowBlade = ModuleShadowBlade.getInstance().itemShadowBlade;
        this.itemDebugger = new ItemDebugger();
    }

    public final Item itemEtherealBlade;
    public final Item itemMantaStyle;
    public final Item itemShadowBlade;

    public final Item itemDebugger;
}
