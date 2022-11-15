package me.pk2.bbtournament.commands;

import static me.pk2.bbtournament.config.def.ConfigLangDefault.LANG;
import static me.pk2.bbtournament.util.LoadUtils._PREFIX;

import me.pk2.bbtournament.config.ConfigLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("bbt.command.reload")) {
                player.sendMessage(_PREFIX(LANG.COMMAND_NO_PERMISSION));
                return true;
            }
        }

        ConfigLoader.load();
        sender.sendMessage(_PREFIX(LANG.COMMAND_RELOAD_SUCCESS));
        return true;
    }
}