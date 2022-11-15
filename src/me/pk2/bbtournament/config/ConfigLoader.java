package me.pk2.bbtournament.config;

import me.pk2.bbtournament.config.def.ConfigLangDefault;
import me.pk2.bbtournament.config.def.ConfigMainDefault;

public class ConfigLoader {
    public static void load() {
        ConfigMainDefault.load();
        ConfigLangDefault.load();
    }

    public static void save() {
        ConfigMainDefault.save();
    }
}