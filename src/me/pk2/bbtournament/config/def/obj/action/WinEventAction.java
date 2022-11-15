package me.pk2.bbtournament.config.def.obj.action;

import me.pk2.bbtournament.util.Function;

public enum WinEventAction {
    DB_RANKSET(v -> null);

    public final Function<Object, String> action;
    WinEventAction(Function<Object, String> action) {
        this.action = action;
    }
}