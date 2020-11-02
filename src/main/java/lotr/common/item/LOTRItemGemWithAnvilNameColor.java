package lotr.common.item;

import net.minecraft.util.EnumChatFormatting;

public class LOTRItemGemWithAnvilNameColor extends LOTRItemGem implements AnvilNameColorProvider {
	private final EnumChatFormatting anvilNameColor;

	public LOTRItemGemWithAnvilNameColor(EnumChatFormatting color) {
		anvilNameColor = color;
	}

	@Override
	public EnumChatFormatting getAnvilNameColor() {
		return anvilNameColor;
	}
}
