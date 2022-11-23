package me.pk2.bbtournament.config.def.obj.vec;

public class Vec3d {
    public double x, y, z;

    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() { return "Vec3d(" + x + ", " + y + ", " + z + ")"; }
}
