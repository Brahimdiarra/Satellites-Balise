package src.main.java.com.oceanographie.model.deplacement;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;

public class DeplacementVerticale implements StrategieDeplacementBalise {
    Double vitesse;
    private boolean montant;
    private double profondeurMin;
    private double profondeurMax;

 public DeplacementVerticale( Double vitesse , boolean montant, double profondeurMin, double profondeurMax ) {
     this.vitesse = vitesse;
     this.montant = montant;
     this.profondeurMin = profondeurMin;
     this.profondeurMax = profondeurMax;
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
