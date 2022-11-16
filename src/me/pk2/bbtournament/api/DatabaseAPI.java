package me.pk2.bbtournament.api;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import static me.pk2.bbtournament.config.def.ConfigMainDefault.server;

public class DatabaseAPI {
    public static Connection connection = null;

    public static boolean isStringInsecure(String string) {
        return string.contains("\"") || string.contains("'") || string.contains(";") || string.contains("`") || string.contains("--");
    }

    public static void connect() {
        String use = server.database.use;
        switch(use.toLowerCase()) {
            case "remote":
                String host = server.database.remote.host;
                int port = server.database.remote.port;
                String schema = server.database.remote.schema;
                String username = server.database.remote.username;
                String password = server.database.remote.password;

                try {
                    connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + schema, username, password);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            case "local":
                String path = server.database.local.path;
                try {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + (path.contains("..")?new File(path).getAbsolutePath():path));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static void prepare() {
        try {
            if (connection == null || connection.isClosed())
                connect();
        } catch (Exception exception) {
            exception.printStackTrace();

            connect();
        }
    }
}