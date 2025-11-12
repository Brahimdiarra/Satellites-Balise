package src.main.java.com.oceanographie.model.deplacement;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;

public class DeplacementHorizontal implements StrategieDeplacementBalise{
    private double vitesse;
    private boolean versLaDroite;

    public DeplacementHorizontal(double vitesse) {
        this.vitesse = vitesse;
        this.versLaDroite = true;
    }

    @Override
    public void appliquerDeplacement(Balise balise) {
        Position pos = balise.getPosition();
        final double LIMITE_GAUCHE = 20;
        final double LIMITE_DROITE = 980; // Si largeur = 1000

        // Déplacement selon la direction
        if (versLaDroite) {
            pos.setX(pos.getX() + vitesse);

            // Si atteint la limite droite, changer de direction
            if (pos.getX() >= LIMITE_DROITE) {
                pos.setX(LIMITE_DROITE);
                versLaDroite = false; // Inverser direction
            }
        } else {
            pos.setX(pos.getX() - vitesse);

            // Si atteint la limite gauche, changer de direction
            if (pos.getX() <= LIMITE_GAUCHE) {
                pos.setX(LIMITE_GAUCHE);
                versLaDroite = true; // Inverser direction
            }
        }
    }


    public boolean isVersLaDroite() {
        return versLaDroite;
    }

    public void setVersLaDroite(boolean versLaDroite) {
        this.versLaDroite = versLaDroite;
    }
}
