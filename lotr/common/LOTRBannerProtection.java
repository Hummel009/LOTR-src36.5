/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftSessionService
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityMinecartTNT
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.entity.projectile.EntityThrowable
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.Container
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.StatCollector
 *  net.minecraft.world.World
 *  net.minecraftforge.common.util.FakePlayer
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.tuple.Pair
 */
package lotr.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.mojang.authlib.GameProfile;

import io.gitlab.dwarfyassassin.lotrucp.core.hooks.ThaumcraftHooks;
import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.fac.LOTRFaction;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class LOTRBannerProtection {
    public static final int MAX_RANGE = 64;
    private static Map<Pair, Integer> protectionBlocks = new HashMap<Pair, Integer>();
    private static Map<UUID, Integer> lastWarningTimes;

    public static int getProtectionRange(Block block, int meta) {
        Integer i = protectionBlocks.get((Object)Pair.of((Object)block, (Object)meta));
        if (i == null) {
            return 0;
        }
        return i;
    }

    public static boolean isProtected(World world, Entity entity, IFilter protectFilter, boolean sendMessage) {
        int i = MathHelper.floor_double((double)entity.posX);
        int j = MathHelper.floor_double((double)entity.boundingBox.minY);
        int k = MathHelper.floor_double((double)entity.posZ);
        return LOTRBannerProtection.isProtected(world, i, j, k, protectFilter, sendMessage);
    }

    public static boolean isProtected(World world, int i, int j, int k, IFilter protectFilter, boolean sendMessage) {
        return LOTRBannerProtection.isProtected(world, i, j, k, protectFilter, sendMessage, 0.0);
    }

    public static boolean isProtected(World world, int i, int j, int k, IFilter protectFilter, boolean sendMessage, double searchExtra) {
        if (!LOTRConfig.allowBannerProtection) {
            return false;
        }
        String protectorName = null;
        AxisAlignedBB originCube = AxisAlignedBB.getBoundingBox((double)i, (double)j, (double)k, (double)(i + 1), (double)(j + 1), (double)(k + 1)).expand(searchExtra, searchExtra, searchExtra);
        AxisAlignedBB searchCube = originCube.expand(64.0, 64.0, 64.0);
        List banners = world.getEntitiesWithinAABB(LOTREntityBanner.class, searchCube);
        if (!banners.isEmpty()) {
            for (int l = 0; l < banners.size(); ++l) {
                ProtectType result;
                LOTREntityBanner banner = (LOTREntityBanner)((Object)banners.get(l));
                AxisAlignedBB protectionCube = banner.createProtectionCube();
                if (!banner.isProtectingTerritory() || !protectionCube.intersectsWith(searchCube) || !protectionCube.intersectsWith(originCube) || (result = protectFilter.protects(banner)) == ProtectType.NONE) continue;
                if (result == ProtectType.FACTION) {
                    protectorName = banner.getBannerType().faction.factionName();
                    break;
                }
                if (result == ProtectType.PLAYER_SPECIFIC) {
                    GameProfile placingPlayer = banner.getPlacingPlayer();
                    if (placingPlayer != null) {
                        if (StringUtils.isBlank((CharSequence)placingPlayer.getName())) {
                            MinecraftServer.getServer().func_147130_as().fillProfileProperties(placingPlayer, true);
                        }
                        protectorName = placingPlayer.getName();
                        break;
                    }
                    protectorName = "?";
                    break;
                }
                if (result != ProtectType.STRUCTURE) continue;
                protectorName = StatCollector.translateToLocal((String)"chat.lotr.protectedStructure");
                break;
            }
        }
        if (protectorName != null) {
            if (sendMessage) {
                protectFilter.warnProtection((IChatComponent)new ChatComponentTranslation("chat.lotr.protectedLand", new Object[]{protectorName}));
            }
            return true;
        }
        return false;
    }

    public static IFilter anyBanner() {
        return new IFilter(){

            @Override
            public ProtectType protects(LOTREntityBanner banner) {
                if (banner.isStructureProtection()) {
                    return ProtectType.STRUCTURE;
                }
                return ProtectType.FACTION;
            }

            @Override
            public void warnProtection(IChatComponent message) {
            }
        };
    }

    public static IFilter forPlayer(EntityPlayer entityplayer) {
        return LOTRBannerProtection.forPlayer(entityplayer, Permission.FULL);
    }

    public static IFilter forPlayer(EntityPlayer entityplayer, Permission perm) {
        return new FilterForPlayer(entityplayer, perm);
    }

    public static IFilter forPlayer_returnMessage(final EntityPlayer entityplayer, final Permission perm, final IChatComponent[] protectionMessage) {
        return new IFilter(){
            private IFilter internalPlayerFilter;
            {
                this.internalPlayerFilter = LOTRBannerProtection.forPlayer(entityplayer, perm);
            }

            @Override
            public ProtectType protects(LOTREntityBanner banner) {
                return this.internalPlayerFilter.protects(banner);
            }

            @Override
            public void warnProtection(IChatComponent message) {
                this.internalPlayerFilter.warnProtection(message);
                protectionMessage[0] = message;
            }
        };
    }

    public static IFilter forNPC(final EntityLiving entity) {
        return new IFilter(){

            @Override
            public ProtectType protects(LOTREntityBanner banner) {
                if (banner.isStructureProtection()) {
                    return ProtectType.STRUCTURE;
                }
                if (banner.getBannerType().faction.isBadRelation(LOTRMod.getNPCFaction((Entity)entity))) {
                    return ProtectType.FACTION;
                }
                return ProtectType.NONE;
            }

            @Override
            public void warnProtection(IChatComponent message) {
            }
        };
    }

    public static IFilter forInvasionSpawner(LOTREntityInvasionSpawner spawner) {
        return LOTRBannerProtection.forFaction(spawner.getInvasionType().invasionFaction);
    }

    public static IFilter forFaction(final LOTRFaction theFaction) {
        return new IFilter(){

            @Override
            public ProtectType protects(LOTREntityBanner banner) {
                if (banner.isStructureProtection()) {
                    return ProtectType.STRUCTURE;
                }
                if (banner.getBannerType().faction.isBadRelation(theFaction)) {
                    return ProtectType.FACTION;
                }
                return ProtectType.NONE;
            }

            @Override
            public void warnProtection(IChatComponent message) {
            }
        };
    }

    public static IFilter forTNT(final EntityTNTPrimed bomb) {
        return new IFilter(){

            @Override
            public ProtectType protects(LOTREntityBanner banner) {
                if (banner.isStructureProtection()) {
                    return ProtectType.STRUCTURE;
                }
                EntityLivingBase bomber = bomb.getTntPlacedBy();
                if (bomber == null) {
                    return ProtectType.FACTION;
                }
                if (bomber instanceof EntityPlayer) {
                    return LOTRBannerProtection.forPlayer((EntityPlayer)bomber, Permission.FULL).protects(banner);
                }
                if (bomber instanceof EntityLiving) {
                    return LOTRBannerProtection.forNPC((EntityLiving)bomber).protects(banner);
                }
                return ProtectType.NONE;
            }

            @Override
            public void warnProtection(IChatComponent message) {
            }
        };
    }

    public static IFilter forTNTMinecart(EntityMinecartTNT minecart) {
        return new IFilter(){

            @Override
            public ProtectType protects(LOTREntityBanner banner) {
                if (banner.isStructureProtection()) {
                    return ProtectType.STRUCTURE;
                }
                return ProtectType.FACTION;
            }

            @Override
            public void warnProtection(IChatComponent message) {
            }
        };
    }

    public static IFilter forThrown(final EntityThrowable throwable) {
        return new IFilter(){

            @Override
            public ProtectType protects(LOTREntityBanner banner) {
                if (banner.isStructureProtection()) {
                    return ProtectType.STRUCTURE;
                }
                EntityLivingBase thrower = throwable.getThrower();
                if (thrower == null) {
                    return ProtectType.FACTION;
                }
                if (thrower instanceof EntityPlayer) {
                    return LOTRBannerProtection.forPlayer((EntityPlayer)thrower, Permission.FULL).protects(banner);
                }
                if (thrower instanceof EntityLiving) {
                    return LOTRBannerProtection.forNPC((EntityLiving)thrower).protects(banner);
                }
                return ProtectType.NONE;
            }

            @Override
            public void warnProtection(IChatComponent message) {
            }
        };
    }

    private static void setWarningCooldown(EntityPlayer entityplayer) {
        lastWarningTimes.put(entityplayer.getUniqueID(), LOTRConfig.bannerWarningCooldown);
    }

    private static boolean hasWarningCooldown(EntityPlayer entityplayer) {
        return lastWarningTimes.containsKey(entityplayer.getUniqueID());
    }

    public static void updateWarningCooldowns() {
        HashSet<UUID> removes = new HashSet<UUID>();
        for (Map.Entry<UUID, Integer> e : lastWarningTimes.entrySet()) {
            UUID player = e.getKey();
            int time = e.getValue();
            e.setValue(--time);
            if (time > 0) continue;
            removes.add(player);
        }
        for (UUID player : removes) {
            lastWarningTimes.remove(player);
        }
    }

    static {
        Pair BRONZE = Pair.of((Object)LOTRMod.blockOreStorage, (Object)2);
        Pair SILVER = Pair.of((Object)LOTRMod.blockOreStorage, (Object)3);
        Pair GOLD = Pair.of((Object)Blocks.gold_block, (Object)0);
        protectionBlocks.put(BRONZE, 8);
        protectionBlocks.put(SILVER, 16);
        protectionBlocks.put(GOLD, 32);
        lastWarningTimes = new HashMap<UUID, Integer>();
    }

    public static class FilterForPlayer
    implements IFilter {
        private EntityPlayer thePlayer;
        private Permission thePerm;

        public FilterForPlayer(EntityPlayer p, Permission perm) {
            this.thePlayer = p;
            this.thePerm = perm;
        }

        @Override
        public ProtectType protects(LOTREntityBanner banner) {
            ProtectType hook;
            if (this.thePlayer instanceof FakePlayer && (hook = ThaumcraftHooks.thaumcraftGolemBannerProtection(this.thePlayer, banner)) != null) {
                return hook;
            }
            if (this.thePlayer.capabilities.isCreativeMode) {
                return ProtectType.NONE;
            }
            if (banner.isStructureProtection()) {
                return ProtectType.STRUCTURE;
            }
            if (banner.isPlayerSpecificProtection()) {
                if (!banner.isPlayerWhitelisted(this.thePlayer, this.thePerm)) {
                    return ProtectType.PLAYER_SPECIFIC;
                }
                return ProtectType.NONE;
            }
            if (!banner.isPlayerAllowedByFaction(this.thePlayer, this.thePerm)) {
                return ProtectType.FACTION;
            }
            return ProtectType.NONE;
        }

        @Override
        public void warnProtection(IChatComponent message) {
            if (this.thePlayer instanceof FakePlayer) {
                return;
            }
            if (this.thePlayer instanceof EntityPlayerMP && !this.thePlayer.worldObj.isRemote) {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.thePlayer;
                entityplayermp.sendContainerToPlayer(this.thePlayer.inventoryContainer);
                if (!LOTRBannerProtection.hasWarningCooldown((EntityPlayer)entityplayermp)) {
                    entityplayermp.addChatMessage(message);
                    LOTRBannerProtection.setWarningCooldown((EntityPlayer)entityplayermp);
                }
            }
        }
    }

    public static interface IFilter {
        public ProtectType protects(LOTREntityBanner var1);

        public void warnProtection(IChatComponent var1);
    }

    public static enum Permission {
        FULL,
        DOORS,
        TABLES,
        CONTAINERS,
        FOOD,
        BEDS,
        SWITCHES;

        public final int bitFlag = 1 << this.ordinal();
        public final String codeName = this.name();

        public static Permission forName(String s) {
            for (Permission p : Permission.values()) {
                if (!p.codeName.equals(s)) continue;
                return p;
            }
            return null;
        }
    }

    public static enum ProtectType {
        NONE,
        FACTION,
        PLAYER_SPECIFIC,
        STRUCTURE;

    }

}

