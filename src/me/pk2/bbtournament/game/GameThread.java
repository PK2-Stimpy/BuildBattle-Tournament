package me.pk2.bbtournament.game;

import me.pk2.bbtournament.config.def.ConfigMainDefault;
import me.pk2.bbtournament.game.state.GameState;
import me.pk2.bbtournament.listeners.UserCacheListener;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static me.pk2.bbtournament.config.def.ConfigMainDefault.server;
import static me.pk2.bbtournament.config.def.ConfigLangDefault.LANG;
import static me.pk2.bbtournament.util.LoadUtils._PREFIX;

public class GameThread {
    public static Thread thread;
    public static AtomicBoolean isRunning;
    public static AtomicInteger startTimer = new AtomicInteger(0);
    public static AtomicInteger gameTimer = new AtomicInteger(0);
    public static void thread() {
        while(isRunning.get()) {
            switch(GameStorage.gameState) {
                case WAITING: {
                    if (UserCacheListener.playerCount >= server.map.min_players) {
                        GameStorage.gameState = GameState.STARTING;

                        Bukkit.broadcastMessage(_PREFIX(LANG.EVENT_BBT_WAITING_STARTING
                                .replaceAll("%time%", String.valueOf(server.map.time.start))));
                        startTimer.set(server.map.time.start);
                    }
                } break;
                case STARTING: {
                    if (startTimer.get() == 0) {
                        if (UserCacheListener.playerCount < server.map.min_players) {
                            GameStorage.gameState = GameState.WAITING;
                            Bukkit.broadcastMessage(_PREFIX(LANG.EVENT_BBT_STARTING_WAITING));

                            break;
                        }

                        GameStorage.gameState = GameState.INGAME;
                        Bukkit.broadcastMessage(_PREFIX(LANG.EVENT_BBT_STARTING_INGAME));

                        gameTimer.set(server.map.time.game);
                        /* TODO:
                            Actually start the game,
                            select a random topic,
                            tp players to their respective building zone,
                            start the timer,
                            and play a cool sound.
                        * */
                        break;
                    }

                    if ((startTimer.get() > 0 && startTimer.get() <= 5) || startTimer.get() == 30 || startTimer.get() == 60) {
                        Bukkit.broadcastMessage(_PREFIX(LANG.EVENT_BBT_STARTING_COUNTDOWN
                                .replaceAll("%time%", String.valueOf(startTimer.get()))));
                        // TODO: Make cool sounds.
                    }

                    startTimer.decrementAndGet();
                } break;
                case INGAME: {
                    if(gameTimer.get() == 0) {
                        GameStorage.gameState = GameState.VOTING;
                        Bukkit.broadcastMessage(_PREFIX(LANG.EVENT_BBT_INGAME_VOTING));

                        /* TODO:
                            Switch players to spectator mode,
                            teleport them to the voting area,
                            and start the voting timer.
                        * */
                        break;
                    }

                    if ((gameTimer.get() > 0 && gameTimer.get() <= 5) || gameTimer.get() == 30 || gameTimer.get() == 60) {
                        Bukkit.broadcastMessage(_PREFIX(LANG.EVENT_BBT_INGAME_COUNTDOWN
                                .replaceAll("%time%", String.valueOf(gameTimer.get()))));
                        // TODO: Make cool sounds.
                    }

                    gameTimer.decrementAndGet();
                } break;
                // TODO: Make rest of the game states.
                default:
                    break;
            }

            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public static void initThread() {
        isRunning = new AtomicBoolean(true);

        thread = new Thread(thread);
        thread.start();
    }
}