package me.pk2.bbtournament.api.bungee;

import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static me.pk2.bbtournament.BuildBattleT.INSTANCE;

public class BungeeAPI {
    public static void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(server);

            player.sendPluginMessage(INSTANCE, "BungeeCord", byteArrayOutputStream.toByteArray());

            byteArrayOutputStream.close();
            dataOutputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}