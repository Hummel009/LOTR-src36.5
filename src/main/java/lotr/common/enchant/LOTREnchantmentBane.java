package lotr.common.enchant;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

public class LOTREnchantmentBane extends LOTREnchantmentDamage {
	private List<Class<? extends EntityLivingBase>> entityClasses;
	private EnumCreatureAttribute entityAttribute;
	public final float baneDamage;
	public boolean isAchievable = true;

	private LOTREnchantmentBane(String s, float boost) {
		super(s, 0.0f);
		baneDamage = boost;
		setValueModifier((10.0f + baneDamage) / 10.0f);
		setPersistsReforge();
		setBypassAnvilLimit();
	}

	public LOTREnchantmentBane(String s, float boost, Class<? extends EntityLivingBase>... classes) {
		this(s, boost);
		entityClasses = Arrays.asList(classes);
	}

	public LOTREnchantmentBane(String s, float boost, EnumCreatureAttribute attr) {
		this(s, boost);
		entityAttribute = attr;
	}

	public LOTREnchantmentBane setUnachievable() {
		isAchievable = false;
		return this;
	}

	public boolean isEntityType(EntityLivingBase entity) {
		if (entityClasses != null) {
			for (Class<? extends EntityLivingBase> cls : entityClasses) {
				if (!cls.isAssignableFrom(entity.getClass())) {
					continue;
				}
				return true;
			}
		} else if (entityAttribute != null) {
			return entity.getCreatureAttribute() == entityAttribute;
		}
		return false;
	}

	@Override
	public float getBaseDamageBoost() {
		return 0.0f;
	}

	@Override
	public float getEntitySpecificDamage(EntityLivingBase entity) {
		if (isEntityType(entity)) {
			return baneDamage;
		}
		return 0.0f;
	}

	public int getRandomKillsRequired(Random random) {
		return MathHelper.getRandomIntegerInRange(random, 100, 250);
	}

	@Override
	public String getDescription(ItemStack itemstack) {
		return StatCollector.translateToLocalFormatted("lotr.enchant." + enchantName + ".desc", formatAdditive(baneDamage));
	}

	@Override
	public boolean isBeneficial() {
		return true;
	}
}
