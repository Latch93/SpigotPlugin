package LatchsTwitchBot;

import Commands.LatchsTwitchBotCommands;
import LatchsTwitchBot.Api.Api;
import LatchsTwitchBot.TabComplete.LatchTwitchBotTabComplete;
import Listeners.HideOAuthTokenEvent;
import Listeners.StopTwitchBotOnPlayerLogoutEvent;
import org.apache.logging.log4j.LogManager;

import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;


public class LatchsTwitchBot extends JavaPlugin implements Listener {
    public java.util.logging.Logger logger = getLogger();
    public Server server = getServer();

    @Override
    public void onEnable() {
        logger.info(Constants.PLUGIN_NAME + " is enabled");
        server.getPluginManager().registerEvents(this, this);
        createConfigurationFiles();
        registerCommands();
        new StopTwitchBotOnPlayerLogoutEvent(this);
        new HideOAuthTokenEvent(this);
        registerLoggerFilters(new MyLogFilter());
    }

    @Override
    public void onDisable() {
        Api.stopAllTwitchBots(LatchsTwitchBotCommands.twitchBotList);
        getLogger().info(Constants.PLUGIN_NAME + " is disabled");
    }

    private void registerLoggerFilters(Filter... filters) {
        org.apache.logging.log4j.Logger rootLogger = LogManager.getRootLogger();
        if (!(rootLogger instanceof Logger logger)) {
            // in case the root logger is not the expected instance of Logger, just return
            // because there is something wrong
            return;
        }

        for (Filter filter : filters) {
            // register all filters onto the root logger
            logger.addFilter(filter);
        }
    }

    public static void createConfigurationFiles(){
        LatchsTwitchBotConfig latchsTwitchBotCfg = new LatchsTwitchBotConfig();
        latchsTwitchBotCfg.setup();
    }

    private void registerCommands() {
        // Server Command
        Objects.requireNonNull(this.getCommand(Constants.TWITCH_COMMAND)).setExecutor(new LatchsTwitchBotCommands());
        Objects.requireNonNull(this.getCommand(Constants.TWITCH_COMMAND)).setTabCompleter(new LatchTwitchBotTabComplete());
    }


}
