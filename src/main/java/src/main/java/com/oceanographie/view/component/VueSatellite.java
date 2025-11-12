package src.main.java.com.oceanographie.view.component;

import src.main.java.com.nicellipse.component.NiEllipse;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.model.Satellite;

import java.awt.*;

public class VueSatellite {
    private Satellite satellite;
    private NiEllipse composant;
    private boolean disponible;
    private static final int TAILLE = 30;

    public VueSatellite(Satellite satellite) {
        this.satellite = satellite;
        this.disponible = true;

        // Créer le composant visuel Nicellipse
        composant = new NiEllipse();
        composant.setBounds(0, 0, TAILLE, TAILLE);
        composant.setBackground(Color.ORANGE);
        composant.setBorderColor(Color.YELLOW);
        composant.setStrokeWidth(2);

        // Position initiale
        mettreAJour();
    }

    public void mettreAJour() {
        Position pos = satellite.getPosition();

        // Convertir la position du modèle en coordonnées d'écran
        int x = (int) pos.getX();
        int y = (int) pos.getY();

        composant.setLocation(x - TAILLE/2, y - TAILLE/2);

        // Changer la couleur selon la disponibilité
        if (disponible) {
            composant.setBackground(Color.ORANGE);
            composant.setBorderColor(Color.YELLOW);
        } else {
            composant.setBackground(Color.RED);
            composant.setBorderColor(Color.DARK_GRAY);
        }
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        mettreAJour();
    }

    public NiEllipse getComponent() {
        return composant;
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public Point getPosition() {
        return new Point(
                composant.getX() + TAILLE/2,
                composant.getY() + TAILLE/2
        );
    }
}