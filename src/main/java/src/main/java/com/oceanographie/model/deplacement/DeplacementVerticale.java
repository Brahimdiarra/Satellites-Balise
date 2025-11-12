package src.main.java.com.oceanographie.model.deplacement;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;

public class DeplacementVerticale implements StrategieDeplacementBalise {
    Double vitesse;
    private boolean montant;
    private double profondeurMin;
    private double profondeurMax;

    public DeplacementVerticale(double vitesse, double profondeurMin, double profondeurMax) {
        this.vitesse = vitesse;
        this.profondeurMin = Math.max(profondeurMin, profondeurMax); // Le moins négatif
        this.profondeurMax = Math.min(profondeurMin, profondeurMax); // Le plus négatif
        this.montant = false;
    }

    @Override
    public void appliquerDeplacement(Balise balise) {
        Position pos = balise.getPosition();

        if (montant) {
            pos.setZ(pos.getZ() + vitesse);
            if (pos.getZ() >= profondeurMin) {
                montant = false;
            }
        } else {
            pos.setZ(pos.getZ() - vitesse);
            if (pos.getZ() <= profondeurMax) {
                montant = true;
            }
        }


    }
}
