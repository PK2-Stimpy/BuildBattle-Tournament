package me.pk2.bbtournament.config.def;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;

import static me.pk2.bbtournament.config.util.LoadUtils._CONFIG;
import static me.pk2.bbtournament.config.util.LoadUtils._LOG;

public class ConfigLangDefault {
    public static class LANG {
        public static String _version;
        public static String PREFIX, COMMAND_NO_PERMISSION, COMMAND_RELOAD_SUCCESS;
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