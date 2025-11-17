package src.main.java.com.oceanographie.view.component;

import src.main.java.com.nicellipse.component.NiEllipse;
import src.main.java.com.nicellipse.component.NiLabel;
import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.view.SimulationPanel;


import javax.swing.*;
import java.awt.*;


public class VueBalise {
    private Balise balise;
    private NiEllipse composant;
    private NiLabel label;
    private static final int TAILLE = 20;

    // Couleurs selon l'état
    private static final Color COULEUR_COLLECTE = Color.CYAN;
    private static final Color COULEUR_REMONTEE = Color.YELLOW;
    private static final Color COULEUR_SURFACE = Color.GREEN;
    private static final Color COULEUR_TRANSFERT = Color.RED;
    private static final Color COULEUR_BORDURE_TRANSFERT = new Color(139, 0, 0);

    public VueBalise(Balise balise) {
        this.balise = balise;

        composant = new NiEllipse();
        composant.setBounds(0, 0, TAILLE, TAILLE);
        composant.setBackground(COULEUR_COLLECTE);
        composant.setBorderColor(Color.WHITE);
        composant.setStrokeWidth(2);

        mettreAJour();
    }

    public void mettreAJour() {
        Position pos = balise.getPosition();

        int x = (int) pos.getX();
        int y = SimulationPanel.getNiveauMer() + (int) Math.abs(pos.getZ());

        composant.setLocation(x - TAILLE/2, y - TAILLE/2);
        // ✅ Mettre à jour la couleur selon l'état actuel
        updateCouleur(balise.getEtat());
    }

    public void onEtatChanged(Balise.EtatBalise nouvelEtat) {
        // ✅ Juste changer la couleur, sans appeler mettreAJour()
        updateCouleur(nouvelEtat);
    }

    // ✅ Méthode privée pour changer la couleur (évite duplication)
    private void updateCouleur(Balise.EtatBalise etat) {
        switch (etat) {
            case COLLECTE:
                composant.setBackground(COULEUR_COLLECTE);
                composant.setBorderColor(Color.WHITE);
                break;

            case REMONTEE:
                composant.setBackground(COULEUR_REMONTEE);
                composant.setBorderColor(Color.ORANGE);
                break;

            case EN_SURFACE:
                composant.setBackground(COULEUR_SURFACE);
                composant.setBorderColor(Color.GREEN.darker());
                break;

            case SYNCHRONISATION:
            case TRANSFERT:
                composant.setBackground(COULEUR_TRANSFERT);
                composant.setBorderColor(COULEUR_BORDURE_TRANSFERT);
                break;
        }
    }

    public NiEllipse getComponent() {
        return composant;
    }

    public NiLabel getLabel() {
        return label;
    }

    public Balise getBalise() {
        return balise;
    }

    public Point getPosition() {
        return new Point(
                composant.getX() + TAILLE/2,
                composant.getY() + TAILLE/2
        );
    }
}