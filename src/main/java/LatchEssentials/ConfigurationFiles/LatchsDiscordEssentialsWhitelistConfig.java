package LatchEssentials.ConfigurationFiles;

import LatchEssentials.Constants;
import LatchEssentials.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class LatchsDiscordEssentialsWhitelistConfig {
    public static final Main plugin = getPlugin(Main.class);
    // Set up config.yml configuration file
    public void setup(){
        FileConfiguration whitelistCfg;
        File whitelistConfigFile;
        // If the plugin folder does not exist, create the plugin folder
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        whitelistConfigFile = new File(plugin.getDataFolder(),  Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME + ".yml");
        whitelistCfg = YamlConfiguration.loadConfiguration(whitelistConfigFile);
        //if the config.yml does not exist, create it
        if(!whitelistConfigFile.exists()){
            try {
                whitelistCfg.save(whitelistConfigFile);
            }
            catch(IOException e){
                System.out.println(ChatColor.RED + "Could not create the " + Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME + ".yml file");
            }
        }
    }
}

