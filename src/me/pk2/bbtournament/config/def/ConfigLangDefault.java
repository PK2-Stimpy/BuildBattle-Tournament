package me.pk2.bbtournament.config.def;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;

import static me.pk2.bbtournament.util.LoadUtils._CONFIG;
import static me.pk2.bbtournament.util.LoadUtils._LOG;

public class ConfigLangDefault {
    public static class LANG {
        public static String _version;
        public static String
                PREFIX,
                COMMAND_NO_PERMISSION,
                COMMAND_MUST_BE_PLAYER,
                COMMAND_RELOAD_SUCCESS,
                COMMAND_EXIT,
                COMMAND_BBT_INVALID_PAGE,
                COMMAND_BBT_CONFIG_SAVE,
                COMMAND_BBT_CONFIG_MODE,
                COMMAND_BBT_CONFIG_MODE_SET,
                COMMAND_BBT_CONFIG_MODE_INVALID,
                COMMAND_BBT_CONFIG_HUB_SERVER,
                COMMAND_BBT_CONFIG_HUB_SERVER_SET,
                COMMAND_BBT_CONFIG_GROUP_REQUIRED,
                COMMAND_BBT_CONFIG_GROUP_REQUIRED_SET,
                COMMAND_BBT_CONFIG_GROUP_INVALID,
                COMMAND_BBT_CONFIG_DB_USE,
                COMMAND_BBT_CONFIG_DB_USE_SET,
                COMMAND_BBT_CONFIG_DB_USE_INVALID,
                COMMAND_BBT_CONFIG_DB_REMOTE_HOST,
                COMMAND_BBT_CONFIG_DB_REMOTE_HOST_SET,
                COMMAND_BBT_CONFIG_DB_REMOTE_PORT,
                COMMAND_BBT_CONFIG_DB_REMOTE_PORT_SET,
                COMMAND_BBT_CONFIG_DB_REMOTE_PORT_INVALID,
                COMMAND_BBT_CONFIG_DB_REMOTE_USERNAME,
                COMMAND_BBT_CONFIG_DB_REMOTE_USERNAME_SET,
                COMMAND_BBT_CONFIG_DB_REMOTE_PASSWORD_SECRET,
                COMMAND_BBT_CONFIG_DB_REMOTE_PASSWORD_SET,
                COMMAND_BBT_CONFIG_DB_REMOTE_SCHEMA,
                COMMAND_BBT_CONFIG_DB_REMOTE_SCHEMA_SET,
                COMMAND_BBT_CONFIG_DB_LOCAL_PATH,
                COMMAND_BBT_CONFIG_DB_LOCAL_PATH_SET,
                COMMAND_BBT_CONFIG_SCORE_NETWORKNAME,
                COMMAND_BBT_CONFIG_SCORE_NETWORKNAME_SET,
                COMMAND_BBT_CONFIG_SCORE_SERVERNAME,
                COMMAND_BBT_CONFIG_SCORE_SERVERNAME_SET,
                COMMAND_BBT_CONFIG_SCORE_SERVERIP,
                COMMAND_BBT_CONFIG_SCORE_SERVERIP_SET,
                COMMAND_BBT_CONFIG_MAP_NAME,
                COMMAND_BBT_CONFIG_MAP_NAME_SET,
                COMMAND_BBT_CONFIG_MAP_WORLD,
                COMMAND_BBT_CONFIG_MAP_WORLD_SET,
                COMMAND_BBT_CONFIG_MAP_MINPLAYERS,
                COMMAND_BBT_CONFIG_MAP_MINPLAYERS_SET,
                COMMAND_BBT_CONFIG_MAP_WIN_EVENT_LIST,
                COMMAND_BBT_CONFIG_MAP_WIN_EVENT_POSITIONS,
                COMMAND_BBT_CONFIG_MAP_WIN_EVENT_POSITIONS_SET,
                COMMAND_BBT_CONFIG_MAP_WIN_EVENT_ADD,
                COMMAND_BBT_CONFIG_MAP_WIN_EVENT_REM_INVALID_INDEX,
                COMMAND_BBT_CONFIG_MAP_WIN_EVENT_REM,
                COMMAND_BBT_CONFIG_MAP_TIME_START,
                COMMAND_BBT_CONFIG_MAP_TIME_START_SET,
                COMMAND_BBT_CONFIG_MAP_TIME_GAME,
                COMMAND_BBT_CONFIG_MAP_TIME_GAME_SET,
                COMMAND_BBT_CONFIG_MAP_TIME_VOTE,
                COMMAND_BBT_CONFIG_MAP_TIME_VOTE_SET,
                COMMAND_BBT_CONFIG_MAP_TIME_END,
                COMMAND_BBT_CONFIG_MAP_TIME_END_SET;
    }

    public static YamlConfiguration CONFIG;

    public static void saveDefault() {
        _LOG("lang.yml", "Saving default lang.yml...");

        PrintWriter writer;
        try {
            writer = new PrintWriter(_CONFIG("lang.yml"), "UTF-8");
            InputStream is = ConfigMainDefault.class.getClassLoader().getResourceAsStream("lang.yml");
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

        _LOG("lang.yml", "Saved default lang.yml!");
    }

    public static void load() {
        _LOG("lang.yml", "Loading lang.yml...");

        File file = new File(_CONFIG("lang.yml"));
        if (!file.exists()) {
            _LOG("lang.yml", "lang.yml does not exist! Creating default lang.yml...");
            try {
                file.createNewFile();
                saveDefault();
            } catch (Exception e) { e.printStackTrace(); }
        }

        CONFIG = YamlConfiguration.loadConfiguration(file);
        for(String key : CONFIG.getKeys(false))
            try {
                LANG.class.getDeclaredField(key).set(null, CONFIG.getString(key));
                _LOG("lang.yml", "Loaded " + key + "!");
            } catch (NoSuchFieldException e) {
                _LOG("lang.yml", "lang.yml has an invalid key: " + key);
            } catch (IllegalAccessException ignored) {}

        if(!LANG._version.contentEquals(ConfigMainDefault.version)) {
            _LOG("lang.yml", "lang.yml is outdated! Creating default lang.yml...");
            saveDefault();
            load();

            return;
        }

        _LOG("lang.yml", "Loaded lang.yml!");
    }
}