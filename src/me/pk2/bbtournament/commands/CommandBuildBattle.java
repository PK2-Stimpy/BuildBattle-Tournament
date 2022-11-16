package me.pk2.bbtournament.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

import static me.pk2.bbtournament.util.LoadUtils._PREFIX;
import static me.pk2.bbtournament.util.LoadUtils._COLOR;

public class CommandBuildBattle implements CommandExecutor {
    private void sendHelpCommand(CommandSender sender) {
        sender.sendMessage(_COLOR("" +
                "&7&m--------------------\n" +
                "  &a/exit\n"));
        if(!sender.hasPermission("bbt.commands.buildbattle.admin")) {
            sender.sendMessage("&7&m--------------------");
            return;
        }

        sender.sendMessage(_COLOR("" +
                "  &a/bb-reload\n" +
                "  &a/bb config save\n" +
                "  &a/bb config hub-server [server]\n" +
                "  &a/bb config group-required [group]\n" +
                "  &a/bb config db use <'remote' / 'local'>\n" +
                "  &a/bb config db remote host [host]\n" +
                "  &a/bb config db remote port [port]\n" +
                "  &a/bb config db remote username [username]\n" +
                "  &a/bb config db remote password <password>\n" +
                "  &a/bb config db remote schema [schema]\n" +
                "  &a/bb config db local path [path]\n" +
                "  &a/bb config score network_name [name]\n" +
                "  &a/bb config score server_name [name]\n" +
                "  &a/bb config score server_ip [ip]\n" +
                "  &a/bb config map name [name]\n" +
                "  &a/bb config map world ['show']\n" +
                "  &a/bb config map min_players [players]\n" +
                "  &a/bb config map win_event positions [pos]\n" +
                "  &a/bb config map win_event get <idx>\n" +
                "  &a/bb config map win_event add <action> <value>\n" +
                "  &a/bb config map win_event rem <idx>\n" +
                "&7&m--------------------"));

        // TODO: Add more commands here when I'm back home
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) {
            sendHelpCommand(sender);
            return true;
        }

        switch(args[0].toLowerCase(Locale.ROOT)) {


            default:
                sendHelpCommand(sender);
                return true;
        }
        return true;
    }
}