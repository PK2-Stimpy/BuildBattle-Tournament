package me.pk2.bbtournament.scoreboard;

import me.pk2.bbtournament.game.GameStorage;
import me.pk2.bbtournament.listeners.UserCacheListener;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static me.pk2.bbtournament.config.def.ConfigMainDefault.server;
import static me.pk2.bbtournament.util.LoadUtils._COLOR;

public class ScoreboardH {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    public static ScoreboardHandler handler = new ScoreboardHandler() {
        @Override
        public String getTitle(Player player) {
            return _COLOR(server.scoreboard.network_name);
        }

        @Override
        public List<Entry> getEntries(Player player) {
            // TODO: Actually make this work.
            String countdownMstr = "", countdownSstr = "";
            String countdownVMstr = "", countdownVSstr = "";
            String countdownEMstr = "", countdownESstr = "";
            String builder = "";
            String winners = "";
            String topic = "";

            switch(GameStorage.gameState) {
                case WAITING:
                    return new EntryBuilder()
                            .next(_COLOR("&7BB" + server.scoreboard.server_name + " " + dateFormat.format(new Date(System.currentTimeMillis()))))
                            .blank()
                            .next(_COLOR("&rWaiting for players..."))
                            .blank()
                            .next(_COLOR("&rPlayers: &a" + UserCacheListener.playerCount))
                            .blank()
                            .next(_COLOR("&e" + server.scoreboard.server_ip))
                            .build();
                case STARTING:
                    return new EntryBuilder()
                            .next(_COLOR("&7BB" + server.scoreboard.server_name + " " + dateFormat.format(new Date(System.currentTimeMillis()))))
                            .blank()
                            .next(_COLOR("&rNext Event"))
                            .next(_COLOR("&aStart " + countdownMstr + ":" + countdownSstr))
                            .blank()
                            .next(_COLOR("&rPlayers: &a" + UserCacheListener.playerCount))
                            .blank()
                            .next(_COLOR("&e" + server.scoreboard.server_ip))
                            .build();
                case INGAME:
                    return new EntryBuilder()
                            .next(_COLOR("&7BB" + server.scoreboard.server_name + " " + dateFormat.format(new Date(System.currentTimeMillis()))))
                            .blank()
                            .next(_COLOR("&rNext Event"))
                            .next(_COLOR("&aVote " + countdownVMstr + ":" + countdownVSstr))
                            .blank()
                            .next(_COLOR("&rTopic: &a" + topic))
                            .blank()
                            .next(_COLOR("&rPlayers: &a" + UserCacheListener.playerCount))
                            .blank()
                            .next(_COLOR("&e" + server.scoreboard.server_ip))
                            .build();
                case VOTING:
                    return new EntryBuilder()
                            .next(_COLOR("&7BB" + server.scoreboard.server_name + " " + dateFormat.format(new Date(System.currentTimeMillis()))))
                            .blank()
                            .next(_COLOR("&rNext Event"))
                            .next(_COLOR("&aEnd " + countdownEMstr + ":" + countdownESstr))
                            .blank()
                            .next(_COLOR("&rBuilder: &a" + builder))
                            .next(_COLOR("&rTopic: &a" + topic))
                            .blank()
                            .next(_COLOR("&rPlayers: &a" + UserCacheListener.playerCount))
                            .blank()
                            .next(_COLOR("&e" + server.scoreboard.server_ip))
                            .build();
                case ENDING:
                    return new EntryBuilder()
                            .next(_COLOR("&7BB" + server.scoreboard.server_name + " " + dateFormat.format(new Date(System.currentTimeMillis()))))
                            .blank()
                            .next(_COLOR("&rNext Event"))
                            .next(_COLOR("&aLobby " + countdownEMstr + ":" + countdownESstr))
                            .blank()
                            .next(_COLOR("&rBuilder: &a" + builder))
                            .next(_COLOR("&rTopic: &a" + topic))
                            .blank()
                            .next(_COLOR("&rWinners: &a" + winners))
                            .blank()
                            .next(_COLOR("&rPlayers: &a" + UserCacheListener.playerCount))
                            .blank()
                            .next(_COLOR("&e" + server.scoreboard.server_ip))
                            .build();
            }

            return new EntryBuilder()
                    .blank()
                    .blank()
                    .blank()
                    .blank()
                    .build();
        }
    };
}