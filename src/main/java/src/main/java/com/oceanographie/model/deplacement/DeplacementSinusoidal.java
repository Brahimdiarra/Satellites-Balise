package src.main.java.com.oceanographie.model.deplacement;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;

public class DeplacementSinusoidal implements StrategieDeplacementBalise{
    private double amplitude;
    private double frequence;
    private double temps;

    public DeplacementSinusoidal(double amplitude, double frequence) {
        this.amplitude = amplitude;
        this.frequence = frequence;
        this.temps = 0;
    }

    @Override
    public void appliquerDeplacement(Balise balise) {
        Position pos = balise.getPosition();

        // Déplacement horizontal
        pos.setX(pos.getX() + 1.0);

        // Oscillation verticale
        double z = -amplitude * Math.sin(frequence * temps);
        pos.setZ(z);

        temps += 0.1;
    }
}
