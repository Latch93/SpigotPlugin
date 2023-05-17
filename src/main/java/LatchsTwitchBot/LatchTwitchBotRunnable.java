package LatchsTwitchBot;

import LatchsTwitchBot.Constants;
import LatchsTwitchBot.LatchBot;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.*;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.pubsub.events.ChannelBitsEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.util.Objects;

public class LatchTwitchBotRunnable implements Runnable {
    LatchBot bot;
    String twitchName;
    String oauthToken;
    String minecraftName;
    TwitchClient twitchClient;
    String channelID;
    TwitchChat twitchChat;

    public LatchTwitchBotRunnable(String twitchName, String oauthToken, String minecraftName, String channelID) {
        this.twitchName = twitchName.toLowerCase();
        this.oauthToken = oauthToken.toLowerCase();
        this.minecraftName = minecraftName;
        this.channelID = channelID;
    }

    public void run() {
        Bukkit.getScheduler().runTaskAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(Constants.PLUGIN_NAME)), () -> {
            OAuth2Credential credential = new OAuth2Credential("twitch", oauthToken);
            this.twitchClient = TwitchClientBuilder.builder()
                    .withEnableChat(true)
                    .withChatAccount(credential)
                    .withEnablePubSub(true)
                    .withDefaultEventHandler(SimpleEventHandler.class)
                    .build();
            this.twitchChat = twitchClient.getChat();
            this.twitchChat.joinChannel(twitchName);
            this.twitchChat.sendMessage(twitchName, "Your TwitchBot is now enabled");
            this.twitchChat.getEventManager().onEvent(ChannelMessageEvent.class, e -> {
                try {
                    twitchChatResponseMessage(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            if (channelID != null) {
                this.twitchClient.getPubSub().listenForFollowingEvents(credential, channelID);
                this.twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, channelID);
                this.twitchClient.getPubSub().listenForSubscriptionEvents(credential, channelID);
                this.twitchClient.getPubSub().listenForCheerEvents(credential, channelID);
                this.twitchClient.getPubSub().listenForChannelSubGiftsEvents(credential, channelID);
                this.twitchClient.getPubSub().listenForRaidEvents(credential, channelID);
                this.twitchClient.getPubSub().listenForCommunityBoostEvents(credential, channelID);
                this.twitchClient.getEventManager().onEvent(RaidEvent.class, e -> displayRaidEventMessage(e, minecraftName));
                this.twitchClient.getEventManager().onEvent(GiftSubscriptionsEvent.class, e -> displayNewGiftSubscriptionEventMessage(e, minecraftName));
                this.twitchClient.getEventManager().onEvent(FollowingEvent.class, e -> Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("[" + ChatColor.DARK_PURPLE + "Twitch" + ChatColor.WHITE + " | " + ChatColor.GOLD + "NEW FOLLOW" + ChatColor.WHITE + "]" + ChatColor.DARK_GRAY + " » " + ChatColor.AQUA + e.getData().getUsername() + ChatColor.GREEN + " just followed you!!!"));
                this.twitchClient.getEventManager().onEvent(SubscriptionEvent.class, e -> displayNewSubscriptionEventMessage(e, minecraftName));
                this.twitchClient.getEventManager().onEvent(ChannelBitsEvent.class, e -> Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("[" + ChatColor.DARK_PURPLE + "Twitch" + ChatColor.WHITE + " | " + ChatColor.GOLD + "BITS" + ChatColor.WHITE + "]" + ChatColor.DARK_GRAY + " » " + ChatColor.AQUA + e.getData().getUserName() + ChatColor.GREEN + " donated " + ChatColor.GOLD + e.getData().getBitsUsed() + ChatColor.GREEN + " bits."));
                this.twitchClient.getEventManager().onEvent(DonationEvent.class, e -> Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("[" + ChatColor.DARK_PURPLE + "Twitch" + ChatColor.WHITE + " | " + ChatColor.GOLD + "DONATION" + ChatColor.WHITE + "]" + ChatColor.DARK_GRAY + " » " + ChatColor.AQUA + e.getUser().getName() + ChatColor.GREEN + " donated " + ChatColor.GOLD + e.getAmount() + ChatColor.GREEN + " dollars. Message:" + e.getMessage()));
                this.twitchClient.getEventManager().onEvent(ChannelGoLiveEvent.class, e -> broadcastGoLiveEvent(e, minecraftName));
            }

            Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage(ChatColor.GREEN + "Your TwitchBot is now enabled");
            if (Boolean.TRUE.equals(Thread.currentThread().isInterrupted())) {
                bot.stop();
            }

        });
    }

    public LatchBot getBot() {
        return this.bot;
    }

    public void setBot(LatchBot bot) {
        this.bot = bot;
    }

    /**
     * User Joins ChatChannel Event
     * @param event IRCMessageEvent
     */
    public void onChannnelClientJoinEvent(IRCMessageEvent event) {
        if(event.getCommandType().equals("JOIN") && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();

            // Dispatch Event
            if (channel != null && user != null) {
                Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("JOIN 1: " + event.getUserName());
                this.twitchClient.getEventManager().publish(new ChannelJoinEvent(channel, user));
                Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("JOIN 2: " + event.getUserName());

            }
        }
    }

    /**
     * User Leaves ChatChannel Event
     * @param event IRCMessageEvent
     */
    public void onChannnelClientLeaveEvent(IRCMessageEvent event) {
        if(event.getCommandType().equals("PART") && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();

            // Dispatch Event
            if (channel != null && user != null) {
                Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("LEAVE 1: " + event.getUserName());
                this.twitchClient.getEventManager().publish(new ChannelLeaveEvent(channel, user));
                Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("LEAVE 2: " + event.getUserName());
            }
        }
    }

    public void displayRaidEventMessage(RaidEvent e, String minecraftName){
        Bukkit.getScheduler().runTask(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(Constants.PLUGIN_NAME)), () -> playRaidSound(Objects.requireNonNull(Bukkit.getPlayer(minecraftName))));
        Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("[" + ChatColor.DARK_PURPLE + "Twitch" + ChatColor.WHITE + " | " + ChatColor.GOLD + "RAID" + ChatColor.WHITE + "] " + ChatColor.DARK_GRAY + " » " + ChatColor.GOLD + e.getRaider().getName() + ChatColor.AQUA + " raided your stream with " + ChatColor.GOLD + e.getViewers() + ChatColor.AQUA + " viewers!!!");
    }

    public void displayNewSubscriptionEventMessage(SubscriptionEvent e, String minecraftName){
        Bukkit.getScheduler().runTask(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(Constants.PLUGIN_NAME)), () -> playNewSubscriberSound(Objects.requireNonNull(Bukkit.getPlayer(minecraftName))));
        Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("[" + ChatColor.DARK_PURPLE + "Twitch" + ChatColor.WHITE + " | " + ChatColor.GOLD + "NEW SUB" + ChatColor.WHITE + "]" + ChatColor.DARK_GRAY + " » " + ChatColor.AQUA + e.getUser().getName() + ChatColor.GREEN + " just subscribed to you!!!");
    }

    public void displayNewGiftSubscriptionEventMessage(GiftSubscriptionsEvent e, String minecraftName){
        Bukkit.getScheduler().runTask(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(Constants.PLUGIN_NAME)), () -> playNewGiftSubscriberSound(Objects.requireNonNull(Bukkit.getPlayer(minecraftName))));
        Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("[" + ChatColor.DARK_PURPLE + "Twitch" + ChatColor.WHITE + " | " + ChatColor.GOLD + "NEW GIFT SUB" + ChatColor.WHITE + "]" + ChatColor.DARK_GRAY + " » " + ChatColor.AQUA + e.getUser().getName() + ChatColor.GREEN + " just gifted a subscription.!!!");
    }

    public void playNewSubscriberSound(Player player) {
        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1, 0);
    }

    public void playNewGiftSubscriberSound(Player player) {
        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_1, 1, 0);
    }
    public void playRaidSound(Player player) {
        player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 1, 0);
    }

    public void broadcastGoLiveEvent(ChannelGoLiveEvent e, String minecraftName){
        Bukkit.getScheduler().runTask(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(Constants.PLUGIN_NAME)), () ->  Bukkit.broadcastMessage(ChatColor.GOLD + minecraftName + ChatColor.GREEN + " just went live streaming " + ChatColor.GOLD + e.getStream().getGameName() + ChatColor.GREEN + ". Go watch them at -> " + ChatColor.GOLD + "https://www.twitch.tv/" + e.getChannel().getName()));
    }
    public void twitchChatResponseMessage(ChannelMessageEvent e) throws IOException {
        Objects.requireNonNull(Bukkit.getPlayer(minecraftName)).sendMessage("[" + ChatColor.DARK_PURPLE + "Twitch" + ChatColor.WHITE + " | " + ChatColor.GOLD + e.getUser().getName() + ChatColor.WHITE + "]" + ChatColor.DARK_GRAY + " » " + ChatColor.AQUA + e.getMessage());
    }

    public String getChannelID() {
        return this.channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public TwitchClient getTwitchClient() {
        return this.twitchClient;
    }

    public void setTwitchClient(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
    }

    public String getTwitchName() {
        return twitchName;
    }

    public void setTwitchName(String twitchName) {
        this.twitchName = twitchName;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }
}