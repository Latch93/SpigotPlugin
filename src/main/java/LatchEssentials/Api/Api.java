package LatchEssentials.Api;

import LatchEssentials.Constants;
import LatchEssentials.Discord.LatchsDiscord;
import LatchEssentials.Main;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class Api {
    public static final Main plugin = getPlugin(Main.class);

    // YML File API
    public static File getConfigFile(String fileName){
        return new File(plugin.getDataFolder(), fileName + ".yml");
    }

    public static FileConfiguration getFileConfiguration(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration loadConfig(String fileName) {
        return YamlConfiguration.loadConfiguration(getConfigFile(fileName));
    }
    // YML File API

    // Luck Perms API
    public static void grantPlayerGroup(Player player, String groupName){
        User user = Main.luckPerms.getUserManager().getUser(player.getUniqueId());
        assert user != null;
        user.setPrimaryGroup(groupName);
        InheritanceNode member = InheritanceNode.builder(groupName).value(true).build();
        user.data().add(member);
        Main.luckPerms.getUserManager().saveUser(user);
    }

    public static void revokePlayerGroup(Player player, String groupName){
        User user = Main.luckPerms.getUserManager().getUser(player.getUniqueId());
        assert user != null;
        user.setPrimaryGroup(groupName);
        InheritanceNode member = InheritanceNode.builder(groupName).value(true).build();
        user.data().remove(member);
        Main.luckPerms.getUserManager().saveUser(user);
    }

    public static Set<String> getPlayerGroups(Player player){
        User user = Main.luckPerms.getUserManager().getUser(player.getUniqueId());
        assert user != null;
        Set<String> groups = user.getNodes(NodeType.INHERITANCE).stream()
            .map(InheritanceNode::getGroupName)
            .collect(Collectors.toSet());
        player.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " is in the following LuckPerms groups: " + ChatColor.GOLD + groups);
        return groups;
    }
    // Luck Perms API

    // Latch Api
    public static String getDiscordNameFromMCid(String minecraftID) {
        assert Constants.DISCORD_SERVER_ID != null;
        return Objects.requireNonNull(Objects.requireNonNull(LatchsDiscord.getJDA().getGuildById(Constants.DISCORD_SERVER_ID)).getMemberById(Api.getDiscordIdFromMCid(minecraftID))).getUser().getName();
    }

    public static String getDiscordIdFromMCid(String minecraftID) {
        FileConfiguration whitelistCfg = loadConfig(Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME);
        String discordID = "";
        for (String mcID : Objects.requireNonNull(whitelistCfg.getConfigurationSection(Constants.PLAYERS)).getKeys(false)) {
            if (minecraftID.equalsIgnoreCase(mcID)) {
                discordID = whitelistCfg.getString(Constants.YML_PLAYERS + mcID + ".discordId");
            }
        }
        return discordID;
    }
    public static String getMinecraftIdFromMinecraftName(String minecraftName) {
        FileConfiguration whitelistCfg = loadConfig(Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME);
        String minecraftID = "";
        for (String mcID : Objects.requireNonNull(whitelistCfg.getConfigurationSection(Constants.PLAYERS)).getKeys(false)) {
            if (minecraftName.equalsIgnoreCase(whitelistCfg.getString(Constants.YML_PLAYERS + mcID + ".minecraftName"))) {
                minecraftID = whitelistCfg.getString(Constants.YML_PLAYERS + mcID + ".minecraftId");
            }
        }
        return minecraftID;
    }

    public static String getMinecraftNameFromMinecraftId(String minecraftID) {
        FileConfiguration whitelistCfg = loadConfig(Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME);
        String minecraftName = "";
        for (String mcID : Objects.requireNonNull(whitelistCfg.getConfigurationSection(Constants.PLAYERS)).getKeys(false)) {
            if (minecraftID.equalsIgnoreCase(whitelistCfg.getString(Constants.YML_PLAYERS + mcID + ".minecraftId"))) {
                minecraftName = whitelistCfg.getString(Constants.YML_PLAYERS + mcID + ".minecraftName");
            }
        }
        return minecraftName;
    }

    public static String getMinecraftIdFromDCid(String discordId) {
        FileConfiguration whitelistCfg = Api.loadConfig(Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME);
        String minecraftId = "";
        for (String mcID : whitelistCfg.getConfigurationSection(Constants.PLAYERS).getKeys(false)) {
            if (discordId.equalsIgnoreCase(whitelistCfg.getString(Constants.YML_PLAYERS + mcID + ".discordId"))) {
                minecraftId = whitelistCfg.getString(Constants.YML_PLAYERS + mcID + ".minecraftId");
            }
        }
        return minecraftId;
    }

    public static void messageInConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public static Main getMainPlugin() {
        return getPlugin(Main.class);
    }
    public static void stopDiscordBot() {
        LatchsDiscord.getJDA().shutdown();
    }

}
