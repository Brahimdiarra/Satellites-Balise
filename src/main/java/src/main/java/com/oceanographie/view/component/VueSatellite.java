package src.main.java.com.oceanographie.view.component;

import src.main.java.com.nicellipse.component.NiEllipse;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.model.Satellite;


import javax.swing.*;
import java.awt.*;


public class VueSatellite {
    private Satellite satellite;
    private NiEllipse composant;
    private static final int TAILLE = 30;

    private static final Color COULEUR_DISPONIBLE = Color.ORANGE;
    private static final Color COULEUR_BORDURE_DISPONIBLE = Color.YELLOW;
    private static final Color COULEUR_TRANSFERT = Color.RED;
    private static final Color COULEUR_BORDURE_TRANSFERT = new Color(139, 0, 0);

    public VueSatellite(Satellite satellite) {
        this.satellite = satellite;

        composant = new NiEllipse();
        composant.setBounds(0, 0, TAILLE, TAILLE);
        composant.setBackground(COULEUR_DISPONIBLE);
        composant.setBorderColor(COULEUR_BORDURE_DISPONIBLE);
        composant.setStrokeWidth(2);

        mettreAJour();
    }

    public void mettreAJour() {
        Position pos = satellite.getPosition();

        int x = (int) pos.getX();
        int y = (int) pos.getY();

        composant.setLocation(x - TAILLE/2, y - TAILLE/2);

        // ✅ Mettre à jour la couleur
        updateCouleur();
    }

    public void setDisponible(boolean disponible) {
        // ✅ Juste changer la couleur
        updateCouleur();
    }

    // ✅ Méthode privée pour changer la couleur
    private void updateCouleur() {
        if (satellite.isDisponible()) {
            composant.setBackground(COULEUR_DISPONIBLE);
            composant.setBorderColor(COULEUR_BORDURE_DISPONIBLE);
        } else {
            composant.setBackground(COULEUR_TRANSFERT);
            composant.setBorderColor(COULEUR_BORDURE_TRANSFERT);
        }
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