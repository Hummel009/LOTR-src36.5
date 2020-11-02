package lotr.common.block;

import java.util.Random;

import net.minecraft.block.Block;

public class LOTRBlockMallornTorch extends LOTRBlockTorch {
	private int torchColor;

	public LOTRBlockMallornTorch(int color) {
		setHardness(0.0f);
		setStepSound(Block.soundTypeWood);
		setLightLevel(0.875f);
		torchColor = color;
	}

	@Override
	public LOTRBlockTorch.TorchParticle createTorchParticle(Random random) {
		if (random.nextInt(3) == 0) {
			return new LOTRBlockTorch.TorchParticle("elvenGlow_" + Integer.toHexString(torchColor), 0.0, -0.1, 0.0, 0.0, 0.0, 0.0);
		}
		return null;
	}
}
