package LatchsTwitchBot;

import LatchsTwitchBot.Api.Api;
import org.bukkit.configuration.file.FileConfiguration;
import com.cavariux.latchtwitch.Core.TwitchBot;

import java.io.IOException;

public class LatchBot extends TwitchBot {

    LatchBot bot;

    public LatchBot(String twitchUserName, String oauthToken, String minecraftUsername) throws IOException {
        FileConfiguration twitchCfg = Api.loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME);
        twitchCfg.set(Constants.YML_PLAYERS + twitchUserName.toLowerCase() + ".twitchUsername", twitchUserName.toLowerCase());
        twitchCfg.set(Constants.YML_PLAYERS + twitchUserName.toLowerCase() + ".minecraftUsername", minecraftUsername);
        twitchCfg.set(Constants.YML_PLAYERS + twitchUserName.toLowerCase() + ".oauthToken", oauthToken);
        twitchCfg.save(Api.getConfigFile(Constants.PLUGIN_CONFIG_FILE_NAME));
        this.setUsername(twitchUserName.toLowerCase());
        this.setOauth_Key(oauthToken);
        this.setClientID(Api.loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME).getString("clientId"));
    }

    public void setBot(LatchBot bot) {
        this.bot = bot;
    }

    public LatchBot getTwitchBot() {
        return this.bot;
    }
}