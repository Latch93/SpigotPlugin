package DefaultSpigotPlugin;

import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class DefaultSpigotPlugin extends JavaPlugin implements Listener {
    public Logger logger = getLogger();
    public Server server = getServer();

    @Override
    public void onEnable() {
        logger.info(Constants.PLUGIN_NAME + " is enabled");
        server.getPluginManager().registerEvents(this, this);
        createConfigurationFiles();
    }

    @Override
    public void onDisable() {
        getLogger().info(Constants.PLUGIN_NAME + " is disabled");
    }

    public static void createConfigurationFiles(){
        DefaultSpigotPluginConfig invDropConfigCfgm = new DefaultSpigotPluginConfig();
        invDropConfigCfgm.setup();
    }

}
