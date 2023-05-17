package LatchsTwitchBot.Api;

import LatchsTwitchBot.LatchTwitchBotRunnable;
import LatchsTwitchBot.LatchsTwitchBot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import LatchsTwitchBot.Constants;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class Api {
    public static final LatchsTwitchBot plugin = getPlugin(LatchsTwitchBot.class);
    public static File getConfigFile(String fileName){
        return new File(plugin.getDataFolder(), fileName + ".yml");
    }

    public static FileConfiguration getFileConfiguration(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration loadConfig(String fileName) {
        return YamlConfiguration.loadConfiguration(getConfigFile(fileName));
    }

    public static void messageInConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
    public static String getTwitchUsername(String minecraftUsername) {
        String twitchUsername = null;
        FileConfiguration twitchCfg = loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME);
        if (twitchCfg.isSet("players")) {
            for (String user : Objects.requireNonNull(twitchCfg.getConfigurationSection("players")).getKeys(false)) {
                if (Objects.requireNonNull(twitchCfg.getString(Constants.YML_PLAYERS + user + ".minecraftUsername")).equalsIgnoreCase(minecraftUsername)) {
                    twitchUsername = twitchCfg.getString(Constants.YML_PLAYERS + user + ".twitchUsername");
                }
            }
        }
        return twitchUsername;
    }

    public static void stopTwitchBot(List<LatchTwitchBotRunnable> twitchBotList, Player player) {
        Iterator<LatchTwitchBotRunnable> iter = twitchBotList.iterator();
        while (iter.hasNext()) {
            LatchTwitchBotRunnable runBot = iter.next();
            if (runBot.getMinecraftName().equalsIgnoreCase(player.getName())) {
                runBot.getTwitchClient().close();
                player.sendMessage(ChatColor.GREEN + "Your TwitchBot has been " + ChatColor.RED + "terminated.");
                Api.messageInConsole(ChatColor.RED + "Terminated " + ChatColor.GOLD + player.getName() + "'s " + ChatColor.RED + "TwitchBot.");
                iter.remove();
            }
        }
    }

    public static void stopAllTwitchBots(List<LatchTwitchBotRunnable> twitchBotList) {
        Iterator<LatchTwitchBotRunnable> iter = twitchBotList.iterator();
        while (iter.hasNext()) {
            LatchTwitchBotRunnable runBot = iter.next();
            runBot.getTwitchClient().close();
            Api.messageInConsole(ChatColor.RED + "Terminated " + ChatColor.GOLD + "ALL " + ChatColor.RED + "TwitchBots.");
            iter.remove();
        }
        Api.messageInConsole(ChatColor.RED + "Terminated " + ChatColor.GOLD + "all " + ChatColor.RED + "TwitchBots.");
    }

}
