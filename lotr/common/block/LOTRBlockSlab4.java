package lotr.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRBlockSlab4 extends LOTRBlockSlabBase {
    public LOTRBlockSlab4(boolean flag) {
        super(flag, Material.rock, 8);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public IIcon getIcon(int i, int j) {
        if((j &= 7) == 0) {
            return LOTRMod.brick.getIcon(i, 15);
        }
        if(j == 1) {
            return LOTRMod.brick2.getIcon(i, 3);
        }
        if(j == 2) {
            return LOTRMod.brick2.getIcon(i, 4);
        }
        if(j == 3) {
            return LOTRMod.brick2.getIcon(i, 5);
        }
        if(j == 4) {
            return LOTRMod.brick2.getIcon(i, 7);
        }
        if(j == 5) {
            return LOTRMod.brick2.getIcon(i, 8);
        }
        if(j == 6) {
            return LOTRMod.brick2.getIcon(i, 9);
        }
        if(j == 7) {
            return LOTRMod.pillar.getIcon(i, 5);
        }
        return super.getIcon(i, j);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconregister) {
    }
}
