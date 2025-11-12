package src.main.java.com.oceanographie.model;

public class Position {
    private double x;
    private double y;
    private double z; // profondeur (négative pour sous l'eau)

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Distance euclidienne 2D (x, y) pour vérifier si satellite au-dessus
    public double distance2D(Position autre) {
        double dx = this.x - autre.x;
        double dy = this.y - autre.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Getters/Setters

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


}