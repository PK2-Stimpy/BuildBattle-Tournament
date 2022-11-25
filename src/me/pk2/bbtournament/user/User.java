package me.pk2.bbtournament.user;

import me.pk2.bbtournament.api.db.obj.UserDB;
import org.bukkit.entity.Player;

import static me.pk2.bbtournament.util.LoadUtils._UUID;
import static me.pk2.bbtournament.api.db.UsersAPI.*;

public class User {
    public final String UUID;
    public final Player player;
    private UserDB userDB;

    public User(Player player) {
        this.UUID = _UUID(player);
        this.player = player;
    }

    public UserDB db() { return userDB; }

    public void load() {
        if(getUserId(UUID) == -1)
            newUser(UUID);

        userDB = UserDB.cache(getUserId(UUID));
        userDB.pull();
    }

    public void save() {
        userDB.push();
    }
}