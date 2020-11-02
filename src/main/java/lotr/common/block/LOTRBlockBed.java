package lotr.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class LOTRBlockBed extends BlockBed {
	public Item bedItem;
	private Block bedBottomBlock;
	private int bedBottomMetadata;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] bedIconsEnd;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] bedIconsSide;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] bedIconsTop;

	public LOTRBlockBed(Block block, int k) {
		bedBottomBlock = block;
		bedBottomMetadata = k;
		setHardness(0.2f);
		setStepSound(Block.soundTypeWood);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		int i1;
		if (i == 0) {
			return bedBottomBlock.getIcon(0, bedBottomMetadata);
		}
		int k = BlockDirectional.getDirection(j);
		int l = Direction.bedDirection[k][i];
		i1 = BlockBed.isBlockHeadOfBed(j) ? 1 : 0;
		return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? l != 5 && l != 4 ? bedIconsTop[i1] : bedIconsSide[i1] : bedIconsEnd[i1];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		bedIconsTop = new IIcon[] { iconregister.registerIcon(getTextureName() + "_feet_top"), iconregister.registerIcon(getTextureName() + "_head_top") };
		bedIconsEnd = new IIcon[] { iconregister.registerIcon(getTextureName() + "_feet_end"), iconregister.registerIcon(getTextureName() + "_head_end") };
		bedIconsSide = new IIcon[] { iconregister.registerIcon(getTextureName() + "_feet_side"), iconregister.registerIcon(getTextureName() + "_head_side") };
	}

	@Override
	public Item getItemDropped(int i, Random random, int j) {
		return BlockBed.isBlockHeadOfBed(i) ? null : bedItem;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public Item getItem(World world, int i, int j, int k) {
		return bedItem;
	}

	@Override
	public boolean isBed(IBlockAccess world, int i, int j, int k, EntityLivingBase entity) {
		return true;
	}
}
