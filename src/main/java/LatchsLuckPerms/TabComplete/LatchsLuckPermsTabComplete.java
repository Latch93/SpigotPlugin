package LatchsLuckPerms.TabComplete;

import LatchsLuckPerms.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LatchsLuckPermsTabComplete implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> latchsLuckPermsCommandList = new ArrayList<>();
        latchsLuckPermsCommandList.add(Constants.GRANT_COMMAND);
        latchsLuckPermsCommandList.add(Constants.CHECK_GROUPS_COMMAND);
        latchsLuckPermsCommandList.add(Constants.REVOKE_COMMAND);
        return StringUtil.copyPartialMatches(args[0], latchsLuckPermsCommandList, new ArrayList<>());
    }
}