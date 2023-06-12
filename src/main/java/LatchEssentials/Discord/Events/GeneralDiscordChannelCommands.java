package LatchEssentials.Discord.Events;

import LatchEssentials.Api.Api;
import LatchEssentials.Constants;
import LatchEssentials.Main;
import LatchEssentials.Discord.LatchsDiscord;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Objects;

import static LatchEssentials.Discord.LatchsDiscord.*;

public class GeneralDiscordChannelCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        messageChannel = e.getChannel();
        messageContents = e.getMessage().getContentRaw();
        senderDiscordUser = e.getAuthor();
        senderDiscordMember = e.getMember();
        senderDiscordUsername = senderDiscordUser.getName();
        senderDiscordUserID = senderDiscordUser.getId();
        jda = LatchsDiscord.getJDA();
        FileConfiguration enabledEventsCfg = Api.loadConfig(Constants.ENABLED_EVENTS_CONFIG_FILE_NAME);
        if (enabledEventsCfg.getBoolean("enableGeneralDiscordCommands") && senderDiscordMember != null && !senderDiscordUserID.equalsIgnoreCase(Constants.LATCH_BOT_DISCORD_USER_ID)) {
            if (messageContents.equalsIgnoreCase("!discordID")) {
                messageChannel.sendMessage("Hi " + senderDiscordUsername + "! Your Discord UserID is " + senderDiscordUserID).queue();
            }
            if (messageContents.equalsIgnoreCase("!joinTime")) {
                messageChannel.sendMessage(senderDiscordUsername + " joined on " + senderDiscordMember.getTimeJoined().toString().split("T")[0]).queue();
            }
            if (messageContents.equalsIgnoreCase("!link")) {
                senderDiscordUser.openPrivateChannel().flatMap(dm -> dm.sendMessage("IP = latch.ddns.net\n" +
                        "Java Port Number = 60\n" +
                        "Bedrock Port Number = 19132\n" +
                        "Run the following command in your minecraft client chat after you join:\n" +
                        "/lmp link " + e.getAuthor().getId())).queue(null, new ErrorHandler()
                        .handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                (ex) -> Main.log.warning("Cannot send link message to " + senderDiscordUsername)));
                messageChannel.sendMessage(senderDiscordUsername + " --- Check your Discord for a private message from my bot containing your link command.").queue();
                if (doesDiscordMemberHasRole(senderDiscordMember, Constants.DISCORD_MEMBER_ROLE_ID) == null){
                    assert Constants.DISCORD_SERVER_ID != null;
                    assert Constants.DISCORD_MEMBER_ROLE_ID != null;
                    Objects.requireNonNull(jda.getGuildById(Constants.DISCORD_SERVER_ID)).addRoleToMember(UserSnowflake.fromId(senderDiscordUserID), Objects.requireNonNull(jda.getRoleById(Constants.DISCORD_MEMBER_ROLE_ID))).queue();
                }
            }
        }
    }

    public Role doesDiscordMemberHasRole(Member senderDiscordMember, String roleID) {
        List<Role> roles = senderDiscordMember.getRoles();
        return roles.stream()
                .filter(role -> role.getId().equals(roleID)) // filter by role name
                .findFirst() // take first result
                .orElse(null); // else return null
    }
}