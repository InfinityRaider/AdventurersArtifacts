package com.infinityraider.addartifacts.registry;

import com.infinityraider.addartifacts.artifacts.debugger.ItemDebugger;
import com.infinityraider.addartifacts.artifacts.mantastyle.ModuleMantaStyle;
import net.minecraft.item.Item;

public class ModItemRegistry {
    private static final ModItemRegistry INSTANCE = new ModItemRegistry();

    public static ModItemRegistry getInstance() {
        return INSTANCE;
    }

    private ModItemRegistry() {
        this.itemMantaStyle = ModuleMantaStyle.getInstance().itemMantaStyle;
        this.itemDebugger = new ItemDebugger();
    }

    public final Item itemMantaStyle;
    public final Item itemDebugger;
}
