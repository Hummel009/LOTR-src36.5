/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.Block$SoundType
 *  net.minecraft.block.BlockTrapDoor
 *  net.minecraft.block.material.Material
 *  net.minecraft.creativetab.CreativeTabs
 */
package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class LOTRBlockTrapdoor
extends BlockTrapDoor {
    public LOTRBlockTrapdoor() {
        this(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(3.0f);
    }

    public LOTRBlockTrapdoor(Material material) {
        super(material);
        this.setCreativeTab((CreativeTabs)LOTRCreativeTabs.tabUtil);
    }
}

