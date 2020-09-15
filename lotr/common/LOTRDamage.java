/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.util.DamageSource
 */
package lotr.common;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import lotr.common.network.LOTRPacketEnvironmentOverlay;
import lotr.common.network.LOTRPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public class LOTRDamage {
    public static DamageSource frost = new DamageSource("lotr.frost").setDamageBypassesArmor();
    public static DamageSource poisonDrink = new DamageSource("lotr.poisonDrink").setDamageBypassesArmor().setMagicDamage();
    public static DamageSource plantHurt = new DamageSource("lotr.plantHurt").setDamageBypassesArmor();

    public static void doFrostDamage(EntityPlayerMP entityplayer) {
        LOTRPacketEnvironmentOverlay packet = new LOTRPacketEnvironmentOverlay(LOTRPacketEnvironmentOverlay.Overlay.FROST);
        LOTRPacketHandler.networkWrapper.sendTo((IMessage)packet, entityplayer);
    }

    public static void doBurnDamage(EntityPlayerMP entityplayer) {
        LOTRPacketEnvironmentOverlay packet = new LOTRPacketEnvironmentOverlay(LOTRPacketEnvironmentOverlay.Overlay.BURN);
        LOTRPacketHandler.networkWrapper.sendTo((IMessage)packet, entityplayer);
    }
}

