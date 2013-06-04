package com.github.CubieX.CartProtect;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CPEntityListener implements Listener
{   
   private CartProtect plugin = null;
   private WorldGuardPlugin wgInst = null;
   private String Message_denyAccess = ChatColor.RED + "Du hast auf diesem Gebiet keine Besitz- oder Baurechte.";

   public CPEntityListener(CartProtect plugin, WorldGuardPlugin wgInst)
   {
      this.plugin = plugin;
      this.wgInst = wgInst;

      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }  

   //----------------------------------------------------------------------------------------------------
   @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
   public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
   {
      if (event.getRightClicked() instanceof StorageMinecart)
      {
         StorageMinecart storageMC = (StorageMinecart) event.getRightClicked();

         if(!playerHasAccessPermissionStorageMinecart(event.getPlayer(), storageMC))
         {
            event.getPlayer().sendMessage(Message_denyAccess);
            event.setCancelled(true);
         }
      }
      else if (event.getRightClicked() instanceof HopperMinecart)
      {
         HopperMinecart hopperMC = (HopperMinecart) event.getRightClicked();

         if(!playerHasAccessPermissionHopperMinecart(event.getPlayer(), hopperMC))
         {
            event.getPlayer().sendMessage(Message_denyAccess);
            event.setCancelled(true);
         }
      }
   }

   private boolean playerHasAccessPermissionStorageMinecart(Player player, StorageMinecart storageMC)
   {
      boolean res = false;

      if(player.isOp() ||
            player.hasPermission("cartprotect.override") ||
            wgInst.canBuild(player, storageMC.getLocation()))
      {
         res = true;
      }

      return res;
   }

   private boolean playerHasAccessPermissionHopperMinecart(Player player, HopperMinecart hopperMC)
   {
      boolean res = false;

      if(player.isOp() ||
            player.hasPermission("cartprotect.override") ||
            wgInst.canBuild(player, hopperMC.getLocation()))
      {
         res = true;
      }

      return res;
   }
}
