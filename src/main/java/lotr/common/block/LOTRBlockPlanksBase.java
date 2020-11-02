package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public abstract class LOTRBlockPlanksBase extends Block {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] plankIcons;
	private String[] plankTypes;

	public LOTRBlockPlanksBase() {
		super(Material.wood);
		setHardness(2.0f);
		setResistance(5.0f);
		setStepSound(Block.soundTypeWood);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}

	protected void setPlankTypes(String... types) {
		plankTypes = types;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= plankTypes.length) {
			j = 0;
		}
		return plankIcons[j];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		plankIcons = new IIcon[plankTypes.length];
		for (int i = 0; i < plankTypes.length; ++i) {
			plankIcons[i] = iconregister.registerIcon(getTextureName() + "_" + plankTypes[i]);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int j = 0; j < plankTypes.length; ++j) {
			list.add(new ItemStack(item, 1, j));
		}
	}
}
