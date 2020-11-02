package lotr.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.*;

public class LOTRBlockUtumnoReturnLight extends Block {
	public LOTRBlockUtumnoReturnLight() {
		super(Material.circuits);
		setLightLevel(1.0f);
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, int i, int j, int k) {
		return true;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
	}

	@Override
	public Item getItemDropped(int i, Random random, int j) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	@Override
	public boolean isCollidable() {
		return false;
	}
}
