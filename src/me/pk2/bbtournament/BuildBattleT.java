package me.pk2.bbtournament;

import static me.pk2.bbtournament.util.LoadUtils._LOG;

import me.pk2.bbtournament.api.DatabaseAPI;
import me.pk2.bbtournament.api.db.GroupsAPI;
import me.pk2.bbtournament.api.db.UsersAPI;
import me.pk2.bbtournament.commands.CommandExit;
import me.pk2.bbtournament.config.ConfigLoader;
import me.pk2.bbtournament.commands.CommandReload;
import me.pk2.bbtournament.listeners.BuildZoneAddListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BuildBattleT extends JavaPlugin {
    public static BuildBattleT INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        _LOG("Registering bungee channels...");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        _LOG("Loading config...");
        ConfigLoader.load();

        _LOG("Connecting to database...");
        DatabaseAPI.connect();

        List<Integer> users = UsersAPI.getUserList();
        if(users == null || users.size() == 0)
            _LOG("No users found in database!");
        else _LOG("Found " + users.size() + " users in database!");

        List<Integer> groups = GroupsAPI.getGroups();
        if(groups == null || groups.size() == 0)
            _LOG("No groups found in database!");
        else _LOG("Found " + groups.size() + " groups in database!");

        _LOG("Loading commands...");
        getCommand("bb-reload").setExecutor(new CommandReload());
        getCommand("exit").setExecutor(new CommandExit());

        _LOG("Loading listeners...");
        getServer().getPluginManager().registerEvents(new BuildZoneAddListener(), this);

        _LOG("BuildBattleTournament has been enabled!");
    }

    @Override
    public void onDisable() {
        _LOG("Disconnecting database...");
        if(DatabaseAPI.connection != null)
            DatabaseAPI.disconnect();

        _LOG("BuildBattleTournament has been disabled!");
    }
}