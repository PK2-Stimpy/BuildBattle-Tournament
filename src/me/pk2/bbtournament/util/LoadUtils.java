package me.pk2.bbtournament.util;

import static me.pk2.bbtournament.config.def.ConfigLangDefault.LANG;

import me.pk2.bbtournament.BuildBattleT;
import me.pk2.bbtournament.config.def.ConfigMainDefault;
import me.pk2.bbtournament.config.def.obj.mode.ServerMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
    public static String _UUID(Player player) { return player.getUniqueId().toString().toLowerCase(); }
    public static void _RUN1(Runnable runnable) { Bukkit.getScheduler().runTaskLater(BuildBattleT.INSTANCE, runnable, 1L); }
    public static boolean _ISEDIT() { return(ConfigMainDefault.server.mode == ServerMode.EDIT); }
}