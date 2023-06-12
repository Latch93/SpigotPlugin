package LatchEssentials.ConfigurationFiles;

import LatchEssentials.Constants;
import LatchEssentials.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class LatchsLuckPermsConfig {
    public static final Main plugin = getPlugin(Main.class);
    // Set up config.yml configuration file
    public void setup(){
        FileConfiguration pluginConfigCfg;
        File pluginConfigFile;
        // If the plugin folder does not exist, create the plugin folder
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        pluginConfigFile = new File(plugin.getDataFolder(),  Constants.LATCHS_LUCK_PERMS_CONFIG_FILE_NAME + ".yml");
        pluginConfigCfg = YamlConfiguration.loadConfiguration(pluginConfigFile);
        //if the config.yml does not exist, create it
        if(!pluginConfigFile.exists()){
            try {
                pluginConfigCfg.set("groupNameToGrant", "insertGroupNameHere");
                pluginConfigCfg.set("groupNameToRevoke", "insertGroupNameHere");
                pluginConfigCfg.save(pluginConfigFile);
            }
            catch(IOException e){
                System.out.println(ChatColor.RED + "Could not create the " + Constants.LATCHS_LUCK_PERMS_CONFIG_FILE_NAME + ".yml file");
            }
        }
    }
}

