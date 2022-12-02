package me.pk2.bbtournament.user;

import me.pk2.bbtournament.api.db.obj.UserDB;
import me.pk2.bbtournament.config.def.obj.BuildZoneObj;
import org.bukkit.entity.Player;

import static me.pk2.bbtournament.config.def.ConfigMainDefault.server;
import static me.pk2.bbtournament.util.LoadUtils._UUID;
import static me.pk2.bbtournament.api.db.UsersAPI.*;

public class User {
    public final String UUID;
    public Player player;
    public boolean isPlaying;
    public int buildZoneIdx;
    private UserDB userDB;

    public User(Player player) {
        this.isPlaying = false;
        this.buildZoneIdx = -1;

        this.UUID = _UUID(player);
        this.player = player;
    }

    public UserDB db() { return userDB; }

    public void load() {
        if(getUserId(UUID) == -1)
            newUser(UUID);

        userDB = UserDB.cache(UUID);
        userDB.pull();
    }

    public void save() {
        userDB.push();
    }
    public BuildZoneObj getBuildZone() {
        if(buildZoneIdx == -1)
            return null;
        return server.map.build_zone.get(buildZoneIdx);
    }
}