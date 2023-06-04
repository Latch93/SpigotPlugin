package LatchsLuckPerms;

import Commands.LatchsLuckPermsCommands;
import LatchsLuckPerms.TabComplete.LatchsLuckPermsTabComplete;
import Listeners.AddGroupToBedrockPlayerOnLogin;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;


public class LatchsLuckPerms extends JavaPlugin implements Listener {

    public static Logger log;
    public Server server = getServer();
    public static LuckPerms luckPerms;


    @Override
    public void onEnable() {
        log = getLogger();
        log.info(Constants.PLUGIN_NAME + " is enabled");
        server.getPluginManager().registerEvents(this, this);
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
    }

    public static void createConfigurationFiles(){
        LatchsLuckPermsConfig latchsTwitchBotCfg = new LatchsLuckPermsConfig();
        latchsTwitchBotCfg.setup();
    }

    private void registerCommands() {
        // Server Command
        Objects.requireNonNull(this.getCommand(Constants.LATCHS_LUCK_PERMS_COMMAND)).setExecutor(new LatchsLuckPermsCommands());
        Objects.requireNonNull(this.getCommand(Constants.LATCHS_LUCK_PERMS_COMMAND)).setTabCompleter(new LatchsLuckPermsTabComplete());
    }

    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }

}
