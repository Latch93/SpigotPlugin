package Commands;

import LatchEssentials.Api.Api;
import LatchEssentials.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class LatchsLuckPermsCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        FileConfiguration cfg = Api.loadConfig(Constants.LATCHS_LUCK_PERMS_CONFIG_FILE_NAME);
        if (Boolean.TRUE.equals(player.isOp())) {
            if (args[0].equalsIgnoreCase(Constants.GRANT_COMMAND)) {
                String groupNameToGive = cfg.getString("groupNameToGrant");
                try {
                    Player playerToGrant = Bukkit.getPlayer(args[1]);
                    assert playerToGrant != null;
                    Api.grantPlayerGroup(playerToGrant, groupNameToGive);
                } catch (NullPointerException e) {
                    player.sendMessage(ChatColor.RED + "Could not get player from entered name. Please try again.");
                }
            } else if (args[0].equalsIgnoreCase(Constants.REVOKE_COMMAND)) {
                String groupNameToGive = cfg.getString("groupNameToRevoke");
                try {
                    Player playerToGrant = Bukkit.getPlayer(args[1]);
                    assert playerToGrant != null;
                    Api.revokePlayerGroup(playerToGrant, groupNameToGive);
                } catch (NullPointerException e) {
                    player.sendMessage(ChatColor.RED + "Could not get player from entered name. Please try again.");
                }
            } else if (args[0].equalsIgnoreCase(Constants.CHECK_GROUPS_COMMAND)) {
                try {
                    Player playerToGrant = Bukkit.getPlayer(args[1]);
                    assert playerToGrant != null;
                    Api.getPlayerGroups(playerToGrant);
                } catch (NullPointerException e) {
                    player.sendMessage(ChatColor.RED + "Could not get player from entered name. Please try again.");
                }
            }
        } else{
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
        }
        return false;
    }

}