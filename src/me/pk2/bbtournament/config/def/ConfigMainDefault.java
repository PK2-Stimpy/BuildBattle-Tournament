package me.pk2.bbtournament.config.def;

import static me.pk2.bbtournament.util.LoadUtils.*;

import me.pk2.bbtournament.BuildBattleT;
import me.pk2.bbtournament.config.def.obj.BuildZoneObj;
import me.pk2.bbtournament.config.def.obj.WinEventDoObj;
import me.pk2.bbtournament.config.def.obj.action.WinEventAction;
import me.pk2.bbtournament.config.def.obj.mode.ServerMode;
import me.pk2.bbtournament.config.def.obj.vec.Vec23d;
import me.pk2.bbtournament.config.def.obj.vec.Vec3d;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigMainDefault {
    public static YamlConfiguration CONFIG = null;

    public static String version;
    public static class server {
        public static ServerMode mode;
        public static String hub_server, group_required;
        public static class database {
            public static String use;
            public static class remote {
                public static String host;
                public static int port;
                public static String username, password, schema;
            }
            public static class local {
                public static String path;
            }
        }
        public static class scoreboard {
            public static String
                    network_name,
                    server_name,
                    server_ip;
        }
        public static class map {
            public static String name;
            public static String world;
            public static int min_players;
            public static class win_event {
                public static int positions;
                public static List<WinEventDoObj> _do;
            }
            public static class time {
                public static int
                        start,
                        game,
                        vote,
                        end;
            }
            public static List<BuildZoneObj> build_zone;
            public static List<String> topics;
        }
    }

    public static void saveDefault() {
        _LOG("config.yml", "Saving default config.yml...");

        BuildBattleT.INSTANCE.getDataFolder().mkdirs();

        PrintWriter writer;
        try {
            writer = new PrintWriter(_CONFIG("config.yml"), "UTF-8");
            InputStream is = ConfigMainDefault.class.getClassLoader().getResourceAsStream("config.yml");
            int i;
            while(true) {
                assert is != null;
                if ((i = is.read()) == -1)
                    break;

                writer.write(i);
            }

            writer.close();
            is.close();
        } catch (Exception e) { e.printStackTrace(); }

        _LOG("config.yml", "Saved default config.yml!");
    }

    public static void load() {
        _LOG("config.yml", "Loading config.yml...");

        File file = new File(_CONFIG("config.yml"));
        if(!file.exists()) {
            _LOG("config.yml", "config.yml does not exist! Creating default config.yml...");
            try {
                file.createNewFile();
                saveDefault();
            } catch (Exception exception) { exception.printStackTrace(); }
        }

        version = BuildBattleT.INSTANCE.getDescription().getVersion();
        CONFIG = YamlConfiguration.loadConfiguration(file);
        if(CONFIG.getString("version") == null || !CONFIG.getString("version").contentEquals(version)) {
            try {
                saveDefault();
                CONFIG = YamlConfiguration.loadConfiguration(file);
            } catch (Exception exception) { exception.printStackTrace(); }
        }

        server.mode = ServerMode.get(CONFIG.getString("server.mode"));
        server.hub_server = CONFIG.getString("server.hub_server");
        server.group_required = CONFIG.getString("server.group_required");

        server.database.use = CONFIG.getString("server.database.use");
        server.database.remote.host = CONFIG.getString("server.database.remote.host");
        server.database.remote.port = CONFIG.getInt("server.database.remote.port");
        server.database.remote.username = CONFIG.getString("server.database.remote.username");
        server.database.remote.password = CONFIG.getString("server.database.remote.password");
        server.database.remote.schema = CONFIG.getString("server.database.remote.schema");
        server.database.local.path = CONFIG.getString("server.database.local.path");

        server.scoreboard.network_name = CONFIG.getString("server.scoreboard.network_name");
        server.scoreboard.server_name = CONFIG.getString("server.scoreboard.server_name");
        server.scoreboard.server_ip = CONFIG.getString("server.scoreboard.server_ip");

        server.map.name = CONFIG.getString("server.map.name");
        server.map.world = CONFIG.getString("server.map.world");
        server.map.min_players = CONFIG.getInt("server.map.min_players");

        server.map.win_event.positions = CONFIG.getInt("server.map.win_event.positions");
        server.map.win_event._do = new ArrayList<>();
        for(String key : CONFIG.getConfigurationSection("server.map.win_event.do").getKeys(false)) {
            _LOG("config.yml", "Loading win event do " + key + "...");

            ConfigurationSection section = CONFIG.getConfigurationSection("server.map.win_event.do." + key);
            server.map.win_event._do.add(new WinEventDoObj(
                    WinEventAction.get(section.getString("action")),
                    section.getString("value")
            ));
        }

        server.map.time.start = CONFIG.getInt("server.map.time.start");
        server.map.time.game = CONFIG.getInt("server.map.time.game");
        server.map.time.vote = CONFIG.getInt("server.map.time.vote");
        server.map.time.end = CONFIG.getInt("server.map.time.end");

        _LOG("config.yml", "Map name " + server.map.name);

        server.map.build_zone = new ArrayList<>();
        List<Double> floor_listX, floor_listY, floor_listZ, build_listX, build_listY, build_listZ;
        for(String s : CONFIG.getConfigurationSection("server.map.build_zone").getKeys(false)) {
            _LOG("config.yml", "Loading build zone " + s + "...");

            ConfigurationSection section = CONFIG.getConfigurationSection("server.map.build_zone." + s);
            floor_listX = section.getDoubleList("floor.x");
            floor_listY = section.getDoubleList("floor.y");
            floor_listZ = section.getDoubleList("floor.z");
            build_listX = section.getDoubleList("build.x");
            build_listY = section.getDoubleList("build.y");
            build_listZ = section.getDoubleList("build.z");

            server.map.build_zone.add(new BuildZoneObj(
                    getWorldOrDefault(section.getString("world")),
                    new Vec3d(
                            section.getDouble("spawn.x"),
                            section.getDouble("spawn.y"),
                            section.getDouble("spawn.z")
                    ),
                    new Vec3d(
                            section.getDouble("spectator_spawn.x"),
                            section.getDouble("spectator_spawn.y"),
                            section.getDouble("spectator_spawn.z")
                    ),
                    new Vec23d(
                            floor_listX.get(0), floor_listX.get(1),
                            floor_listY.get(0), floor_listY.get(1),
                            floor_listZ.get(0), floor_listZ.get(1)
                    ),
                    new Vec23d(
                            build_listX.get(0), build_listX.get(1),
                            build_listY.get(0), build_listY.get(1),
                            build_listZ.get(0), build_listZ.get(1)
                    )
            ));

            floor_listX.clear();
            floor_listY.clear();
            floor_listZ.clear();
            build_listX.clear();
            build_listY.clear();
            build_listZ.clear();
        }

        server.map.topics = new ArrayList<>();
        server.map.topics.addAll(CONFIG.getStringList("server.map.topics"));

        _LOG("config.yml", "Topics size: " + server.map.topics.size());

        floor_listX = null;
        floor_listY = null;
        floor_listZ = null;
        build_listX = null;
        build_listY = null;
        build_listZ = null;

        _LOG("config.yml", "Loaded config.yml!");
    }

    public static void save() {
        _LOG("config.yml", "Saving config.yml...");

        CONFIG.set("version", version);

        CONFIG.set("server.mode", server.mode.name());
        CONFIG.set("server.hub_server", server.hub_server);
        CONFIG.set("server.group_required", server.group_required);

        CONFIG.set("server.database.use", server.database.use);
        CONFIG.set("server.database.remote.host", server.database.remote.host);
        CONFIG.set("server.database.remote.port", server.database.remote.port);
        CONFIG.set("server.database.remote.username", server.database.remote.username);
        CONFIG.set("server.database.remote.password", server.database.remote.password);
        CONFIG.set("server.database.remote.schema", server.database.remote.schema);
        CONFIG.set("server.database.local.path", server.database.local.path);
        
        CONFIG.set("server.scoreboard.network_name", server.scoreboard.network_name);
        CONFIG.set("server.scoreboard.server_name", server.scoreboard.server_name);
        CONFIG.set("server.scoreboard.server_ip", server.scoreboard.server_ip);
        
        CONFIG.set("server.map.name", server.map.name);
        CONFIG.set("server.map.world", server.map.world);
        CONFIG.set("server.map.min_players", server.map.min_players);

        CONFIG.set("server.map.win_event.positions", server.map.win_event.positions);
        for(int i = 0; i < server.map.win_event._do.size(); i++) {
            _LOG("config.yml", "Saving win event do " + i + "...");

            CONFIG.set("server.map.win_event.do." + i + ".action", server.map.win_event._do.get(i).action.name());
            CONFIG.set("server.map.win_event.do." + i + ".value", server.map.win_event._do.get(i).value);
        }

        CONFIG.set("server.map.time.start", server.map.time.start);
        CONFIG.set("server.map.time.game", server.map.time.game);
        CONFIG.set("server.map.time.vote", server.map.time.vote);
        CONFIG.set("server.map.time.end", server.map.time.end);
        
        CONFIG.set("server.map.build_zone", null);
        for(int i = 0; i < server.map.build_zone.size(); i++) {
            _LOG("config.yml", "Saving build zone " + i + "...");

            BuildZoneObj obj = server.map.build_zone.get(i);
            CONFIG.set("server.map.build_zone." + i + ".world", obj.world.getName());
            CONFIG.set("server.map.build_zone." + i + ".spawn.x", obj.spawn.x);
            CONFIG.set("server.map.build_zone." + i + ".spawn.y", obj.spawn.y);
            CONFIG.set("server.map.build_zone." + i + ".spawn.z", obj.spawn.z);
            CONFIG.set("server.map.build_zone." + i + ".spectator_spawn.x", obj.spectator_spawn.x);
            CONFIG.set("server.map.build_zone." + i + ".spectator_spawn.y", obj.spectator_spawn.y);
            CONFIG.set("server.map.build_zone." + i + ".spectator_spawn.z", obj.spectator_spawn.z);
            CONFIG.set("server.map.build_zone." + i + ".floor.x", Arrays.asList(obj.floor.x1, obj.floor.x2));
            CONFIG.set("server.map.build_zone." + i + ".floor.y", Arrays.asList(obj.floor.y1, obj.floor.y2));
            CONFIG.set("server.map.build_zone." + i + ".floor.z", Arrays.asList(obj.floor.z1, obj.floor.z2));
            CONFIG.set("server.map.build_zone." + i + ".build.x", Arrays.asList(obj.build.x1, obj.build.x2));
            CONFIG.set("server.map.build_zone." + i + ".build.y", Arrays.asList(obj.build.y1, obj.build.y2));
            CONFIG.set("server.map.build_zone." + i + ".build.z", Arrays.asList(obj.build.z1, obj.build.z2));
        }
        
        CONFIG.set("server.map.topics", server.map.topics);

        _LOG("config.yml", "Topics size: " + server.map.topics.size());
        _LOG("config.yml", "Saved config.yml!");
    }
}