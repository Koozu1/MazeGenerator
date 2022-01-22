package net.koozumaa.mazegenerator.Utils;

public class MLocation implements Cloneable{
    private double x;
    private double y;
    private double z;

    public MLocation(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public MLocation clone() {
        try {
            return (MLocation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
    public MLocation add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    public MLocation subtract(int x, int y, int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    @Override
    public String toString(){
        return ("{" + (int)x + ":" + (int)z + "}");
    }

}

