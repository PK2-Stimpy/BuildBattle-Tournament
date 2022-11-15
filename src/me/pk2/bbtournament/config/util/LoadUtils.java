package me.pk2.bbtournament.config.util;

import static me.pk2.bbtournament.config.def.ConfigLangDefault.LANG;

import me.pk2.bbtournament.BuildBattleT;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

public class LoadUtils {
    public static World getWorldOrDefault(String worldName) {
        for(World w : Bukkit.getWorlds())
            if(w.getName().contentEquals(worldName))
                return w;
        return Bukkit.getWorlds().get(0);
    }

    public static String _CONFIG(String path) { return "plugins/BuildBattleTournament/" + path; }
    public static void _LOG(String prefix, String message) { BuildBattleT.INSTANCE.getLogger().info("[" + prefix + "] " + message); }
    public static void _LOG(String message) { BuildBattleT.INSTANCE.getLogger().info(message); }
    public static String _COLOR(String text) { return ChatColor.translateAlternateColorCodes('&', text); }
    public static String _PREFIX(String text) { return _COLOR(LANG.PREFIX + text); }
}