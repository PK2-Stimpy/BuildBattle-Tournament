package me.pk2.bbtournament.api.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static me.pk2.bbtournament.api.DatabaseAPI.*;

public class UsersAPI {
    public static void newUser(String uuid) {
        prepare();
        if(isStringInsecure(uuid))
            return;

        try {
            connection.prepareStatement("INSERT INTO users (uuid) VALUES ('" + uuid + "')").executeUpdate();
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static void deleteUser(String uuid) {
        prepare();
        if(isStringInsecure(uuid))
            return;

        try {
            connection.prepareStatement("DELETE FROM users WHERE uuid = '" + uuid + "'").executeUpdate();
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static int getUserId(String uuid) {
        prepare();
        if(isStringInsecure(uuid))
            return -1;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM users WHERE uuid = '" + uuid + "'");
            ResultSet set = statement.executeQuery();
            if(set.getFetchSize() == 0)
                return -1;

            set.next();
            return set.getInt("id");
        } catch (Exception exception) { exception.printStackTrace(); }

        return -1;
    }

    public static int getUserGroupId(String uuid) {
        prepare();
        if(isStringInsecure(uuid))
            return -1;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT group_id FROM users WHERE uuid = '" + uuid + "'");
            ResultSet set = statement.executeQuery();
            if(set.getFetchSize() == 0)
                return -1;

            set.next();
            return set.getInt("group_id");
        } catch (Exception exception) { exception.printStackTrace(); }

        return -1;
    }

    public static void setUserGroup(String uuid, int id) {
        prepare();
        if(isStringInsecure(uuid) || id < 0)
            return;

        try {
            connection.prepareStatement("UPDATE users SET group_id = '" + id + "' WHERE uuid = '" + uuid + "'").executeUpdate();
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static List<Integer> getUserList() {
        prepare();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM users");
            ResultSet set = statement.executeQuery();
            List<Integer> list = new ArrayList<>();
            if(set.getFetchSize() == 0)
                return list;

            while(set.next())
                list.add(set.getInt("id"));
            return list;
        } catch (Exception exception) { exception.printStackTrace(); }

        return null;
    }

    public static String getUUIDById(int id) {
        prepare();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM users WHERE id = '" + id + "'");
            ResultSet set = statement.executeQuery();
            if(set.getFetchSize() == 0)
                return null;

            set.next();
            return set.getString("uuid");
        } catch (Exception exception) { exception.printStackTrace(); }

        return null;
    }
}