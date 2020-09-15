/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.Block$SoundType
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemMechanism
extends Item {
    public LOTRItemMechanism() {
        this.setCreativeTab((CreativeTabs)LOTRCreativeTabs.tabMisc);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2) {
        int meta;
        Block block = world.getBlock(i, j, k);
        if (block == Blocks.rail && (meta = world.getBlockMetadata(i, j, k)) >= 0 && meta <= 5) {
            Block setBlock = LOTRMod.mechanisedRailOn;
            world.setBlock(i, j, k, setBlock, meta | 8, 3);
            Block.SoundType sound = setBlock.stepSound;
            world.playSoundEffect((double)((float)i + 0.5f), (double)((float)j + 0.5f), (double)((float)k + 0.5f), sound.func_150496_b(), (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
            --itemstack.stackSize;
            return true;
        }
        return false;
    }
}

