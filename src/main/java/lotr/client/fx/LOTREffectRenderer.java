package lotr.client.fx;

import java.util.*;

import org.lwjgl.opengl.GL11;

import lotr.client.LOTRClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LOTREffectRenderer {
	private Minecraft mc;
	private List particles = new ArrayList();

	public LOTREffectRenderer(Minecraft minecraft) {
		mc = minecraft;
	}

	public void addEffect(EntityFX entityfx) {
		if (particles.size() >= 4000) {
			particles.remove(0);
		}
		particles.add(entityfx);
	}

	public void updateEffects() {
		for (int i = 0; i < particles.size(); ++i) {
			EntityFX entityfx = (EntityFX) particles.get(i);
			if (entityfx != null) {
				entityfx.onUpdate();
			}
			if (entityfx != null && !entityfx.isDead) {
				continue;
			}
			particles.remove(i--);
		}
	}

	public void renderParticles(Entity entity, float f) {
		float f1 = ActiveRenderInfo.rotationX;
		float f2 = ActiveRenderInfo.rotationZ;
		float f3 = ActiveRenderInfo.rotationYZ;
		float f4 = ActiveRenderInfo.rotationXY;
		float f5 = ActiveRenderInfo.rotationXZ;
		EntityFX.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * f;
		EntityFX.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * f;
		EntityFX.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * f;
		if (!particles.isEmpty()) {
			mc.getTextureManager().bindTexture(LOTRClientProxy.particlesTexture);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glDepthMask(false);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glAlphaFunc(516, 0.003921569f);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			for (Object element : particles) {
				EntityFX entityfx = (EntityFX) element;
				if (entityfx == null) {
					continue;
				}
				tessellator.setBrightness(entityfx.getBrightnessForRender(f));
				entityfx.renderParticle(tessellator, f, f1, f5, f2, f3, f4);
			}
			tessellator.draw();
			GL11.glDisable(3042);
			GL11.glDepthMask(true);
			GL11.glAlphaFunc(516, 0.1f);
		}
	}

	public void clearEffectsAndSetWorld(World world) {
		particles.clear();
	}
}
