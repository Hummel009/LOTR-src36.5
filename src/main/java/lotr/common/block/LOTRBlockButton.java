package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.BlockButton;
import net.minecraft.client.renderer.texture.IIconRegister;

public class LOTRBlockButton extends BlockButton {
	private String iconPath;

	public LOTRBlockButton(boolean flag, String s) {
		super(flag);
		iconPath = s;
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		blockIcon = iconregister.registerIcon(iconPath);
	}
}
