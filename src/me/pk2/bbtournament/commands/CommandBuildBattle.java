package me.pk2.bbtournament.commands;

import me.pk2.bbtournament.api.db.GroupsAPI;
import me.pk2.bbtournament.api.db.UsersAPI;
import me.pk2.bbtournament.api.db.obj.GroupDB;
import me.pk2.bbtournament.api.db.obj.UserDB;
import me.pk2.bbtournament.config.def.ConfigMainDefault;
import me.pk2.bbtournament.config.def.obj.BuildZoneObj;
import me.pk2.bbtournament.config.def.obj.WinEventDoObj;
import me.pk2.bbtournament.config.def.obj.action.WinEventAction;
import me.pk2.bbtournament.config.def.obj.mode.ServerMode;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static me.pk2.bbtournament.config.def.ConfigMainDefault.server;
import static me.pk2.bbtournament.config.def.ConfigLangDefault.LANG;
import static me.pk2.bbtournament.util.LoadUtils.*;
import static me.pk2.bbtournament.commands.CommandBuildBattle.AddBuildStatus.*;

public class CommandBuildBattle implements CommandExecutor {
    public static final HashMap<String, AddBuildStatus> hashZone = new HashMap<>();
    public static final Executor executor = Executors.newFixedThreadPool(1);
    public static enum AddBuildStatus {
        STATUS_SPAWN,
        STATUS_SPECSPAWN,
        STATUS_FLOOR1,
        STATUS_FLOOR2,
        STATUS_BUILD1,
        STATUS_BUILD2}

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
                        "  &a/bb-reload\n" +
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
                        "  &a/bb config map world [world]\n" +
                        "  &a/bb config map min_players [players]\n" +
                        "  &a/bb config map win_event list" +
                        "  &a/bb config map win_event positions [pos]\n"));
                break;
            case 3:
                sender.sendMessage(_COLOR("" +
                        "  &a/bb config map win_event add <action> <value>\n" +
                        "  &a/bb config map win_event rem <idx>\n" +
                        "  &a/bb config map time start [sec]\n" +
                        "  &a/bb config map time game [sec]\n" +
                        "  &a/bb config map time vote [sec]\n" +
                        "  &a/bb config map time end [sec]\n" +
                        "  &a/bb config map build_zone list\n" +
                        "  &a/bb config map build_zone add\n" +
                        "  &a/bb config map build_zone rem <idx>\n" +
                        "  &a/bb config topics [add/rem] [topic]\n"));
                break;
            case 4:
            default:
                sender.sendMessage(_COLOR("" +
                        "  &a/bb db group list\n" +
                        "  &a/bb db group add <group> <display>\n" +
                        "  &a/bb db group rem <group>\n" +
                        "  &a/bb db group set <group> <display>\n" +
                        "  &a/bb db group user <user> [group]\n"));
                break;
        }

        sender.sendMessage(_COLOR("&7------&e" + page + "/4&7-------------"));
    }

    private void spacedArray(String[] arr, int sid, int lid) {
        StringBuilder b = new StringBuilder();
        for(int i = lid; i < arr.length; i++)
            b.append(arr[i] + ((i==arr.length-1)?"":" "));

        arr[sid] = b.toString();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        executor.execute(() -> threadedCommand(sender, command, label, args));
        return true;
    }

    public void threadedCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) {
            sendHelpCommand(sender, 1);
            return;
        }

        switch(args[0].toLowerCase(Locale.ROOT)) {
            case "config": {
                if(!sender.hasPermission("bbt.commands.buildbattle.admin")) {
                    sender.sendMessage(_PREFIX(LANG.COMMAND_NO_PERMISSION));
                    return;
                }

                if(args.length < 2) {
                    sendHelpCommand(sender, 1);
                    return;
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
                            case "local": {
                                if(args.length < 4) {
                                    sendHelpCommand(sender, 1);
                                    break;
                                }

                                if ("path".contentEquals(args[3].toLowerCase(Locale.ROOT))) {
                                    if (args.length < 5) {
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_LOCAL_PATH)
                                                .replaceAll("%path%", server.database.local.path));
                                        break;
                                    }

                                    server.database.local.path = args[4];
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_DB_LOCAL_PATH_SET)
                                            .replaceAll("%path%", server.database.local.path));
                                } else sendHelpCommand(sender, 2);
                            } break;
                        }
                    } break;
                    case "score": {
                        if(args.length < 3) {
                            sendHelpCommand(sender, 2);
                            break;
                        }

                        switch(args[2].toLowerCase()) {
                            case "network_name": {
                                if(args.length < 4) {
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_SCORE_NETWORKNAME)
                                            .replaceAll("%network_name%", server.scoreboard.network_name));
                                    break;
                                }

                                spacedArray(args, 3, 4);

                                server.scoreboard.network_name = args[3];
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_SCORE_NETWORKNAME_SET)
                                        .replaceAll("%network_name%", server.scoreboard.network_name));
                            } break;
                            case "server_name": {
                                if(args.length < 4) {
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_SCORE_SERVERNAME)
                                            .replaceAll("%server_name%", server.scoreboard.server_name));
                                    break;
                                }

                                server.scoreboard.server_name = args[3];
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_SCORE_SERVERNAME_SET)
                                        .replaceAll("%server_name%", server.scoreboard.server_name));
                            } break;
                            case "server_ip": {
                                if(args.length < 4) {
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_SCORE_SERVERIP)
                                            .replaceAll("%server_ip%", server.scoreboard.server_ip));
                                    break;
                                }

                                server.scoreboard.server_ip = args[3];
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_SCORE_SERVERIP_SET)
                                        .replaceAll("%server_ip%", server.scoreboard.server_ip));
                            } break;

                            default:
                                sendHelpCommand(sender, 2);
                                break;
                        }
                    } break;
                    case "map": {
                        if(args.length < 3) {
                            sendHelpCommand(sender, 2);
                            break;
                        }

                        switch(args[2].toLowerCase()) {
                            case "name": {
                                if(args.length < 4) {
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_NAME)
                                            .replaceAll("%map_name%", server.map.name));
                                    break;
                                }

                                spacedArray(args, 3, 4);

                                server.map.name = args[3];
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_NAME_SET)
                                        .replaceAll("%map_name%", server.map.name));
                            } break;
                            case "world": {
                                if(args.length < 4) {
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_WORLD)
                                            .replaceAll("%map_world%", server.map.world));
                                    break;
                                }

                                spacedArray(args, 3, 3);

                                server.map.world = args[3];
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_WORLD_SET)
                                        .replaceAll("%map_world%", server.map.world));
                            } break;
                            case "min_players": {
                                if(args.length < 4) {
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_MINPLAYERS)
                                            .replaceAll("%min_players%", String.valueOf(server.map.min_players)));
                                    break;
                                }

                                server.map.min_players = Integer.parseInt(args[3]);
                                sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_MINPLAYERS_SET)
                                        .replaceAll("%min_players%", String.valueOf(server.map.min_players)));
                            } break;
                            case "win_event": {
                                if(args.length < 4) {
                                    sendHelpCommand(sender, 2);
                                    break;
                                }

                                switch(args[3].toLowerCase(Locale.ROOT)) {
                                    case "list": {
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_WIN_EVENT_LIST));

                                        WinEventDoObj event;
                                        for(int i = 0; i < server.map.win_event._do.size(); i++) {
                                            event = server.map.win_event._do.get(i);

                                            sender.sendMessage(" " + i + ". " + event.action.name() + "(\"" + event.value + "\")");
                                        }
                                    } break;
                                    case "positions": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_WIN_EVENT_POSITIONS)
                                                    .replaceAll("%positions%", String.valueOf(server.map.win_event.positions)));
                                            break;
                                        }

                                        server.map.win_event.positions = Integer.parseInt(args[4]);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_WIN_EVENT_POSITIONS_SET)
                                                .replaceAll("%positions%", String.valueOf(server.map.win_event.positions)));
                                    } break;
                                    case "add": {
                                        if(args.length < 6) {
                                            sendHelpCommand(sender, 3);
                                            break;
                                        }

                                        spacedArray(args, 4, 4);

                                        WinEventDoObj event = new WinEventDoObj(WinEventAction.get(args[4].toUpperCase(Locale.ROOT)), args[5]);

                                        server.map.win_event._do.add(event);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_WIN_EVENT_ADD)
                                                .replaceAll("%action%", event.action.name())
                                                .replaceAll("%value%", event.value));
                                    } break;
                                    case "rem": {
                                        if(args.length < 5) {
                                            sendHelpCommand(sender, 3);
                                            break;
                                        }

                                        int index = Integer.parseInt(args[4]);
                                        if(index < 0 || index >= server.map.win_event._do.size()) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_WIN_EVENT_REM_INVALID_INDEX));
                                            break;
                                        }

                                        server.map.win_event._do.remove(index);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_WIN_EVENT_REM)
                                                .replaceAll("%index%", String.valueOf(index)));
                                    } break;

                                    default:
                                        sendHelpCommand(sender, 2);
                                        break;
                                }
                            } break;
                            case "time": {
                                if(args.length < 4) {
                                    sendHelpCommand(sender, 3);
                                    break;
                                }

                                switch(args[3].toLowerCase(Locale.ROOT)) {
                                    case "start": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TIME_START)
                                                    .replaceAll("%start_time%", String.valueOf(server.map.time.start)));
                                            break;
                                        }

                                        server.map.time.start = Integer.parseInt(args[4]);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TIME_START_SET)
                                                .replaceAll("%start_time%", String.valueOf(server.map.time.start)));
                                    } break;
                                    case "game": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TIME_GAME)
                                                    .replaceAll("%game_time%", String.valueOf(server.map.time.game)));
                                            break;
                                        }

                                        server.map.time.game = Integer.parseInt(args[4]);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TIME_GAME_SET)
                                                .replaceAll("%game_time%", String.valueOf(server.map.time.game)));
                                    } break;
                                    case "vote": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TIME_VOTE)
                                                    .replaceAll("%vote_time%", String.valueOf(server.map.time.vote)));
                                            break;
                                        }

                                        server.map.time.vote = Integer.parseInt(args[4]);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TIME_VOTE_SET)
                                                .replaceAll("%vote_time%", String.valueOf(server.map.time.vote)));
                                    } break;
                                    case "end": {
                                        if(args.length < 5) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TIME_END)
                                                    .replaceAll("%end_time%", String.valueOf(server.map.time.end)));
                                            break;
                                        }

                                        server.map.time.end = Integer.parseInt(args[4]);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TIME_END_SET)
                                                .replaceAll("%end_time%", String.valueOf(server.map.time.end)));
                                    } break;

                                    default:
                                        sendHelpCommand(sender, 3);
                                        break;
                                }
                            } break;
                            case "build_zone": {
                                if(args.length < 4) {
                                    sendHelpCommand(sender, 3);
                                    break;
                                }

                                switch(args[3].toLowerCase(Locale.ROOT)) {
                                    case "list": {
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_LIST));

                                        BuildZoneObj zone;
                                        for(int i = 0; i < server.map.build_zone.size(); i++) {
                                            zone = server.map.build_zone.get(i);
                                            sender.sendMessage(" - " + i + "{" +
                                                    "SP=" + zone.spawn + ", " +
                                                    "SS=" + zone.spectator_spawn + ", " +
                                                    "FL=" + zone.floor + ", " +
                                                    "BU=" + zone.build + ", " +
                                                    "WO=" + zone.world.getName() + "}");
                                        }
                                    } break;
                                    case "add": {
                                        if(!(sender instanceof Player)) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_PLAYER_ONLY));
                                            break;
                                        }
                                        Player player = (Player) sender;
                                        if(hashZone.containsKey(_UUID(player))) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_ALREADY_ADDING));
                                            break;
                                        }

                                        hashZone.put(_UUID(player), STATUS_SPAWN);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_ASK_SPAWN));
                                    } break;
                                    case "rem": {
                                        if(args.length < 5) {
                                            sendHelpCommand(sender, 3);
                                            break;
                                        }

                                        int index = Integer.parseInt(args[4]);
                                        if(index < 0 || index >= server.map.build_zone.size()) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_REM_INVALID_INDEX));
                                            break;
                                        }

                                        server.map.build_zone.remove(index);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_REM)
                                                .replaceAll("%index%", String.valueOf(index)));
                                    } break;

                                    default:
                                        sendHelpCommand(sender, 3);
                                        break;
                                }
                            } break;
                            case "topics": {
                                if(args.length < 4) {
                                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TOPICS)
                                            .replaceAll("%topics%", String.join(", ", server.map.topics)));
                                    break;
                                }

                                switch(args[3].toLowerCase(Locale.ROOT)) {
                                    case "add": {
                                        if(args.length < 5) {
                                            sendHelpCommand(sender, 3);
                                            break;
                                        }

                                        spacedArray(args, 4, 4);

                                        String topic = args[4];
                                        if(server.map.topics.contains(topic)) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TOPICS_ADD_ALREADY_EXIST)
                                                    .replaceAll("%topic%", topic));
                                            break;
                                        }

                                        server.map.topics.add(topic);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TOPICS_ADD)
                                                .replaceAll("%topic%", topic));
                                    } break;
                                    case "rem": {
                                        if(args.length < 5) {
                                            sendHelpCommand(sender, 3);
                                            break;
                                        }

                                        spacedArray(args, 4, 4);

                                        String topic = args[4];
                                        if(!server.map.topics.contains(topic)) {
                                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TOPICS_REM_NOT_EXIST)
                                                    .replaceAll("%topic%", topic));
                                            break;
                                        }

                                        server.map.topics.remove(topic);
                                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_TOPICS_REM)
                                                .replaceAll("%topic%", topic));
                                    } break;

                                    default:
                                        sendHelpCommand(sender, 3);
                                        break;
                                }
                            } break;

                            default:
                                sendHelpCommand(sender, 2);
                                break;
                        }
                    } break;

                    default:
                        sendHelpCommand(sender, 1);
                        break;
                }
            } break;
            case "db": {
                if(!sender.hasPermission("bbt.commands.buildbattle.admin")) {
                    sender.sendMessage(_PREFIX(LANG.COMMAND_NO_PERMISSION));
                    return;
                }

                if(args.length < 3) {
                    sendHelpCommand(sender, 4);
                    return;
                }

                if(!args[1].equalsIgnoreCase("group")) {
                    sendHelpCommand(sender, 4);
                    return;
                }

                switch(args[2].toLowerCase(Locale.ROOT)) {
                    case "list": {
                        List<Integer> list = GroupsAPI.getGroups();
                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_GROUP_LIST)
                                .replaceAll("%groups%", String.valueOf(list.size())));

                        for(int i = 0; i < Objects.requireNonNull(list).size(); i++) {
                            int id = list.get(i);

                            GroupDB group = GroupDB.cache(id);
                            group.pull();

                            sender.sendMessage(_PREFIX("&aGroup " + i + ": Name=&e'" + group.name + "'&a, Display=&e'" + group.display + "'"));
                        }
                    } break;
                    case "add": {
                        if(args.length < 5) {
                            sendHelpCommand(sender, 4);
                            break;
                        }

                        _LOG("BuildBatteCommand", "Array[4]: " + args[4]);
                        spacedArray(args, 4, 4);
                        _LOG("BuildBattleCommand", "Array[3]: " + args[3]);
                        _LOG("BuildBatteCommand", "Array spaced[4]: " + args[4]);

                        String name = args[3];
                        String display = args[4];
                        GroupsAPI.newGroup(name, display);

                        if(GroupsAPI.getGroupID(name) == -1) {
                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_ADD_FAILED));
                            break;
                        }

                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_ADD_SUCCESS));
                    } break;
                    case "rem": {
                        if(args.length < 4) {
                            sendHelpCommand(sender, 4);
                            break;
                        }

                        String name = args[3];
                        int id = GroupsAPI.getGroupID(name);
                        if(id == -1) {
                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_REM_NOT_EXIST));
                            break;
                        }

                        GroupsAPI.deleteGroup(name);
                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_REM_SUCCESS));
                    } break;
                    case "set": {
                        if(args.length < 5) {
                            sendHelpCommand(sender, 4);
                            break;
                        }

                        String name = args[3];
                        int id = GroupsAPI.getGroupID(name);
                        if(id == -1) {
                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_SET_NOT_EXIST));
                            break;
                        }

                        spacedArray(args, 4, 4);

                        GroupDB group = GroupDB.cache(id);
                        group.display = args[4];
                        group.push();

                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_SET_SUCCESS));
                    } break;
                    case "user": {
                        if(args.length < 4) {
                            sendHelpCommand(sender, 4);
                            break;
                        }

                        UserDB user = UserDB.cache(_UUID(Bukkit.getPlayer(args[3])));
                        user.pull();
                        if(args.length < 5) {
                            GroupDB group = GroupDB.cache(user.group_id);

                            sender.sendMessage(_PREFIX("&aUser " + args[2] + ": Group=&e" + group.name));
                            break;
                        }

                        String group = args[4];
                        int group_id = GroupsAPI.getGroupID(group);
                        if(group_id == -1) {
                            sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_USER_GROUP_NOT_EXIST));
                            break;
                        }

                        user.group_id = group_id;
                        user.push();

                        sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_DB_USER_GROUP_SET));
                    } break;

                    default:
                        sendHelpCommand(sender, 4);
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
                    if(page < 1)
                        throw new NumberFormatException();

                    sendHelpCommand(sender, page);
                } catch (Exception e) {
                    sender.sendMessage(_PREFIX(LANG.COMMAND_BBT_INVALID_PAGE));
                }
            } break;

            default:
                sendHelpCommand(sender, 1);
                break;
        }
    }
}