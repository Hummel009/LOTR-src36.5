package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRBlockBars extends LOTRBlockPane {
	public LOTRBlockBars() {
		super("", "", Material.iron, true);
		setHardness(5.0f);
		setResistance(10.0f);
		setStepSound(Block.soundTypeMetal);
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
