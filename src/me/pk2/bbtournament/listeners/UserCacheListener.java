package me.pk2.bbtournament.listeners;

import me.pk2.bbtournament.api.db.GroupsAPI;
import me.pk2.bbtournament.config.def.ConfigMainDefault;
import me.pk2.bbtournament.scoreboard.ScoreboardH;
import me.pk2.bbtournament.user.User;
import me.pk2.bbtournament.game.GameStorage;
import me.pk2.bbtournament.game.state.GameState;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static me.pk2.bbtournament.config.def.ConfigLangDefault.*;
import static me.pk2.bbtournament.user.UserManager.*;
import static me.pk2.bbtournament.util.LoadUtils._ISEDIT;
import static me.pk2.bbtournament.util.LoadUtils._RUN1;
import static me.pk2.bbtournament.util.LoadUtils._UUID;
import static me.pk2.bbtournament.util.LoadUtils._LOG;

public class UserCacheListener implements Listener {
    public static final Executor executor = Executors.newFixedThreadPool(1);
    public static int playerCount = 0;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(_ISEDIT()) {
            if(!event.getPlayer().hasPermission("bbt.admin"))
                event.getPlayer().kickPlayer("You are not allowed to join this server.");

            return;
        }
        event.setJoinMessage(null);

        if(GameStorage.gameState != GameState.WAITING && GameStorage.gameState != GameState.STARTING) {
            event.getPlayer().kickPlayer("The game is already started.");
            return;
        }

        if(playerCount >= ConfigMainDefault.server.map.build_zone.size()) {
            event.getPlayer().kickPlayer("The server is full.");
            return;
        }

        executor.execute(() -> {
            User user = cache(event.getPlayer());
            user.load();

            _LOG("UserCacheListener", "Loaded user " + user.UUID + " from database!");
            _LOG("UserCacheListener", "User " + user.UUID + " has " + user.db().group_id + " group!");

            if(!ConfigMainDefault.server.group_required.equalsIgnoreCase("none") && !Objects.requireNonNull(GroupsAPI.getGroupName(user.db().group_id)).contentEquals(ConfigMainDefault.server.group_required)) {
                _RUN1(() -> user.player.kickPlayer("You are not allowed to join this server!"));
                return;
            }

            user.isPlaying = true;
            playerCount++;
            Bukkit.broadcastMessage(LANG.EVENT_BBT_PLAYER_JOIN.replaceAll("%player%", user.player.getName()));

            ScoreboardLib.createScoreboard(user.player).setHandler(new ScoreboardH().handler);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(_ISEDIT())
            return;
        event.setQuitMessage(null);

        executor.execute(() -> {
            if(users.containsKey(_UUID(event.getPlayer()))) {
                if(users.get(_UUID(event.getPlayer())).isPlaying) {
                    playerCount--;
                    Bukkit.broadcastMessage(LANG.EVENT_BBT_PLAYER_QUIT.replaceAll("%player%", event.getPlayer().getName()));
                }

                remove(event.getPlayer());
            }
            _LOG("UserCacheListener", "Removed user " + _UUID(event.getPlayer()) + " from cache!");
        });
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        onQuit(new PlayerQuitEvent(event.getPlayer(), event.getLeaveMessage()));
    }
}