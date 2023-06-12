package LatchEssentials.TabComplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LatchsDiscordEssentialsTabComplete implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> latchsDiscordEssentialsCommandList = new ArrayList<>();
        return StringUtil.copyPartialMatches(args[0], latchsDiscordEssentialsCommandList, new ArrayList<>());
    }
}