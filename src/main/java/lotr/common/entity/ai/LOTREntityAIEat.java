package lotr.common.entity.ai;

import lotr.common.LOTRFoods;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.item.*;

public class LOTREntityAIEat extends LOTREntityAIConsumeBase {
	public LOTREntityAIEat(LOTREntityNPC entity, LOTRFoods foods, int chance) {
		super(entity, foods, chance);
	}

	@Override
	protected ItemStack createConsumable() {
		return foodPool.getRandomFood(rand);
	}

	@Override
	protected void updateConsumeTick(int tick) {
		if (tick % 4 == 0) {
			theEntity.spawnFoodParticles();
			theEntity.playSound("random.eat", 0.5f + 0.5f * rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);
		}
	}

	@Override
	protected void consume() {
		ItemStack itemstack = theEntity.getHeldItem();
		Item item = itemstack.getItem();
		if (item instanceof ItemFood) {
			ItemFood food = (ItemFood) item;
			theEntity.heal(food.func_150905_g(itemstack));
		}
	}
}
