package me.pk2.bbtournament.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

import static me.pk2.bbtournament.util.LoadUtils._PREFIX;
import static me.pk2.bbtournament.util.LoadUtils._COLOR;

public class CommandBuildBattle implements CommandExecutor {
    private void sendHelpCommand(CommandSender sender, int page) {
        sender.sendMessage(_COLOR("" +
                "&7&m--------------------\n" +
                "  &a/exit\n"));
        if(!sender.hasPermission("bbt.commands.buildbattle.admin")) {
            sender.sendMessage("&7--------------------");
            return;
        }

        switch(page) {
            case 1:
                sender.sendMessage(_COLOR("" +
                        "  &a/bb-reload" +
                        "  &a/bb help [page]\n" +
                        "  &a/bb config save\n" +
                        "  &a/bb config hub-server [server]\n" +
                        "  &a/bb config group-required [group]\n" +
                        "  &a/bb config db use <'remote' / 'local'>\n" +
                        "  &a/bb config db remote host [host]\n" +
                        "  &a/bb config db remote port [port]\n" +
                        "  &a/bb config db remote username [username]\n" +
                        "  &a/bb config db remote password <password>\n" +
                        "  &a/bb config db remote schema [schema]"));
                break;
            case 2:
                sender.sendMessage(_COLOR("" +
                        "  &a/bb config db local path [path]\n" +
                        "  &a/bb config score network_name [name]\n" +
                        "  &a/bb config score server_name [name]\n" +
                        "  &a/bb config score server_ip [ip]\n" +
                        "  &a/bb config map name [name]\n" +
                        "  &a/bb config map world ['show']\n" +
                        "  &a/bb config map min_players [players]\n" +
                        "  &a/bb config map win_event list" +
                        "  &a/bb config map win_event positions [pos]\n" +
                        "  &a/bb config map win_event get <idx>"));
                break;
            case 3:
            default:
                sender.sendMessage(_COLOR("" +
                        "  &a/bb config map win_event add <action> <value>\n" +
                        "  &a/bb config map win_event rem <idx>\n" +
                        "  &a/bb config map time start [sec]\n" +
                        "  &a/bb config map time game [sec]\n" +
                        "  &a/bb config map time vote [sec]\n" +
                        "  &a/bb config map time end [sec]\n" +
                        "  &a/bb config map build_zone list\n" +
                        "  &a/bb config map build_zone add\n" +
                        "  &a/bb config map build_zone rem\n" +
                        "  &a/bb config map build_zone get <idx>\n" +
                        "  &a/bb config topics [set/rem] [topic]\n"));
                break;
        }

        sender.sendMessage(_COLOR("&7------&e" + page + "/3&7-------------"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) {
            sendHelpCommand(sender, 1);
            return true;
        }

        switch(args[0].toLowerCase(Locale.ROOT)) {
            case "help": {
                if(args.length < 2) {
                    sendHelpCommand(sender, 1);
                    return true;
                }

                try {
                    int page = Integer.parseInt(args[1]);
                    sendHelpCommand(sender, page);
                } catch (NumberFormatException e) {
                    sender.sendMessage(_PREFIX("&cInvalid page number!"));
                }
            } break;

            default:
                sendHelpCommand(sender, 1);
                return true;
        }

        return true;
    }
}