package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRBlockWoodBars extends LOTRBlockPane {
	public LOTRBlockWoodBars() {
		super("", "", Material.wood, true);
		setHardness(2.0f);
		setResistance(5.0f);
		setStepSound(Block.soundTypeWood);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		blockIcon = iconregister.registerIcon(getTextureName());
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon func_150097_e() {
		return blockIcon;
	}
}
