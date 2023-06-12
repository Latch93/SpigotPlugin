package LatchEssentials.ConfigurationFiles;

import LatchEssentials.Constants;
import LatchEssentials.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class EnabledEventsConfig {
    public static final Main plugin = getPlugin(Main.class);
    // Set up config.yml configuration file
    public void setup(){
        FileConfiguration enabledEventsCfg;
        File enabledEventsFile;
        // If the plugin folder does not exist, create the plugin folder
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        enabledEventsFile = new File(plugin.getDataFolder(),  Constants.ENABLED_EVENTS_CONFIG_FILE_NAME + ".yml");
        enabledEventsCfg = YamlConfiguration.loadConfiguration(enabledEventsFile);
        //if the config.yml does not exist, create it
        if(!enabledEventsFile.exists()){
            try {
                enabledEventsCfg.save(enabledEventsFile);
            }
            catch(IOException e){
                System.out.println(ChatColor.RED + "Could not create the " + Constants.ENABLED_EVENTS_CONFIG_FILE_NAME + ".yml file");
            }
        }
    }
}