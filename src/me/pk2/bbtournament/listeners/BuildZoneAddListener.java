package me.pk2.bbtournament.listeners;

import me.pk2.bbtournament.config.def.obj.BuildZoneObj;
import me.pk2.bbtournament.config.def.obj.vec.Vec23d;
import me.pk2.bbtournament.config.def.obj.vec.Vec3d;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

import static me.pk2.bbtournament.commands.CommandBuildBattle.AddBuildStatus.*;
import static me.pk2.bbtournament.commands.CommandBuildBattle.hashZone;
import static me.pk2.bbtournament.config.def.ConfigMainDefault.server;
import static me.pk2.bbtournament.config.def.ConfigLangDefault.LANG;
import static me.pk2.bbtournament.util.LoadUtils._ISEDIT;
import static me.pk2.bbtournament.util.LoadUtils._PREFIX;
import static me.pk2.bbtournament.util.LoadUtils._UUID;

public class BuildZoneAddListener implements Listener {
    public static final HashMap<String, BuildZoneObj> hashBuild = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(!_ISEDIT())
            return;

        Player player = event.getPlayer();
        if(hashZone.containsKey(_UUID(player))) {
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                hashZone.remove(_UUID(player));
                hashBuild.remove(_UUID(player));

                player.sendMessage(_PREFIX("&cCancelled."));
                event.setCancelled(true);
                return;
            }

            if(!event.getMessage().equalsIgnoreCase("ok"))
                return;
            switch (hashZone.get(_UUID(player))) {
                case STATUS_SPAWN: {
                    hashBuild.put(_UUID(player), new BuildZoneObj(player.getWorld(), new Vec3d(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()), null, null, null));

                    player.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_ASK_SPECSPAWN));
                    hashZone.put(_UUID(player), STATUS_SPECSPAWN);
                } break;
                case STATUS_SPECSPAWN: {
                    hashBuild.get(_UUID(player)).spectator_spawn = new Vec3d(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                    hashBuild.get(_UUID(player)).floor = new Vec23d(0, 0, 0, 0, 0, 0);

                    player.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_ASK_FLOOR1));
                    hashZone.put(_UUID(player), STATUS_FLOOR1);
                } break;
                case STATUS_FLOOR1: {
                    hashBuild.get(_UUID(player)).floor.x1 = player.getTargetBlock(null, 200).getX();
                    hashBuild.get(_UUID(player)).floor.y1 = player.getTargetBlock(null, 200).getY();
                    hashBuild.get(_UUID(player)).floor.z1 = player.getTargetBlock(null, 200).getZ();

                    player.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_ASK_FLOOR2));
                    hashZone.put(_UUID(player), STATUS_FLOOR2);
                } break;
                case STATUS_FLOOR2: {
                    hashBuild.get(_UUID(player)).floor.x2 = player.getTargetBlock(null, 200).getX();
                    hashBuild.get(_UUID(player)).floor.y2 = player.getTargetBlock(null, 200).getY();
                    hashBuild.get(_UUID(player)).floor.z2 = player.getTargetBlock(null, 200).getZ();

                    player.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_ASK_BUILD1));
                    hashZone.put(_UUID(player), STATUS_BUILD1);
                } break;
                case STATUS_BUILD1: {
                    hashBuild.get(_UUID(player)).build.x1 = player.getTargetBlock(null, 200).getX();
                    hashBuild.get(_UUID(player)).build.y1 = player.getTargetBlock(null, 200).getY();
                    hashBuild.get(_UUID(player)).build.z1 = player.getTargetBlock(null, 200).getZ();

                    player.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_ASK_BUILD2));
                    hashZone.put(_UUID(player), STATUS_BUILD2);
                } break;
                case STATUS_BUILD2: {
                    hashBuild.get(_UUID(player)).build.x2 = player.getTargetBlock(null, 200).getX();
                    hashBuild.get(_UUID(player)).build.y2 = player.getTargetBlock(null, 200).getY();
                    hashBuild.get(_UUID(player)).build.z2 = player.getTargetBlock(null, 200).getZ();

                    server.map.build_zone.add(hashBuild.get(_UUID(player)));

                    player.sendMessage(_PREFIX(LANG.COMMAND_BBT_CONFIG_MAP_BUILD_ZONE_SUCCESS));
                    hashZone.remove(_UUID(player));
                    hashBuild.remove(_UUID(player));
                } break;

                default:
                    break;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(!_ISEDIT())
            return;

        Player player = event.getPlayer();
        if(hashZone.containsKey(_UUID(player))) {
            hashZone.remove(_UUID(player));
            hashBuild.remove(_UUID(player));
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if(!_ISEDIT())
            return;

        Player player = event.getPlayer();
        if(hashZone.containsKey(_UUID(player))) {
            hashZone.remove(_UUID(player));
            hashBuild.remove(_UUID(player));
        }
    }
}