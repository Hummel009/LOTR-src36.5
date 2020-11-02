package lotr.client.render.entity;

import lotr.client.model.LOTRModelElf;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderDorwinionElfVintner extends LOTRRenderElf {
	private ResourceLocation outfitTexture;
	private ResourceLocation capeTexture;
	private ModelBiped outfitModel = new LOTRModelElf(0.5f);

	public LOTRRenderDorwinionElfVintner() {
		outfitTexture = new ResourceLocation("lotr:mob/elf/dorwinionVintner_cloak.png");
		capeTexture = new ResourceLocation("lotr:mob/elf/dorwinionVintner_cape.png");
		setRenderPassModel(outfitModel);
	}

	@Override
	public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1) {
		((LOTREntityNPC) entity).npcCape = capeTexture;
		super.doRender(entity, d, d1, d2, f, f1);
	}

	@Override
	public int shouldRenderPass(EntityLiving entity, int pass, float f) {
		if (pass == 0) {
			setRenderPassModel(outfitModel);
			bindTexture(outfitTexture);
			return 1;
		}
		return super.shouldRenderPass(entity, pass, f);
	}
}
