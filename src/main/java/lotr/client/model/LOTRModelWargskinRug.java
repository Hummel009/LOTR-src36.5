package lotr.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class LOTRModelWargskinRug extends ModelBase {
	private LOTRModelWarg wargModel = new LOTRModelWarg();

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles();
		GL11.glTranslatef(0.0f, -0.3f, 0.0f);
		GL11.glPushMatrix();
		GL11.glScalef(1.5f, 0.4f, 1.0f);
		wargModel.body.render(f5);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0f, 0.0f, 0.0f);
		wargModel.tail.render(f5);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0f, -0.5f, 0.1f);
		wargModel.head.render(f5);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.3f, 0.0f, 0.0f);
		wargModel.leg1.render(f5);
		wargModel.leg3.render(f5);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(0.3f, 0.0f, 0.0f);
		wargModel.leg2.render(f5);
		wargModel.leg4.render(f5);
		GL11.glPopMatrix();
	}

	private void setRotationAngles() {
		wargModel.leg1.rotateAngleX = (float) Math.toRadians(30.0);
		wargModel.leg1.rotateAngleZ = (float) Math.toRadians(90.0);
		wargModel.leg2.rotateAngleX = (float) Math.toRadians(30.0);
		wargModel.leg2.rotateAngleZ = (float) Math.toRadians(-90.0);
		wargModel.leg3.rotateAngleX = (float) Math.toRadians(-20.0);
		wargModel.leg3.rotateAngleZ = (float) Math.toRadians(90.0);
		wargModel.leg4.rotateAngleX = (float) Math.toRadians(-20.0);
		wargModel.leg4.rotateAngleZ = (float) Math.toRadians(-90.0);
	}
}
