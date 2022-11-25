package me.pk2.bbtournament.listeners;

import me.pk2.bbtournament.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.pk2.bbtournament.user.UserManager.*;
import static me.pk2.bbtournament.util.LoadUtils._UUID;

public class UserCacheListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        User user = cache(event.getPlayer());
        user.load();
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