package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.fac.LOTRFaction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityMordorWarg extends LOTREntityWarg {
	public LOTREntityMordorWarg(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		setWargType(LOTREntityWarg.WargType.BLACK);
	}

	@Override
	public LOTREntityNPC createWargRider() {
		if (rand.nextBoolean()) {
			setWargArmor(new ItemStack(LOTRMod.wargArmorMordor));
		}
		return worldObj.rand.nextBoolean() ? new LOTREntityMordorOrcArcher(worldObj) : new LOTREntityMordorOrc(worldObj);
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.MORDOR;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}
}
