package me.pk2.bbtournament.api.db.obj;

import java.util.HashMap;

import static me.pk2.bbtournament.api.db.GroupsAPI.*;

public class GroupDB {
    public final int id;
    public final String name;
    public String display;
    public GroupDB(int id) {
        this.id = id;
        this.name = getGroupName(id);
    }

    public void pull() {
        display = getGroupDisplay(id);
    }

    public void push() {
        setGroupDisplay(name, display);
    }

    /* STATIC */
    public static final HashMap<Integer, GroupDB> groups = new HashMap<>();
    public static GroupDB cache(int id) {
        if(groups.containsKey(id))
            return groups.get(id);

        GroupDB group = new GroupDB(id);
        group.pull();

        groups.put(id, group);
        return group;
    }
}