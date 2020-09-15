package lotr.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class LOTRInterModComms {
  public static void update() {
    ImmutableList<FMLInterModComms.IMCMessage> messages = FMLInterModComms.fetchRuntimeMessages(LOTRMod.instance);
    if (!messages.isEmpty())
      for (UnmodifiableIterator<FMLInterModComms.IMCMessage> unmodifiableIterator = messages.iterator(); unmodifiableIterator.hasNext(); ) {
        FMLInterModComms.IMCMessage message = unmodifiableIterator.next();
        if (message.key.equals("SIEGE_ACTIVE")) {
          String playerName = message.getStringValue();
          EntityPlayerMP entityPlayerMP = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
          if (entityPlayerMP != null) {
            int duration = 20;
            LOTRLevelData.getData((EntityPlayer)entityPlayerMP).setSiegeActive(duration);
          } 
        } 
      }  
  }
}
