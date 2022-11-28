package me.pk2.bbtournament.user;

import me.pk2.bbtournament.listeners.UserCacheListener;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static me.pk2.bbtournament.util.LoadUtils._UUID;

public class UserManager {
    public static final HashMap<String, User> users = new HashMap<>();
    public static User cache(Player player) {
        if(users.containsKey(_UUID(player))) {
            User user = users.get(_UUID(player));
            user.player = player;

            return user;
        }

        User user = new User(player);
        users.put(_UUID(player), user);
        return user;
    }

    public static void remove(Player player) {
        String uuid = _UUID(player);

        UserCacheListener.executor.execute(() -> {
            users.get(uuid).save();
            users.remove(uuid);
        });
    }
}