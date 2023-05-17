package Listeners;

import Commands.LatchsTwitchBotCommands;
import LatchsTwitchBot.Constants;
import LatchsTwitchBot.Api.Api;
import LatchsTwitchBot.LatchsTwitchBot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.Arrays;

public class HideOAuthTokenEvent implements Listener {

    public HideOAuthTokenEvent(LatchsTwitchBot plugin) { plugin.getServer().getPluginManager().registerEvents(this, plugin);}

    @EventHandler(priority = EventPriority.HIGHEST)
    private void hideOAuthTokenOnAddTwitchBotEvent(PlayerCommandPreprocessEvent e) throws IOException {
        if (e.getMessage().toLowerCase().contains("twitch addbot")){
            String[] args = e.getMessage().split(" ");
            if (args[1] != null && args[1].equalsIgnoreCase(Constants.ADD_BOT_COMMAND) && args[2] != null && args[3] != null) {
                LatchsTwitchBotCommands.addTwitchBotCredentials(args, Api.loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME), e.getPlayer());
            }
        }
    }
}
