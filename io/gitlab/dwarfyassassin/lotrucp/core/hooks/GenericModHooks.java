/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.registry.GameData
 *  cpw.mods.fml.common.registry.RegistryDelegate
 *  cpw.mods.fml.common.registry.RegistryDelegate$Delegate
 *  cpw.mods.fml.relauncher.ReflectionHelper
 *  net.minecraft.block.Block
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.oredict.OreDictionary
 */
package io.gitlab.dwarfyassassin.lotrucp.core.hooks;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.RegistryDelegate;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GenericModHooks {
    public static void setItemDelagateName(Item item, String name) {
        RegistryDelegate.Delegate delegate = (RegistryDelegate.Delegate)item.delegate;
        ReflectionHelper.setPrivateValue(RegistryDelegate.Delegate.class, delegate, (Object)name, (String[])new String[]{"name"});
    }

    public static void setBlockDelagateName(Block block, String name) {
        RegistryDelegate.Delegate delegate = (RegistryDelegate.Delegate)block.delegate;
        ReflectionHelper.setPrivateValue(RegistryDelegate.Delegate.class, delegate, (Object)name, (String[])new String[]{"name"});
    }

    public static void removeBlockFromOreDictionary(Block block) {
        GenericModHooks.removeItemFromOreDictionary(Item.getItemFromBlock((Block)block));
    }

    public static void removeItemFromOreDictionary(Item item) {
        if (item == null) {
            return;
        }
        ItemStack stack = new ItemStack(item, 1, 32767);
        int[] oreIDs = OreDictionary.getOreIDs((ItemStack)stack);
        List oreIdToStacks = (List)ReflectionHelper.getPrivateValue(OreDictionary.class, null, (String[])new String[]{"idToStack"});
        for (int oreID : oreIDs) {
            ArrayList<ItemStack> oreStacks = (ArrayList)oreIdToStacks.get(oreID);
            if (oreStacks == null) continue;
            HashSet<ItemStack> toRemove = new HashSet<ItemStack>();
            for (ItemStack oreStack : oreStacks) {
                if (oreStack.getItem() != stack.getItem()) continue;
                toRemove.add(oreStack);
            }
            oreStacks.removeAll(toRemove);
        }
        String registryName = stack.getItem().delegate.name();
        if (registryName == null) {
            return;
        }
        int stackId = GameData.getItemRegistry().getId(registryName);
        Map stackIdToOreId = (Map)ReflectionHelper.getPrivateValue(OreDictionary.class, null, (String[])new String[]{"stackToId"});
        stackIdToOreId.remove(stackId);
    }
}

