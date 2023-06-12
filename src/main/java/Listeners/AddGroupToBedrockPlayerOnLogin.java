package Listeners;

import LatchEssentials.Api.Api;
import LatchEssentials.Main;
import LatchEssentials.Constants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AddGroupToBedrockPlayerOnLogin implements Listener {

    public AddGroupToBedrockPlayerOnLogin(Main plugin) { plugin.getServer().getPluginManager().registerEvents(this, plugin);}


    @EventHandler
    private void addGroupToBedrockPlayerOnLogin(PlayerJoinEvent e) {
        if (e.getPlayer().getName().contains(".")) {
            boolean grantPlayerGroup = false;
            FileConfiguration cfg = Api.loadConfig(Constants.LATCHS_LUCK_PERMS_CONFIG_FILE_NAME);
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