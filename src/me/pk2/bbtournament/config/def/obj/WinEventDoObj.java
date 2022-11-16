package me.pk2.bbtournament.config.def.obj;

import me.pk2.bbtournament.config.def.obj.action.WinEventAction;

public class WinEventDoObj {
    public WinEventAction action;
    public String value;
    public WinEventDoObj(WinEventAction action, String value) {
        this.action = action;
        this.value = value;
    }

    public Object run() { return action.action.r(value); }
}