package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRBlockReedBars extends LOTRBlockPane {
	public LOTRBlockReedBars() {
		super("", "", Material.grass, true);
		setHardness(0.5f);
		setStepSound(Block.soundTypeGrass);
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
