package src.main.java.com.oceanographie.model.deplacement;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;

public class DeplacementSinusoidal implements StrategieDeplacementBalise{
    private double amplitude; // Amplitude verticale
    private double frequence;
    private double temps;
    private double vitesseHorizontale;
    private boolean versLaDroite;
    private double profondeurCentrale;


    public DeplacementSinusoidal(double amplitude, double frequence) {
        this.amplitude = amplitude;
        this.frequence = frequence;
        this.temps = 0;
        this.vitesseHorizontale = 1.5;
        this.versLaDroite = true;
        this.profondeurCentrale = -100; // Oscille autour de -100
    }

    @Override
    public void appliquerDeplacement(Balise balise) {
        Position pos = balise.getPosition();

        // Limites horizontales
        final double LIMITE_GAUCHE = 20;
        final double LIMITE_DROITE = 980;

        // Déplacement horizontal avec rebond
        if (versLaDroite) {
            pos.setX(pos.getX() + vitesseHorizontale);
            if (pos.getX() >= LIMITE_DROITE) {
                pos.setX(LIMITE_DROITE);
                versLaDroite = false;
            }
        } else {
            pos.setX(pos.getX() - vitesseHorizontale);
            if (pos.getX() <= LIMITE_GAUCHE) {
                pos.setX(LIMITE_GAUCHE);
                versLaDroite = true;
            }
        }

        // Oscillation verticale (sinusoïdale)
        // z = profondeurCentrale + amplitude * sin(frequence * temps)
        double z = profondeurCentrale + amplitude * Math.sin(frequence * temps);

        // S'assurer de rester dans les limites verticales
        z = Math.max(z, -350); // Pas plus profond que -350
        z = Math.min(z, -20);  // Pas plus proche que -20 de la surface

        pos.setZ(z);

        // Incrémenter le temps pour l'animation
        temps += 0.1;
    }


    public void setProfondeurCentrale(double profondeurCentrale) {
        this.profondeurCentrale = profondeurCentrale;
    }

}
