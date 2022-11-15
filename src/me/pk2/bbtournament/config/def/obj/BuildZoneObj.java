package me.pk2.bbtournament.config.def.obj;

import me.pk2.bbtournament.config.def.obj.vec.Vec23d;
import me.pk2.bbtournament.config.def.obj.vec.Vec3d;
import org.bukkit.World;

public class BuildZoneObj {
    public World world;
    public Vec3d spawn, spectator_spawn;
    public Vec23d floor, build;

    public BuildZoneObj(World world, Vec3d spawn, Vec3d spectator_spawn, Vec23d floor, Vec23d build) {
        this.world = world;
        this.spawn = spawn;
        this.spectator_spawn = spectator_spawn;
        this.floor = floor;
        this.build = build;
    }
}