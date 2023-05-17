package LatchsTwitchBot.TabComplete;

import LatchsTwitchBot.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LatchTwitchBotTabComplete implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> twitchBotCommandList = new ArrayList<>();
        if (args.length == 1) {
            twitchBotCommandList.add(Constants.ADD_BOT_COMMAND);
            twitchBotCommandList.add(Constants.STOP_BOT_COMMAND);
            twitchBotCommandList.add(Constants.START_BOT_COMMAND);
            twitchBotCommandList.add(Constants.HELP_COMMAND);
            twitchBotCommandList.add(Constants.SEND_TWITCH_MESSAGE_COMMAND);
            twitchBotCommandList.add(Constants.ADD_CHANNEL_ID);
        } else if (args.length == 2 && args[0].equalsIgnoreCase(Constants.ADD_BOT_COMMAND)) {
            twitchBotCommandList.add("[TwitchUsername]");
            return StringUtil.copyPartialMatches(args[1], twitchBotCommandList, new ArrayList<>());
        } else if (args.length == 2 && args[0].equalsIgnoreCase(Constants.ADD_CHANNEL_ID)) {
            twitchBotCommandList.add("[Channel ID]");
            return StringUtil.copyPartialMatches(args[1], twitchBotCommandList, new ArrayList<>());
        } else if (args.length == 3 && args[0].equalsIgnoreCase(Constants.ADD_BOT_COMMAND)) {
            twitchBotCommandList.add("[OAuth Token]");
            return StringUtil.copyPartialMatches(args[2], twitchBotCommandList, new ArrayList<>());
        }
        return StringUtil.copyPartialMatches(args[0], twitchBotCommandList, new ArrayList<>());
    }
}