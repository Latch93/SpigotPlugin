package Commands;

import LatchsTwitchBot.LatchTwitchBotRunnable;
import LatchsTwitchBot.Api.Api;
import LatchsTwitchBot.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jasypt.util.text.StrongTextEncryptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LatchsTwitchBotCommands implements CommandExecutor {
    public static List<LatchTwitchBotRunnable> twitchBotList = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        FileConfiguration twitchCfg = Api.loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME);
        try {
            if (args[0].equalsIgnoreCase(Constants.START_BOT_COMMAND)) {
                FileConfiguration cfg = Api.loadConfig(Constants.PLUGIN_CONFIG_FILE_NAME);
                if (Api.getTwitchUsername(player.getName()) != null) {
                    String twitchUsername = twitchCfg.getString(Constants.YML_PLAYERS + Api.getTwitchUsername(player.getName()).toLowerCase() + ".twitchUsername");
                    String minecraftUsername = twitchCfg.getString(Constants.YML_PLAYERS + Api.getTwitchUsername(player.getName()).toLowerCase() + ".minecraftUsername");
                    String oauthToken = twitchCfg.getString(Constants.YML_PLAYERS + Api.getTwitchUsername(player.getName()) + ".oauthToken");
                    StrongTextEncryptor oAuthDecrypter = new StrongTextEncryptor();
                    assert twitchUsername != null;
                    oAuthDecrypter.setPassword(twitchUsername);
                    String decryptedOAuth = oAuthDecrypter.decrypt(oauthToken);
                    String channelID = null;
                    if (twitchCfg.isSet(Constants.YML_PLAYERS + Api.getTwitchUsername(player.getName()) + ".channelID")) {
                        channelID = String.valueOf(twitchCfg.getInt(Constants.YML_PLAYERS + Api.getTwitchUsername(player.getName()) + ".channelID"));
                    }
                    assert oauthToken != null;
                    assert minecraftUsername != null;
                    LatchTwitchBotRunnable twitchBot = new LatchTwitchBotRunnable(twitchUsername, decryptedOAuth, minecraftUsername, channelID);
                    twitchBot.run();
                    twitchBotList.add(twitchBot);
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Invalid twitch bot credentials. Go to " + ChatColor.AQUA + "https://twitchapps.com/tmi/" + ChatColor.YELLOW + " and copy the oauth token. For more info, type " + ChatColor.AQUA + "/twitch help");
                }
            } else if (args[0].equalsIgnoreCase(Constants.STOP_BOT_COMMAND)) {
                Api.stopTwitchBot(twitchBotList, player);
            } else if (args[0].equalsIgnoreCase("stopall")) {
                Api.stopAllTwitchBots(twitchBotList);
                player.sendMessage(ChatColor.GREEN + "All TwitchBots have been " + ChatColor.RED + "terminated.");
            } else if (args[0].equalsIgnoreCase(Constants.HELP_COMMAND)) {
                player.sendMessage(ChatColor.WHITE + "1.) " + ChatColor.GREEN + "You need to get your Twitch oauth to use the Twitch bot.");
                player.sendMessage(ChatColor.WHITE + "2.) " + ChatColor.GREEN + "You can get your Twitch oauth from this link -> " + ChatColor.AQUA + "https://twitchapps.com/tmi/");
                player.sendMessage(ChatColor.WHITE + "3.) " + ChatColor.GREEN + "Copy the whole oauth token which includes the 'oauth:");
                player.sendMessage(ChatColor.WHITE + "4.) " + ChatColor.GREEN + "To add a Twitch bot, type " + ChatColor.AQUA + "/twitch addBot [twitchUsername] [oauthToken]");
                player.sendMessage(ChatColor.WHITE + "5.) " + ChatColor.GREEN + "To start your Twitch bot, type " + ChatColor.AQUA + "/twitch run");
                player.sendMessage(ChatColor.WHITE + "6.) " + ChatColor.GREEN + "Before you log off the server for the day, you should stop your Twitch bot.");
                player.sendMessage(ChatColor.WHITE + "7.) " + ChatColor.GREEN + "To do this, type " + ChatColor.AQUA + "/twitch stop");
                player.sendMessage(ChatColor.WHITE + "8.) " + ChatColor.GREEN + "You can download a Chrome extension to get your twitch channelId. It is a string of numbers. Example: 161082218");
                player.sendMessage(ChatColor.WHITE + "9.) " + ChatColor.GREEN + "If you want to get follow/subscriber event messages in minecraft, you need to add your channelID to your bot credentials.");
                player.sendMessage(ChatColor.WHITE + "10.) " + ChatColor.GREEN + "To add your channelID to your bot credentials, type " + ChatColor.AQUA + "/twitch addChannelId [channelId]");
            } else if (args[0].equalsIgnoreCase(Constants.SEND_TWITCH_MESSAGE_COMMAND)) {
                Iterator<LatchTwitchBotRunnable> iter = twitchBotList.iterator();
                StringBuilder messageString = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    messageString.append(args[i]).append(" ");
                }
                while (iter.hasNext()) {
                    LatchTwitchBotRunnable runBot = iter.next();
                    if (runBot.getMinecraftName().equalsIgnoreCase(player.getName())) {
                        runBot.getTwitchClient().getChat().sendMessage(runBot.getTwitchName(), String.valueOf(messageString));
                    }
                }
            } else if (args[0].equalsIgnoreCase(Constants.ADD_CHANNEL_ID)) {
                if (args[1] != null) {
                    twitchCfg.set(Constants.YML_PLAYERS + Api.getTwitchUsername(player.getName()) + ".channelID", args[1]);
                    twitchCfg.save(Api.getConfigFile(Constants.PLUGIN_CONFIG_FILE_NAME));
                    player.sendMessage(ChatColor.GREEN + "Your channelID has been added to your twitch bot credentials.");
                } else {
                    player.sendMessage(ChatColor.RED + "Must enter in a channelID. Use command like this -> " + ChatColor.AQUA + "/twitch addChannelId [channelId]");
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            player.sendMessage(ChatColor.RED + "Invalid command. Please type -> " + ChatColor.AQUA + "/twitch help");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static void addTwitchBotCredentials(String[] args, FileConfiguration twitchCfg, Player player) throws IOException {
        String twitchUsername = args[2].toLowerCase();
        twitchCfg.set(Constants.YML_PLAYERS + twitchUsername + ".twitchUsername", twitchUsername);
        twitchCfg.set(Constants.YML_PLAYERS + twitchUsername + ".minecraftUsername", player.getName().toLowerCase());
        StrongTextEncryptor oAuthEncrypter = new StrongTextEncryptor();
        oAuthEncrypter.setPassword(twitchUsername);
        String oAuthEncryptedText = oAuthEncrypter.encrypt(args[3].toLowerCase());
        twitchCfg.set(Constants.YML_PLAYERS + twitchUsername + ".oauthToken", oAuthEncryptedText);
        twitchCfg.save(Api.getConfigFile(Constants.PLUGIN_CONFIG_FILE_NAME));
        player.sendMessage(ChatColor.GREEN + "Your twitch bot credentials have been saved. Run " + ChatColor.AQUA + "/twitch start " + ChatColor.GREEN + "to connect to your twitch chat.");
        Api.messageInConsole(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " added their credentials for twitch bot.");
    }
}