package me.pk2.bbtournament.api.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static me.pk2.bbtournament.util.LoadUtils._LOG;
import static me.pk2.bbtournament.api.DatabaseAPI.*;

public class GroupsAPI {
    public static void newGroup(String name, String display) {
        prepare();
        if(isStringInsecure(name) || isStringInsecure(display))
            return;

        try {
            connection.prepareStatement("INSERT INTO `groups` (name, display) VALUES ('" + name + "', '" + display + "')").executeUpdate();
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static void deleteGroup(String name) {
        prepare();
        if(isStringInsecure(name))
            return;

        try {
            connection.prepareStatement("DELETE FROM `groups` WHERE name = '" + name + "'").executeUpdate();
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static void setGroupDisplay(String name, String display) {
        prepare();
        if(isStringInsecure(name) || isStringInsecure(display))
            return;

        try {
            connection.prepareStatement("UPDATE `groups` SET display = '" + display + "' WHERE name = '" + name + "'").executeUpdate();
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static int getGroupID(String name) {
        prepare();
        if(isStringInsecure(name))
            return -1;

        try {
            return connection.prepareStatement("SELECT id FROM `groups` WHERE name = '" + name + "'").executeQuery().getInt("id");
        } catch (Exception exception) { exception.printStackTrace(); }
        return -1;
    }

    public static String getGroupName(int id) {
        prepare();
        if(id < 0)
            return null;

        try {
            return connection.prepareStatement("SELECT * FROM `groups` WHERE id = " + id).executeQuery().getString("name");
        } catch (Exception exception) { exception.printStackTrace(); }
        return null;
    }

    public static String getGroupDisplay(int id) {
        prepare();
        if(id < 0)
            return null;

        try {
            return connection.prepareStatement("SELECT * FROM `groups` WHERE id = " + id).executeQuery().getString("display");
        } catch (Exception exception) { exception.printStackTrace(); }
        return null;
    }

    public static String getGroupDisplay(String name) { return getGroupDisplay(getGroupID(name)); }

    public static List<Integer> getGroups() {
        _LOG("GroupsAPI", "Started getGroups transaction");
        prepare();

        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM `groups`");
            List<Integer> list = new ArrayList<>();
            while(set.next())
                list.add(set.getInt("id"));
            _LOG("GroupsAPI", "Finished getGroups transaction with " + list.size() + " results");
            return list;
        } catch (Exception exception) { exception.printStackTrace(); }

        return null;
    }
}