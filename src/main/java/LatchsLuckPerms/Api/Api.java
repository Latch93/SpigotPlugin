package LatchsLuckPerms.Api;

import LatchsLuckPerms.LatchsLuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class Api {
    public static final LatchsLuckPerms plugin = getPlugin(LatchsLuckPerms.class);
    public static File getConfigFile(String fileName){
        return new File(plugin.getDataFolder(), fileName + ".yml");
    }

    public static FileConfiguration getFileConfiguration(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration loadConfig(String fileName) {
        return YamlConfiguration.loadConfiguration(getConfigFile(fileName));
    }

    public static void grantPlayerGroup(Player player, String groupName){
        User user = LatchsLuckPerms.luckPerms.getUserManager().getUser(player.getUniqueId());
        assert user != null;
        user.setPrimaryGroup(groupName);
        InheritanceNode member = InheritanceNode.builder(groupName).value(true).build();
        user.data().add(member);
        LatchsLuckPerms.luckPerms.getUserManager().saveUser(user);
    }

    public static void revokePlayerGroup(Player player, String groupName){
        User user = LatchsLuckPerms.luckPerms.getUserManager().getUser(player.getUniqueId());
        assert user != null;
        user.setPrimaryGroup(groupName);
        InheritanceNode member = InheritanceNode.builder(groupName).value(true).build();
        user.data().remove(member);
        LatchsLuckPerms.luckPerms.getUserManager().saveUser(user);
    }

    public static Set<String> getPlayerGroups(Player player){
        User user = LatchsLuckPerms.luckPerms.getUserManager().getUser(player.getUniqueId());
        assert user != null;
        Set<String> groups = user.getNodes(NodeType.INHERITANCE).stream()
            .map(InheritanceNode::getGroupName)
            .collect(Collectors.toSet());
        player.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " is in the following LuckPerms groups: " + ChatColor.GOLD + groups);
        return groups;
    }


}
