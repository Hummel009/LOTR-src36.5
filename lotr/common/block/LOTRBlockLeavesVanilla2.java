package lotr.common.block;

import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.*;

public class LOTRBlockLeavesVanilla2 extends LOTRBlockLeavesBase {
    public LOTRBlockLeavesVanilla2() {
        super(true, "lotr:leavesV2");
        this.setLeafNames("acacia", "darkOak");
        this.setSeasonal(false, true);
    }

    @Override
    public String[] func_150125_e() {
        return BlockNewLeaf.field_150133_O;
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public int getRenderColor(int i) {
        int meta = i & 3;
        if(meta == 0 || meta == 1) {
            return ColorizerFoliage.getFoliageColorBasic();
        }
        return super.getRenderColor(i);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess world, int i, int j, int k) {
        int meta = world.getBlockMetadata(i, j, k) & 3;
        if(meta == 0 || meta == 1) {
            return LOTRBlockLeavesBase.getBiomeLeafColor(world, i, j, k);
        }
        return super.colorMultiplier(world, i, j, k);
    }

    @Override
    public Item getItemDropped(int i, Random random, int j) {
        return Item.getItemFromBlock(Blocks.sapling);
    }

    @Override
    public int damageDropped(int i) {
        return super.damageDropped(i) + 4;
    }

    @Override
    protected int getSaplingChance(int meta) {
        if(meta == 1) {
            return 12;
        }
        return super.getSaplingChance(meta);
    }

    @Override
    public int getDamageValue(World world, int i, int j, int k) {
        return super.damageDropped(world.getBlockMetadata(i, j, k));
    }
}
