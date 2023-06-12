package LatchEssentials.Discord.Events;

import LatchEssentials.Api.Api;
import LatchEssentials.Constants;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class UpdateWhitelistOnMemberLeaveGuildEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        String minecraftId = Api.getMinecraftIdFromDCid(Objects.requireNonNull(event.getMember()).getUser().getId());
        FileConfiguration whitelistCfg = Api.loadConfig(Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME);
        if (!minecraftId.isEmpty()){
            try {
                if (whitelistCfg.isSet(Constants.YML_PLAYERS + minecraftId + ".isPlayerInDiscord")) {
                    whitelistCfg.set(Constants.YML_PLAYERS + minecraftId + ".isPlayerInDiscord", false);
                    whitelistCfg.save(Api.getConfigFile(Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME));

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}