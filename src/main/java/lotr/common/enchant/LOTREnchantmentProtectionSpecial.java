package lotr.common.enchant;

import net.minecraft.util.DamageSource;

public abstract class LOTREnchantmentProtectionSpecial extends LOTREnchantment {
	public final int protectLevel;

	public LOTREnchantmentProtectionSpecial(String s, int level) {
		this(s, LOTREnchantmentType.ARMOR, level);
	}

	public LOTREnchantmentProtectionSpecial(String s, LOTREnchantmentType type, int level) {
		super(s, type);
		protectLevel = level;
		setValueModifier((2.0f + protectLevel) / 2.0f);
	}

	@Override
	public boolean isBeneficial() {
		return true;
	}

	@Override
	public boolean isCompatibleWith(LOTREnchantment other) {
		if (super.isCompatibleWith(other)) {
			if (other instanceof LOTREnchantmentProtectionSpecial) {
				return isCompatibleWithOtherProtection() || ((LOTREnchantmentProtectionSpecial) other).isCompatibleWithOtherProtection();
			}
			return true;
		}
		return false;
	}

	protected boolean isCompatibleWithOtherProtection() {
		return false;
	}

	protected abstract boolean protectsAgainst(DamageSource var1);

	public final int calcSpecialProtection(DamageSource source) {
		if (source.canHarmInCreative()) {
			return 0;
		}
		if (protectsAgainst(source)) {
			return calcIntProtection();
		}
		return 0;
	}

	protected abstract int calcIntProtection();
}
