package src.main.java.com.oceanographie.model;

public abstract class ElementMobile {
    protected String id;
    protected Position position;
    protected double vitesse;
    protected boolean actif;

    public ElementMobile(String id, Position position, double vitesse) {
        this.id = id;
        this.position = position;
        this.vitesse = vitesse;
        this.actif = false;
    }

    public abstract void deplacer();

    public void start() {
        this.actif = true;
    }

    public void stop() {
        this.actif = false;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getVitesse() {
        return vitesse;
    }

    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}