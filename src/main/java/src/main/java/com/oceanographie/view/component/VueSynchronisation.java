package src.main.java.com.oceanographie.view.component;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.model.Satellite;
import src.main.java.com.oceanographie.view.SimulationPanel;

import java.awt.*;
import java.awt.geom.Line2D;

public class VueSynchronisation {
    private Balise balise;
    private Satellite satellite;
    private Color couleur;
    private float epaisseur;
    private boolean animee;
    private float phase; // Pour l'animation

    public VueSynchronisation(Balise balise, Satellite satellite) {
        this.balise = balise;
        this.satellite = satellite;
        this.couleur = Color.GREEN;
        this.epaisseur = 3.0f;
        this.animee = true;
        this.phase = 0;
    }

    public void dessiner(Graphics2D g2d) {
        Position posBalise = balise.getPosition();
        Position posSat = satellite.getPosition();

        // Convertir les positions en coordonnées d'écran
        int x1 = (int) posBalise.getX();
        int y1 = SimulationPanel.getNiveauMer() + (int) Math.abs(posBalise.getZ());

        int x2 = (int) posSat.getX();
        int y2 = (int) posSat.getY();

        // Animation : ligne pointillée qui bouge
        if (animee) {
            phase += 0.1f;
            if (phase > 20) phase = 0;

            float[] dash = {10f, 10f};
            g2d.setStroke(new BasicStroke(
                    epaisseur,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    10.0f,
                    dash,
                    phase
            ));
        } else {
            g2d.setStroke(new BasicStroke(epaisseur));
        }

        // Dessiner la ligne
        g2d.setColor(couleur);
        g2d.draw(new Line2D.Double(x1, y1, x2, y2));

        // Dessiner des cercles aux extrémités
        int rayonCercle = 5;
        g2d.fillOval(x1 - rayonCercle, y1 - rayonCercle, rayonCercle * 2, rayonCercle * 2);
        g2d.fillOval(x2 - rayonCercle, y2 - rayonCercle, rayonCercle * 2, rayonCercle * 2);

        // Texte "TRANSFERT EN COURS" au milieu
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String texte = "TRANSFERT";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(texte);
        g2d.drawString(texte, midX - textWidth/2, midY - 5);
    }

    // Getters
    public Balise getBalise() { return balise; }
    public Satellite getSatellite() { return satellite; }
}