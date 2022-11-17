package me.pk2.bbtournament.config.def.obj.mode;

public enum ServerMode {
    EDIT, PLAY;

    public static ServerMode get(String mode) {
        for(ServerMode serverMode : ServerMode.values())
            if(serverMode.name().equalsIgnoreCase(mode))
                return serverMode;
        return PLAY;
    }
}