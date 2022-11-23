package me.pk2.bbtournament.config.def.obj.vec;

public class Vec23d {
    public double x1, y1, z1;
    public double x2, y2, z2;

    public Vec23d(double x1, double x2, double y1, double y2, double z1, double z2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }

    @Override
    public String toString() { return String.format("Vec32d((%s,%s,%s),(%s,%s,%s))", x1, y1, z1, x2, y2, z2); }
}

