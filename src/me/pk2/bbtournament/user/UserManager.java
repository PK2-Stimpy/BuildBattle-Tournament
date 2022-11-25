package me.pk2.bbtournament.user;

import org.bukkit.entity.Player;

import java.util.HashMap;

import static me.pk2.bbtournament.util.LoadUtils._UUID;

public class UserManager {
    public static final HashMap<String, User> users = new HashMap<>();
    public static User cache(Player player) {
        if(users.containsKey(_UUID(player)))
            return users.get(_UUID(player));

        User user = new User(player);
        users.put(_UUID(player), user);
        return user;
    }

    public static void remove(Player player) {
        users.get(_UUID(player)).save();
        users.remove(_UUID(player));
    }
}