package lotr.common.block;

import java.util.*;

import cpw.mods.fml.relauncher.*;
import lotr.common.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class LOTRBlockFence extends BlockFence {
	private Block plankBlock;

	public LOTRBlockFence(Block planks) {
		super("", Material.wood);
		setHardness(2.0f);
		setResistance(5.0f);
		setStepSound(Block.soundTypeWood);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		plankBlock = planks;
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int i, int j, int k) {
		return true;
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public int getRenderType() {
		if (LOTRMod.proxy.isClient()) {
			return LOTRMod.proxy.getFenceRenderID();
		}
		return super.getRenderType();
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		return plankBlock.getIcon(i, j);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		ArrayList plankTypes = new ArrayList();
		plankBlock.getSubBlocks(Item.getItemFromBlock(plankBlock), plankBlock.getCreativeTabToDisplayOn(), plankTypes);
		for (Object obj : plankTypes) {
			if (!(obj instanceof ItemStack)) {
				continue;
			}
			int meta = ((ItemStack) obj).getItemDamage();
			list.add(new ItemStack(this, 1, meta));
		}
	}
}
