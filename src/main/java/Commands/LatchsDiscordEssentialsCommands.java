package Commands;

import LatchEssentials.Api.Api;
import LatchEssentials.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class LatchsDiscordEssentialsCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        FileConfiguration cfg = Api.loadConfig(Constants.LATCHS_DISCORD_ESSENTIALS_COMMAND);

        return false;
    }

}