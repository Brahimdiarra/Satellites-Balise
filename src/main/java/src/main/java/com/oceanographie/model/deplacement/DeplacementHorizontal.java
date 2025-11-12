package src.main.java.com.oceanographie.model.deplacement;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;

public class DeplacementHorizontal implements StrategieDeplacementBalise{
    private double vitesse;

    public DeplacementHorizontal(double vitesse) {
        this.vitesse = vitesse;
    }

    @Override
    public void appliquerDeplacement(Balise balise) {
        Position pos = balise.getPosition();
        pos.setX(pos.getX() + vitesse);
    }
}
