package com.infinityraider.addartifacts.artifacts.debugger;

import com.google.common.collect.ImmutableList;
import com.infinityraider.addartifacts.AddArtifacts;
import com.infinityraider.addartifacts.reference.Reference;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemDebuggerBase;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class ItemDebugger extends ItemDebuggerBase implements IItemWithModel {
    public ItemDebugger() {
        super(false);
        this.setCreativeTab(AddArtifacts.MOD_TAB);
    }

    @Override
    protected List<DebugMode> getDebugModes() {
        List<DebugMode> list = new ArrayList<>();
        return list;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":debugger", "inventory")));
    }
}
