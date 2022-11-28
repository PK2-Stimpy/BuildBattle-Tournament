package me.pk2.bbtournament.api.db.obj;

import java.util.HashMap;

import static me.pk2.bbtournament.api.db.UsersAPI.*;

public class UserDB {
    public int id;
    public final String UUID;
    public int group_id;
    public UserDB(String uuid) {
        this.id = getUserId(uuid);
        this.UUID = uuid;
    }

    public void pull() {
        id = getUserId(UUID);
        group_id = getUserGroupId(UUID);
    }

    public void push() {
        setUserGroup(UUID, group_id);
    }

    /* STATIC */
    public static final HashMap<String, UserDB> users = new HashMap<>();
    public static UserDB cache(String uuid) {
        if(users.containsKey(uuid))
            return users.get(uuid);

        UserDB user = new UserDB(uuid);
        user.pull();
        users.put(uuid, user);

        return user;
    }
}