package me.pk2.bbtournament.api.db.obj;

import java.util.HashMap;

import static me.pk2.bbtournament.api.db.UsersAPI.*;

public class UserDB {
    public final int id;
    public final String UUID;
    public int group_id;
    public UserDB(int id) {
        this.id = id;
        this.UUID = getUUIDById(id);
    }

    public void pull() {
        group_id = getUserGroupId(UUID);
    }

    public void push() {
        setUserGroup(UUID, group_id);
    }

    /* STATIC */
    public static final HashMap<Integer, UserDB> users = new HashMap<>();
    public static UserDB cache(int id) {
        if(users.containsKey(id))
            return users.get(id);

        UserDB user = new UserDB(id);
        user.pull();

        return users.put(id, user);
    }
}