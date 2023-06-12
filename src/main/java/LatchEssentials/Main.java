package LatchEssentials;

import Commands.LatchsDiscordEssentialsCommands;
import Commands.LatchsLuckPermsCommands;
import LatchEssentials.Api.Api;
import LatchEssentials.ConfigurationFiles.EnabledEventsConfig;
import LatchEssentials.ConfigurationFiles.LatchsDiscordEssentialsMainConfig;
import LatchEssentials.ConfigurationFiles.LatchsDiscordEssentialsWhitelistConfig;
import LatchEssentials.ConfigurationFiles.LatchsLuckPermsConfig;
import LatchEssentials.TabComplete.LatchsLuckPermsTabComplete;
import LatchEssentials.Discord.LatchsDiscord;
import Listeners.AddGroupToBedrockPlayerOnLogin;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.Objects;
import java.util.logging.Logger;


public class Main extends JavaPlugin implements Listener {

    public static Logger log;
    public Server server = getServer();
    public static LuckPerms luckPerms;


    @Override
    public void onEnable() {
        log = getLogger();
        log.info(Constants.PLUGIN_NAME + " is enabled");
        server.getPluginManager().registerEvents(this, this);
        if (Api.loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME).getBoolean(Constants.ENABLE_LATCH_DISCORD_KEY)) {
            try {
                new LatchsDiscord();
            } catch (LoginException e) {
                throw new RuntimeException(e);
            }
        }
        createConfigurationFiles();
        registerCommands();
        new AddGroupToBedrockPlayerOnLogin(this);
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }


    }

    @Override
    public void onDisable() {
        log.info(Constants.PLUGIN_NAME + " is disabled");
//        if (enabledEventsCfg.getBoolean("broadcastServerStoppedMessageToDiscord")) {
            LatchsDiscord.sendServerStoppedMessage();
//        }
        Api.stopDiscordBot();
    }

    public static void createConfigurationFiles(){
        LatchsLuckPermsConfig latchsLuckPermsCfg = new LatchsLuckPermsConfig();
        latchsLuckPermsCfg.setup();
        LatchsDiscordEssentialsMainConfig latchsDiscordEssentialsMainCfg = new LatchsDiscordEssentialsMainConfig();
        latchsDiscordEssentialsMainCfg.setup();
        LatchsDiscordEssentialsWhitelistConfig latchsDiscordEssentialsWhitelistCfg = new LatchsDiscordEssentialsWhitelistConfig();
        latchsDiscordEssentialsWhitelistCfg.setup();
        EnabledEventsConfig enabledEventsCfg = new EnabledEventsConfig();
        enabledEventsCfg.setup();

    }

    private void registerCommands() {
        // Server Command
        Objects.requireNonNull(this.getCommand(Constants.LATCHS_LUCK_PERMS_COMMAND)).setExecutor(new LatchsLuckPermsCommands());
        Objects.requireNonNull(this.getCommand(Constants.LATCHS_LUCK_PERMS_COMMAND)).setTabCompleter(new LatchsLuckPermsTabComplete());
        Objects.requireNonNull(this.getCommand(Constants.LATCHS_DISCORD_ESSENTIALS_COMMAND)).setExecutor(new LatchsDiscordEssentialsCommands());
        Objects.requireNonNull(this.getCommand(Constants.LATCHS_DISCORD_ESSENTIALS_COMMAND)).setTabCompleter(new LatchsLuckPermsTabComplete());
    }

    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }

}
