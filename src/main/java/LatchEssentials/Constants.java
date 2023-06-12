package LatchEssentials;

import LatchEssentials.Api.Api;

public class Constants {
    public static final String PLUGIN_NAME = "LatchEssentials";
    public static final String PLUGIN_CONFIG_FILE_NAME = "config";
    public static final String LATCHS_LUCK_PERMS_CONFIG_FILE_NAME = "permissions";
    public static final String LATCHS_DISCORD_WHITELIST_FILE_NAME = "whitelist";
    public static final String ENABLED_EVENTS_CONFIG_FILE_NAME = "enabledEvents";
    public static final String DISCORD_MEMBER_ROLE_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("discordMemberRoleID");

    public static final String LATCHS_LUCK_PERMS_COMMAND = "llp";
    public static final String LUCK_PERMS_MOD_GROUP = Api.loadConfig(LATCHS_LUCK_PERMS_CONFIG_FILE_NAME).getString("modLuckPermsGroup");
    public static final String LUCK_PERMS_ADMIN_GROUP = Api.loadConfig(LATCHS_LUCK_PERMS_CONFIG_FILE_NAME).getString("adminLuckPermsGroup");
    public static final String LATCHS_DISCORD_ESSENTIALS_COMMAND = "lde";

    public static final String ENABLE_LATCH_DISCORD_KEY = "enableLatchDiscord";
    public static final String GRANT_COMMAND = "grant";
    public static final String REVOKE_COMMAND = "revoke";
    public static final String CHECK_GROUPS_COMMAND = "groups";
    public static final String YML_PLAYERS = "players.";
    public static final String PLAYERS = "players";
    public static final String DISCORD_BOT_TOKEN = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("botToken");

    public static final String MINECRAFT_CHAT_DISCORD_CHANNEL_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("minecraftChatDiscordChannelID");
    public static final String MINECRAFT_SERVER_USER_JOIN_DISCORD_CHANNEL_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("userJoinDiscordChannelID");
    public static final String MINECRAFT_SERVER_USER_LEAVE_DISCORD_CHANNEL_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("userLeaveDiscordChannelID");
    public static final String MINECRAFT_SERVER_STAFF_DISCORD_CHANNEL_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("staffDiscordChannelID");
    public static final String MINECRAFT_SERVER_ADMIN_DISCORD_CHANNEL_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("adminDiscordChannelID");
    public static final String USER_LEAVES_DISCORD_CHANNEL_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("userLeaveDiscordChannelID");

    public static final String DISCORD_SERVER_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("discordServerID");
    public static final String LATCH_BOT_DISCORD_USER_ID = Api.loadConfig(PLUGIN_CONFIG_FILE_NAME).getString("latchBotDiscordUserID");




}
