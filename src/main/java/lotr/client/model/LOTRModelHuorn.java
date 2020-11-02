package lotr.client.model;

import java.util.*;

import org.lwjgl.opengl.GL11;

import lotr.client.render.entity.LOTRHuornTextures;
import lotr.common.entity.npc.LOTREntityHuornBase;
import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;

public class LOTRModelHuorn extends ModelBase {
	private List woodBlocks = new ArrayList();
	private List leafBlocks = new ArrayList();
	private ModelRenderer face;
	private int baseX = 2;
	private int baseY = 0;
	private int baseZ = 2;
	private Random rand = new Random();

	public LOTRModelHuorn() {
		int j;
		rand.setSeed(100L);
		int height = 6;
		int leafStart = 3;
		int leafRangeMin = 0;
		for (j = baseY - leafStart + height; j <= baseY + height; ++j) {
			int j1 = j - (baseY + height);
			int leafRange = leafRangeMin + 1 - j1 / 2;
			for (int i = baseX - leafRange; i <= baseX + leafRange; ++i) {
				int i1 = i - baseX;
				for (int k = baseZ - leafRange; k <= baseZ + leafRange; ++k) {
					int k1 = k - baseZ;
					if (Math.abs(i1) == leafRange && Math.abs(k1) == leafRange && (rand.nextInt(2) == 0 || j1 == 0)) {
						continue;
					}
					ModelRenderer block = new ModelRenderer(this, 0, 0);
					block.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
					block.setRotationPoint(i * 16.0f, 16.0f - j * 16.0f, k * 16.0f);
					leafBlocks.add(block);
				}
			}
		}
		for (j = 0; j < height; ++j) {
			ModelRenderer block = new ModelRenderer(this, 0, 0);
			block.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
			block.setRotationPoint(baseX * 16.0f, 16.0f - j * 16.0f, baseZ * 16.0f);
			woodBlocks.add(block);
		}
		face = new ModelRenderer(this, 0, 0);
		face.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16, 0.5f);
		face.setRotationPoint(0.0f, 0.0f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		ModelRenderer block;
		int i;
		LOTREntityHuornBase huorn = (LOTREntityHuornBase) entity;
		if (huorn.isHuornActive()) {
			face.render(f5);
		}
		GL11.glPushMatrix();
		GL11.glEnable(2884);
		GL11.glTranslatef(-((float) baseX), -((float) baseY), -((float) baseZ));
		for (i = 0; i < woodBlocks.size(); ++i) {
			if (i == 0) {
				LOTRHuornTextures.INSTANCE.bindWoodTexture(huorn);
			}
			block = (ModelRenderer) woodBlocks.get(i);
			block.render(f5);
		}
		for (i = 0; i < leafBlocks.size(); ++i) {
			if (i == 0) {
				LOTRHuornTextures.INSTANCE.bindLeafTexture(huorn);
			}
			block = (ModelRenderer) leafBlocks.get(i);
			block.render(f5);
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glDisable(2884);
		GL11.glPopMatrix();
	}
}
