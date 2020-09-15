/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.CommandBase
 *  net.minecraft.command.ICommand
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.PlayerNotFoundException
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.server.MinecraftServer
 */
package lotr.common.command;

import java.util.List;

import lotr.common.LOTRLevelData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class LOTRCommandPledgeCooldown
extends CommandBase {
    public String getCommandName() {
        return "pledgeCooldown";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.lotr.pledgeCooldown.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length >= 1) {
            EntityPlayerMP entityplayer;
            int cd = LOTRCommandPledgeCooldown.parseIntBounded((ICommandSender)sender, (String)args[0], (int)0, (int)10000000);
            if (args.length >= 2) {
                entityplayer = LOTRCommandPledgeCooldown.getPlayer((ICommandSender)sender, (String)args[1]);
            } else {
                entityplayer = LOTRCommandPledgeCooldown.getCommandSenderAsPlayer((ICommandSender)sender);
                if (entityplayer == null) {
                    throw new PlayerNotFoundException();
                }
            }
            LOTRLevelData.getData((EntityPlayer)entityplayer).setPledgeBreakCooldown(cd);
            LOTRCommandPledgeCooldown.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"commands.lotr.pledgeCooldown.set", (Object[])new Object[]{entityplayer.getCommandSenderName(), cd, LOTRLevelData.getHMSTime_Ticks(cd)});
            return;
        }
        throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2) {
            return LOTRCommandPledgeCooldown.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }

    public boolean isUsernameIndex(String[] args, int i) {
        return i == 1;
    }
}

