package LatchEssentials.Discord;

import LatchEssentials.Api.Api;
import LatchEssentials.Constants;
import LatchEssentials.Discord.Events.GeneralDiscordChannelCommands;
import LatchEssentials.Discord.Events.SendServerStartMessageToDiscordEvent;
import LatchEssentials.Discord.Events.UpdateWhitelistOnMemberLeaveGuildEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Mentions;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.dv8tion.jda.api.entities.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class LatchsDiscord extends ListenerAdapter implements Listener {
    public static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(Constants.PLUGIN_NAME);

    public static String senderDiscordUsername = "";
    public static String senderDiscordUserID = "";
    public static User senderDiscordUser;
    public static Member senderDiscordMember;
    public static Message message;
    public static String messageContents;
    public static String messageID;
    public static Mentions mentionedChannelsList;
    public static List<User> mentionedUsersList;
    public static MessageChannel messageChannel;
    public static String messageChannelID;
    public static JDA jda;
    public static final JDABuilder jdaBuilder = JDABuilder.createDefault(Constants.DISCORD_BOT_TOKEN)
            .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
            .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT);

    public LatchsDiscord() throws LoginException {
        startBot();
        jda.addEventListener(this);
    }
    private void startBot() {
        jda = jdaBuilder.build();
        jda.addEventListener(new GeneralDiscordChannelCommands());
        jda.addEventListener(new SendServerStartMessageToDiscordEvent());
        jda.addEventListener(new UpdateWhitelistOnMemberLeaveGuildEvent());
//        jda.addEventListener(new LogPlayerBanFromDiscordConsole());
//        jda.addEventListener(new BanPlayerForTypingTheNWordInDiscordChat());
//        jda.addEventListener(new RemoveServerRoleOnGuildMemberRemove());
//        jda.addEventListener(new NewGuildMemberJoin());
//        jda.addEventListener(new ShowOnlinePlayersInDiscord());
//        jda.addEventListener(new SendLTSNominationForm());
//        jda.addEventListener(new SendAFKMessageIfMentionLatch());
    }

    public static JDA getJDA() {
        return jda;
    }

    public static Message getMessage(){return message;}

    public static String convertDiscordMessageToServer(Member member, String message, String senderName, Boolean isReply, Message repliedMessage, Boolean staffMessage) {
        int count = 0;
        String highestRole = "Member";
        ChatColor colorCode;
        for (Role role : Objects.requireNonNull(member).getRoles()) {
            if (role.getPosition() >= count) {
                count = role.getPosition();
                highestRole = role.getName();
            }
        }
        if (highestRole.equalsIgnoreCase("Owner")) {
            colorCode = ChatColor.GOLD;
        } else if (highestRole.toLowerCase().contains("admin")) {
            colorCode = ChatColor.RED;
        } else if (highestRole.toLowerCase().contains("mod")) {
            colorCode = ChatColor.LIGHT_PURPLE;
        } else if (highestRole.toLowerCase().contains("builder")) {
            colorCode = ChatColor.BLUE;
        } else {
            colorCode = ChatColor.GREEN;
        }
        String finalMessage;
        if (staffMessage){
            if (isReply) {
                finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Discord" + ChatColor.WHITE + " | " + colorCode + highestRole + ChatColor.DARK_GRAY + "] " + senderName + ChatColor.GRAY + " » " + ChatColor.WHITE + "Replied to " + ChatColor.GOLD + repliedMessage.getAuthor().getName() +
                        ChatColor.GRAY + " » " + ChatColor.GREEN + "'" + repliedMessage.getContentRaw() + "'" + ChatColor.GRAY + " » " + ChatColor.AQUA + message;
            } else {
                finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Discord" + ChatColor.WHITE + " | " + colorCode + highestRole + ChatColor.DARK_GRAY + "] " + senderName + ChatColor.GRAY + " - " + ChatColor.GRAY + "» " + ChatColor.AQUA + message;
            }
        } else {
            if (isReply) {
                finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Discord" + ChatColor.WHITE + " | " + colorCode + highestRole + ChatColor.DARK_GRAY + "] " + senderName + ChatColor.GRAY + " » " + ChatColor.WHITE + "Replied to " + ChatColor.GOLD + repliedMessage.getAuthor().getName() +
                        ChatColor.GRAY + " » " + ChatColor.GREEN + "'" + repliedMessage.getContentRaw() + "'" + ChatColor.GRAY + " » " + ChatColor.WHITE + message;
            } else {
                finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Discord" + ChatColor.WHITE + " | " + colorCode + highestRole + ChatColor.DARK_GRAY + "] " + senderName + ChatColor.GRAY + " - " + ChatColor.GRAY + "» " + ChatColor.WHITE + message;
            }
        }
        return finalMessage;
    }

    public static void sendServerStoppedMessage() {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM-dd-yyyy hh:mm:ssa z");
        String dtStr = fmt.print(dt);
        FileConfiguration configCfg = Api.loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME);
        configCfg.set("serverStopTime", dtStr);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("LMP Server has stopped", null);
        eb.setColor(new Color(0xE15C0000, true));
        eb.setDescription(dtStr);
        eb.setThumbnail("https://raw.githubusercontent.com/Latch93/DiscordText/master/src/main/resources/lmp_discord_image.png");
        assert Constants.MINECRAFT_CHAT_DISCORD_CHANNEL_ID != null;
        TextChannel minecraftChannel = jda.getTextChannelById(Constants.MINECRAFT_CHAT_DISCORD_CHANNEL_ID);
        assert minecraftChannel != null;
        minecraftChannel.sendMessageEmbeds(eb.build()).queue();
    }

    public static void setDiscordId() throws IOException {
        FileConfiguration whitelistCfg = Api.loadConfig(Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME);
        assert Constants.DISCORD_SERVER_ID != null;
        List<Member> members = Objects.requireNonNull(jda.getGuildById(Constants.DISCORD_SERVER_ID)).getMembers();
        String players = Constants.YML_PLAYERS;
        for (String playerName : Objects.requireNonNull(whitelistCfg.getConfigurationSection(Constants.PLAYERS)).getKeys(false)) {
            for (Member member : members) {
                if (member.getUser().getName().equalsIgnoreCase(whitelistCfg.getString(players + playerName + ".discordName"))) {
                    whitelistCfg.set(players + playerName + ".discordId", member.getId());
                    whitelistCfg.set(players + playerName + ".joinedTime", null);
                    whitelistCfg.set(players + playerName + ".joinTime", member.getTimeJoined().toLocalDateTime().toString());
                }
            }
            if (!whitelistCfg.isSet(players + playerName + ".discordId")) {
                whitelistCfg.set(players + playerName, null);
            }
        }
        whitelistCfg.save(Api.getConfigFile(Constants.LATCHS_DISCORD_WHITELIST_FILE_NAME));
    }

    public static String getDiscordUserId(String discordUserName) {
        String discordUserId = "";
        assert Constants.DISCORD_SERVER_ID != null;
        for (Member member : Objects.requireNonNull(jda.getGuildById(Constants.DISCORD_SERVER_ID)).getMembers()) {
            if (discordUserName.equalsIgnoreCase(member.getUser().getName())) {
                discordUserId = member.getId();
            }
        }
        return discordUserId;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        messageChannel = e.getChannel();
        message = e.getMessage();
        if (e.getMember() != null) {
            senderDiscordUsername = Objects.requireNonNull(e.getMember().getUser().getName());
            senderDiscordUserID = Objects.requireNonNull(e.getMember().getId());
        }
        mentionedChannelsList = message.getMentions();
        mentionedUsersList = message.getMentions().getUsers();
        messageID = e.getMessageId();
        messageContents = e.getMessage().getContentRaw();
        senderDiscordUsername = e.getAuthor().getName();
        senderDiscordMember = e.getMember();
        messageChannelID = e.getChannel().getId();
        senderDiscordUser = e.getAuthor();
        jda = getJDA();
        for (GuildChannel guildChannel : mentionedChannelsList.getChannels()) {
            messageContents = messageContents.replace(guildChannel.getId(), guildChannel.getName());
        }
        for (User user : mentionedUsersList) {
            messageContents = messageContents.replace(user.getId(), user.getName());
        }
        try {
            if ((messageChannel.getId().equalsIgnoreCase(Constants.MINECRAFT_CHAT_DISCORD_CHANNEL_ID) && !e.getAuthor().getId().equals(Constants.LATCH_BOT_DISCORD_USER_ID))) {
                if (e.getMessage().getReferencedMessage() != null) {
                    Bukkit.broadcastMessage(convertDiscordMessageToServer(senderDiscordMember, messageContents, senderDiscordUsername, true, e.getMessage().getReferencedMessage(), false));
                } else {
                    Bukkit.broadcastMessage(convertDiscordMessageToServer(senderDiscordMember, messageContents, senderDiscordUsername, false, null, false));
                }
            }
//            if ((messageChannel.getId().equalsIgnoreCase(Constants.MINECRAFT_CHAT_DISCORD_CHANNEL_ID) || messageChannel.getId().equalsIgnoreCase(Constants.MINECRAFT_SERVER_STAFF_DISCORD_CHANNEL_ID))) {
//                if (messageContents.toLowerCase().contains("!searchdiscord")) {
//                    String[] messageArr = messageContents.split(" ");
//                    try {
//                        String discordUserName = Api.getDiscordNameFromMCid(Api.getMinecraftIdFromMinecraftName(messageArr[1]));
//                        messageChannel.sendMessage(messageArr[1] + "'s Discord username is: " + discordUserName).queue();
//
//                    } catch (IllegalArgumentException error) {
//                        messageChannel.sendMessage("That player does not have a discord account linked to their minecraft account. Maybe try again.\nCommand usage: !searchDiscord [minecraftName]").queue();
//                    }
//                } else if (messageContents.toLowerCase().contains("!searchminecraft")) {
//                    String[] messageArr = messageContents.split(" ");
//                    try {
//
//                        if (messageArr.length > 0) {
//                            String minecraftUsername = Bukkit.getOfflinePlayer(UUID.fromString(Api.getMinecraftIdFromDCid(messageArr[1]))).getName();
//                            messageChannel.sendMessage(Objects.requireNonNull(jda.getUserById(messageArr[1])).getName() + "'s Minecraft username is: " + minecraftUsername).queue();
//                        }
//                    } catch (IllegalArgumentException error) {
//                        messageChannel.sendMessage("That player does not have a minecraft account linked to their discord account. Maybe try again.\nCommand usage: !searchMinecraft [discordID]").queue();
//                    }
//                }
//            }
//            // Sends staff application to member
//            if (messageChannel.getId().equals(lmp.Constants.STAFF_APPLICATION_CHANNEL_ID) && messageContents.equalsIgnoreCase(lmp.Constants.STAFF_APPLY_COMMAND)) {
//                messageChannel.deleteMessageById(messageID).queue();
//                e.getAuthor().openPrivateChannel().flatMap(privateChannel -> {
//                    TextChannel applicationSubmittedChannel = jda.getTextChannelById(lmp.Constants.STAFF_APP_SUBMITTED_CHANNEL_ID);
//                    e.getJDA().addEventListener(new StaffApplication(privateChannel, e.getAuthor(), applicationSubmittedChannel));
//                    return privateChannel.sendMessage("\nResponsibilities as a Jr. Mod:\n*** Watch new players on the server while in vanish.\n*** Ensure new players are following the rules, i.e., not x-raying, griefing, stealing or being a jerk.\n" +
//                            "Please enter your application information line by line.\nPress enter after each question response.\n" +
//                            "1.) How old are you?");
//                }).queue();
//            }
//            // Sends unban request to member
//            if (messageChannel.getId().equals(lmp.Constants.UNBAN_REQUEST_CHANNEL_ID) && messageContents.equalsIgnoreCase(lmp.Constants.UNBAN_REQUEST)) {
//                messageChannel.deleteMessageById(messageID).queue();
//                e.getAuthor().openPrivateChannel().flatMap(privateChannel -> {
//                    TextChannel unbanRequestSubmittedChannel = jda.getTextChannelById(lmp.Constants.UNBAN_REQUEST_COMPLETE_CHANNEL_ID);
//                    e.getJDA().addEventListener(new UnbanRequest(privateChannel, e.getAuthor(), unbanRequestSubmittedChannel));
//                    return privateChannel.sendMessage("Please enter your unban form line by line. \n Press enter after each question response. \n 1.) What is your Minecraft username?");
//                }).queue();
//            }
        } catch (NullPointerException error) {
            error.printStackTrace();
        }

        if (messageChannel.getId().equalsIgnoreCase(Constants.MINECRAFT_SERVER_STAFF_DISCORD_CHANNEL_ID) && !e.getAuthor().getId().equalsIgnoreCase(Constants.LATCH_BOT_DISCORD_USER_ID)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                assert Constants.LUCK_PERMS_MOD_GROUP != null;
                if (player.hasPermission(Constants.LUCK_PERMS_MOD_GROUP)) {
                    if (e.getMessage().getReferencedMessage() != null) {
                        player.sendMessage("[" + ChatColor.LIGHT_PURPLE + "Mod Chat" + ChatColor.WHITE + "] - " + convertDiscordMessageToServer(senderDiscordMember, messageContents, senderDiscordUsername, true, e.getMessage().getReferencedMessage(), true));
                    } else {
                        player.sendMessage("[" + ChatColor.LIGHT_PURPLE + "Mod Chat" + ChatColor.WHITE + "] - " + convertDiscordMessageToServer(senderDiscordMember, messageContents, senderDiscordUsername, false, null, true));
                    }
                }
            }
        }
        if (messageChannel.getId().equalsIgnoreCase(Constants.MINECRAFT_SERVER_ADMIN_DISCORD_CHANNEL_ID) && !e.getAuthor().getId().equalsIgnoreCase(Constants.LATCH_BOT_DISCORD_USER_ID)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                assert Constants.LUCK_PERMS_ADMIN_GROUP != null;
                if (player.hasPermission(Constants.LUCK_PERMS_ADMIN_GROUP)) {
                    if (e.getMessage().getReferencedMessage() != null) {
                        player.sendMessage("[" + ChatColor.DARK_PURPLE + "Admin Chat" + ChatColor.WHITE + "] - " + convertDiscordMessageToServer(senderDiscordMember, messageContents, senderDiscordUsername, true, e.getMessage().getReferencedMessage(), true));
                    } else {
                        player.sendMessage("[" + ChatColor.DARK_PURPLE + "Admin Chat" + ChatColor.WHITE + "] - " + convertDiscordMessageToServer(senderDiscordMember, messageContents, senderDiscordUsername, false, null, true));
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Api.messageInConsole(ChatColor.GOLD + Objects.requireNonNull(event.getMember()).getUser().getName() + ChatColor.RED + " left the discord.");
        assert Constants.USER_LEAVES_DISCORD_CHANNEL_ID != null;
        TextChannel byebyeDiscordChat = jda.getTextChannelById(Constants.USER_LEAVES_DISCORD_CHANNEL_ID);
        assert byebyeDiscordChat != null;
        byebyeDiscordChat.sendMessage(event.getMember().getUser().getName() + " left Discord. ").queue();
    }


//    public static void setChannelDescription() {
//        TextChannel minecraftChatChannel = jda.getTextChannelById(Constants.MINECRAFT_CHAT_CHANNEL_ID);
//        String maxPlayerCount = String.valueOf(Bukkit.getServer().getMaxPlayers());
//        assert minecraftChatChannel != null;
//        int count = 0;
//        for (Player player : Bukkit.getOnlinePlayers()){
//            if (!Api.isPlayerInvisible(player.getUniqueId().toString())){
//                count++;
//            }
//        }
//        FileConfiguration configCfg = Api.loadConfig(Api.getConfigFile(YmlFileNames.YML_CONFIG_FILE_NAME));
//        DateTime currentTime = new DateTime();
//        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM-dd-yyyy HH:mm:ss");
//        String current = fmt.print(currentTime);
//        DateTime startTime = fmt.parseDateTime(configCfg.getString("serverStartTime"));
//        DateTime now = fmt.parseDateTime(current);
//        Duration duration = new Duration(startTime, now);
//        minecraftChatChannel.getManager().setTopic("Online Players: " + count + "/" + maxPlayerCount + " | Server Uptime: " + duration.getStandardMinutes() + " minutes.").queue();
//    }

    public void clearAllUserMessages(MessageChannel channel, String messageId, String userID) {
        channel.deleteMessageById(messageId).queue();
        MessageHistory history = MessageHistory.getHistoryFromBeginning(channel).complete();
        List<Message> messageHistory = history.getRetrievedHistory();
        for (Message message : messageHistory) {
            if (message.getAuthor().getId().equalsIgnoreCase(userID)) {
                channel.deleteMessageById(message.getId()).queue();
            }
        }
    }

}
