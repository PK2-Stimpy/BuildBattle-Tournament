package me.pk2.bbtournament.config.def.obj.action;

import me.pk2.bbtournament.util.Function;

public enum WinEventAction {
    NONE(a -> null),
    DB_RANKSET(a -> null);

    public final Function<Object, String> action;
    WinEventAction(Function<Object, String> action) {
        this.action = action;
    }

    public static WinEventAction get(String name) {
        for (WinEventAction action : values())
            if (action.name().equalsIgnoreCase(name))
                return action;
        return NONE;
    }
}