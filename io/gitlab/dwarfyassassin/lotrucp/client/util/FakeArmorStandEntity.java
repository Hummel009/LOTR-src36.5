/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.client.FMLClientHandler
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package io.gitlab.dwarfyassassin.lotrucp.client.util;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FakeArmorStandEntity
extends EntityLivingBase {
    public static final FakeArmorStandEntity INSTANCE = new FakeArmorStandEntity();

    public FakeArmorStandEntity() {
        super((World)FMLClientHandler.instance().getWorldClient());
    }

    public ItemStack getHeldItem() {
        return null;
    }

    public ItemStack getEquipmentInSlot(int p_71124_1_) {
        return null;
    }

    public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
    }

    public ItemStack[] getLastActiveItems() {
        return null;
    }
}

