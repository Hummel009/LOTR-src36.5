package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import lotr.common.fac.LOTRFaction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class LOTRBlockHalfTrollTable extends LOTRBlockCraftingTable {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] tableIcons;

	public LOTRBlockHalfTrollTable() {
		super(Material.rock, LOTRFaction.HALF_TROLL, 34);
		setStepSound(Block.soundTypeStone);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (i == 1) {
			return tableIcons[1];
		}
		if (i == 0) {
			return Blocks.cobblestone.getIcon(i, 0);
		}
		return tableIcons[0];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		tableIcons = new IIcon[2];
		tableIcons[0] = iconregister.registerIcon(getTextureName() + "_side");
		tableIcons[1] = iconregister.registerIcon(getTextureName() + "_top");
	}
}
