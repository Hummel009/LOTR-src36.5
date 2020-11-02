package lotr.client.render.entity;

import lotr.client.model.LOTRModelHuman;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderBlacksmith extends LOTRRenderBiped {
	private LOTRRandomSkins skins;
	private ResourceLocation apron;
	private ModelBiped standardRenderPassModel = new LOTRModelHuman(0.5f, false);

	public LOTRRenderBlacksmith(String s, String s1) {
		super(new LOTRModelHuman(), 0.5f);
		skins = LOTRRandomSkins.loadSkinsList("lotr:mob/" + s);
		apron = new ResourceLocation("lotr:mob/" + s1 + ".png");
		setRenderPassModel(standardRenderPassModel);
	}

	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return skins.getRandomSkin((LOTREntityNPC) entity);
	}

	@Override
	public int shouldRenderPass(EntityLiving entity, int pass, float f) {
		if (pass == 1) {
			setRenderPassModel(standardRenderPassModel);
			bindTexture(apron);
			return 1;
		}
		return super.shouldRenderPass(entity, pass, f);
	}
}
