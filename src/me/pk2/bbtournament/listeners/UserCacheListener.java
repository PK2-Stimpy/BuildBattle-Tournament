package me.pk2.bbtournament.listeners;

import me.pk2.bbtournament.BuildBattleT;
import me.pk2.bbtournament.api.db.GroupsAPI;
import me.pk2.bbtournament.config.def.ConfigMainDefault;
import me.pk2.bbtournament.user.User;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static me.pk2.bbtournament.user.UserManager.*;
import static me.pk2.bbtournament.util.LoadUtils._UUID;
import static me.pk2.bbtournament.util.LoadUtils._LOG;

public class UserCacheListener implements Listener {
    public static final Executor executor = Executors.newFixedThreadPool(1);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        executor.execute(() -> {
            User user = cache(event.getPlayer());
            user.load();

            _LOG("UserCacheListener", "Loaded user " + user.UUID + " from database!");

            if(!ConfigMainDefault.server.group_required.equalsIgnoreCase("none") && !Objects.requireNonNull(GroupsAPI.getGroupName(user.db().group_id)).contentEquals(ConfigMainDefault.server.group_required))
                Bukkit.getScheduler().runTaskLater(BuildBattleT.INSTANCE, () -> user.player.kickPlayer("You are not allowed to join this server!"), 1L);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(users.containsKey(_UUID(event.getPlayer())))
            remove(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        onQuit(new PlayerQuitEvent(event.getPlayer(), event.getLeaveMessage()));
    }
}