package me.pk2.bbtournament.game;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameThread {
    public static Thread thread;
    public static AtomicBoolean isRunning;
    public static void thread() {
        while(isRunning.get()) {

        }
    }

    public static void initThread() {
        isRunning = new AtomicBoolean(true);

        thread = new Thread(thread);
        thread.start();
    }
}