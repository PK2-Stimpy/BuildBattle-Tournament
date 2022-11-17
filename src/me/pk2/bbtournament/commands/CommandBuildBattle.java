package me.pk2.bbtournament.commands;

import me.pk2.bbtournament.api.db.GroupsAPI;
import me.pk2.bbtournament.config.def.ConfigMainDefault;
import me.pk2.bbtournament.config.def.obj.mode.ServerMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

import static me.pk2.bbtournament.config.def.ConfigMainDefault.server;
import static me.pk2.bbtournament.config.def.ConfigLangDefault.LANG;
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
                        "  &a/bb config save" +
                        "  &a/bb config mode ['PLAY'/'EDIT']\n" +
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
            case "config": {
                if(!sender.hasPermission("bbt.commands.buildbattle.admin")) {
                    sender.sendMessage(_PREFIX(LANG.COMMAND_NO_PERMISSION));
                    return true;
                }

                if(args.length < 2) {
                    sendHelpCommand(sender, 1);
                    return true;
                }

                switch(args[1].toLowerCase(Locale.ROOT)) {
                    case "save": {
                        ConfigMainDefault.save();
                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_SAVE));
                    } break;
                    case "mode": {
                        if(args.length < 3) {
                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MODE)
                                    .replaceAll("%mode%", server.mode.name()));
                            break;
                        }

                        switch(args[2].toUpperCase(Locale.ROOT)) {
                            case "PLAY": {
                                server.mode = ServerMode.PLAY;
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MODE_SET)
                                        .replaceAll("%mode%", server.mode.name()));
                            } break;

                            case "EDIT": {
                                server.mode = ServerMode.EDIT;
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MODE_SET)
                                        .replaceAll("%mode%", server.mode.name()));
                            } break;

                            default:
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MODE_INVALID));
                                break;
                        }
                    } break;
                    case "hub-server": {
                        if(args.length < 3) {
                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_HUB_SERVER)
                                    .replaceAll("%server%", server.hub_server));
                            break;
                        }

                        server.hub_server = args[2];
                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_HUB_SERVER_SET)
                                .replaceAll("%server%", server.hub_server));
                    } break;
                    case "group-required": {
                        if(args.length < 3) {
                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_GROUP_REQUIRED)
                                    .replaceAll("%group%", server.group_required));
                            break;
                        }

                        if(GroupsAPI.getGroupID(args[2]) == -1) {
                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_GROUP_INVALID));
                            break;
                        }

                        server.group_required = args[2];
                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_GROUP_REQUIRED_SET)
                                .replaceAll("%group%", server.group_required));
                    } break;
                    case "db": {
                        if(args.length < 3) {
                            sendHelpCommand(sender, 1);
                            break;
                        }

                        switch(args[2].toLowerCase(Locale.ROOT)) {
                            case "use": {
                                if(args.length < 4) {
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_USE)
                                            .replaceAll("%use%", String.valueOf(server.database.use)));
                                    break;
                                }

                                switch(args[3].toLowerCase(Locale.ROOT)) {
                                    case "remote": {
                                        server.database.use = "REMOTE";
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_USE_SET)
                                                .replaceAll("%use%", server.database.use));
                                    } break;

                                    case "local": {
                                        server.database.use = "LOCAL";
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_USE_SET)
                                                .replaceAll("%use%", server.database.use));
                                    } break;

                                    default:
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_USE_INVALID));
                                        break;
                                }
                            }
                            case "remote": {
                                if(args.length < 4) {
                                    sendHelpCommand(sender, 1);
                                    break;
                                }

                                switch(args[3].toLowerCase(Locale.ROOT)) {
                                    case "host": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_HOST)
                                                    .replaceAll("%host%", server.database.remote.host));
                                            break;
                                        }

                                        server.database.remote.host = args[4];
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_HOST_SET)
                                                .replaceAll("%host%", server.database.remote.host));
                                    } break;
                                    case "port": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_PORT)
                                                    .replaceAll("%port%", String.valueOf(server.database.remote.port)));
                                            break;
                                        }

                                        try {
                                            server.database.remote.port = Integer.parseInt(args[4]);
                                            if(server.database.remote.port < 1 || server.database.remote.port > 65535)
                                                throw new NumberFormatException();
                                        } catch(NumberFormatException e) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_PORT_INVALID));
                                            break;
                                        }

                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_PORT_SET)
                                                .replaceAll("%port%", String.valueOf(server.database.remote.port)));
                                    } break;
                                    case "username": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_USERNAME)
                                                    .replaceAll("%username%", server.database.remote.username));
                                            break;
                                        }

                                        server.database.remote.username = args[4];
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_USERNAME_SET)
                                                .replaceAll("%username%", server.database.remote.username));
                                    } break;
                                    case "password": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_PASSWORD_SECRET));
                                            break;
                                        }

                                        server.database.remote.password = args[4];
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_PASSWORD_SET));
                                    } break;
                                    case "schema": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_SCHEMA)
                                                    .replaceAll("%schema%", server.database.remote.schema));
                                            break;
                                        }

                                        server.database.remote.schema = args[4];
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_REMOTE_SCHEMA_SET)
                                                .replaceAll("%schema%", server.database.remote.schema));
                                    } break;

                                    default:
                                        sendHelpCommand(sender, 1);
                                        break;
                                }
                            } break;
                        }
                    } break;

                    default:
                        sendHelpCommand(sender, 1);
                        break;
                }
            } break;

            case "help": {
                if(args.length < 2) {
                    sendHelpCommand(sender, 1);
                    break;
                }

                try {
                    int page = Integer.parseInt(args[1]);
                    if(page < 1 || page > 3)
                        throw new NumberFormatException();

                    sendHelpCommand(sender, page);
                } catch (NumberFormatException e) {
                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_INVALID_PAGE));
                }
            } break;

            default:
                sendHelpCommand(sender, 1);
                break;
        }

        return true;
    }
}