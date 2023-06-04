package Listeners;

import LatchsLuckPerms.Api.Api;
import LatchsLuckPerms.LatchsLuckPerms;
import LatchsLuckPerms.Constants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

public class AddGroupToBedrockPlayerOnLogin implements Listener {

    public AddGroupToBedrockPlayerOnLogin(LatchsLuckPerms plugin) { plugin.getServer().getPluginManager().registerEvents(this, plugin);}


    @EventHandler
    private void addGroupToBedrockPlayerOnLogin(PlayerJoinEvent e) {
        if (e.getPlayer().getName().contains(".")) {
            boolean grantPlayerGroup = false;
            FileConfiguration cfg = Api.loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME);
            String groupNameToGive = cfg.getString("groupNameToGrant");
            for (String group : Api.getPlayerGroups(e.getPlayer())){
                if (group.equalsIgnoreCase(groupNameToGive)) {
                    grantPlayerGroup = true;
                    break;
                }
            }
            if (Boolean.TRUE.equals(grantPlayerGroup)) {
                Api.grantPlayerGroup(e.getPlayer(), groupNameToGive);
            }
        }
    }
}