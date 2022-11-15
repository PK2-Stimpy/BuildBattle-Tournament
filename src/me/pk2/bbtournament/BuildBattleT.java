package me.pk2.bbtournament;

import static me.pk2.bbtournament.util.LoadUtils._LOG;

import me.pk2.bbtournament.config.ConfigLoader;
import me.pk2.bbtournament.commands.CommandReload;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildBattleT extends JavaPlugin {
    public static BuildBattleT INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        _LOG("Loading config...");
        ConfigLoader.load();

        _LOG("Loading commands...");
        getCommand("bb-reload").setExecutor(new CommandReload());
        getCommand("buildbattle-reload").setExecutor(new CommandReload());

        _LOG("BuildBattleTournament has been enabled!");
    }

    @Override
    public void onDisable() {
        _LOG("BuildBattleTournament has been disabled!");
    }
}