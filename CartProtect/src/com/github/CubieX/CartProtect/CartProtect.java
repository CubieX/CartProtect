package com.github.CubieX.CartProtect;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CartProtect extends JavaPlugin
{
   public static final Logger log = Bukkit.getServer().getLogger();
   static final String logPrefix = "[CartProtect] "; // Prefix to go in front of all log entries

   private CartProtect plugin = null;
   private CPCommandHandler comHandler = null;
   private CPConfigHandler configHandler = null;
   private CPEntityListener eListener = null;
   private WorldGuardPlugin wgInst = null;

   static boolean debug = false;

   //*************************************************
   static String usedConfigVersion = "1"; // Update this every time the config file version changes, so the plugin knows, if there is a suiting config present
   //*************************************************

   @Override
   public void onEnable()
   {
      this.plugin = this;
      configHandler = new CPConfigHandler(this);

      if(!checkConfigFileVersion())
      {
         log.severe(logPrefix + "Outdated or corrupted config file(s). Please delete your config files."); 
         log.severe(logPrefix + "will generate a new config for you.");
         log.severe(logPrefix + "will be disabled now. Config file is outdated or corrupted.");
         getServer().getPluginManager().disablePlugin(this);
         return;
      }

      if(!getWorldGuard())
      {
         log.severe(logPrefix + " will be disabled now. Error on getting WorldEdit instance.");
         getServer().getPluginManager().disablePlugin(this);
      }

      eListener = new CPEntityListener(this, wgInst);
      comHandler = new CPCommandHandler(this, configHandler);
      getCommand("cprotect").setExecutor(comHandler);

      readConfigValues();

      log.info(this.getName() + " version " + getDescription().getVersion() + " is enabled!");
   }

   public void readConfigValues()
   {
      boolean exceed = false;
      boolean invalid = false;

      debug = configHandler.getConfig().getBoolean("debug");            

      if(exceed)
      {
         log.warning("One or more config values are exceeding their allowed range. Please check your config file!");
      }

      if(invalid)
      {
         log.warning("One or more config values are invalid. Please check your config file!");
      }
   }

   private boolean checkConfigFileVersion()
   {      
      boolean configOK = false;

      if(configHandler.getConfig().isSet("config_version"))
      {
         String configVersion = configHandler.getConfig().getString("config_version");

         if(configVersion.equals(usedConfigVersion))
         {
            configOK = true;
         }
      }

      return (configOK);
   }

   private boolean getWorldGuard()
   {
      boolean ok = false;

      Plugin wgPlugin = getServer().getPluginManager().getPlugin("WorldGuard");

      // WorldGuard may not be loaded
      if ((null != wgPlugin) && (wgPlugin instanceof WorldGuardPlugin))
      {        
         this.wgInst = (WorldGuardPlugin) wgPlugin;
         ok = true;
      }

      return ok;
   }

   @Override
   public void onDisable()
   {
      comHandler = null;
      eListener = null;
      configHandler = null;
      log.info(this.getDescription().getName() + " version " + getDescription().getVersion() + " is disabled!");
   }
}


