package me.pk2.bbtournament.commands;

import me.pk2.bbtournament.api.bungee.BungeeAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.pk2.bbtournament.config.def.ConfigMainDefault.server;
import static me.pk2.bbtournament.config.def.ConfigLangDefault.LANG;
import static me.pk2.bbtournament.util.LoadUtils._PREFIX;
import static me.pk2.bbtournament.BuildBattleT.INSTANCE;

public class CommandExit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(_PREFIX(LANG.COMMAND_MUST_BE_PLAYER));
            return true;
        }

        Player player = (Player)sender;
        player.sendMessage(_PREFIX(LANG.COMMAND_EXIT));

        Bukkit.getScheduler().runTaskLater(INSTANCE, () -> BungeeAPI.sendPlayerToServer(player, server.hub_server), 60L);
        return true;
    }
}