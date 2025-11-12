package src.main.java.com.oceanographie.view.component;

import src.main.java.com.nicellipse.component.NiEllipse;
import src.main.java.com.nicellipse.component.NiLabel;
import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.view.SimulationPanel;

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
    private static final Color COULEUR_SYNCHRO = Color.MAGENTA;

    public VueBalise(Balise balise) {
        this.balise = balise;

        // Créer le composant visuel
        composant = new NiEllipse();
        composant.setBounds(0, 0, TAILLE, TAILLE);
        composant.setBackground(COULEUR_COLLECTE);
        composant.setBorderColor(Color.WHITE);
        composant.setStrokeWidth(2);

        // Label pour afficher l'ID (optionnel)
        label = new NiLabel(balise.getId());
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 10));

        // Position initiale
        mettreAJour();
    }

    public void mettreAJour() {
        Position pos = balise.getPosition();

        // Convertir la position 3D en 2D pour l'affichage
        int x = (int) pos.getX();

        // La profondeur Z négative devient Y positif (vers le bas)
        // Surface = NIVEAU_MER, plus profond = plus bas
        int y = SimulationPanel.getNiveauMer() + (int) Math.abs(pos.getZ());

        composant.setLocation(x - TAILLE/2, y - TAILLE/2);

        // Mettre à jour le label
        if (label != null) {
            label.setLocation(x + TAILLE/2 + 5, y - TAILLE/2);
        }
    }

    public void onEtatChanged(Balise.EtatBalise nouvelEtat) {
        // Changer la couleur selon l'état
        switch (nouvelEtat) {
            case COLLECTE:
                composant.setBackground(COULEUR_COLLECTE);
                break;
            case REMONTEE:
                composant.setBackground(COULEUR_REMONTEE);
                break;
            case EN_SURFACE:
                composant.setBackground(COULEUR_SURFACE);
                break;
            case SYNCHRONISATION:
            case TRANSFERT:
                composant.setBackground(COULEUR_SYNCHRO);
                break;
        }

        mettreAJour();
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