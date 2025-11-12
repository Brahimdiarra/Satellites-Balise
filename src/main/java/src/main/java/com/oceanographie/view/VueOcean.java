package src.main.java.com.oceanographie.view;

import java.awt.*;
import java.awt.geom.Path2D;

public class VueOcean {
    private int largeur;
    private int hauteur;
    private int niveauMer;
    private double phase = 0;

    public VueOcean(int largeur, int hauteur, int niveauMer) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.niveauMer = niveauMer;
    }

    public void dessiner(Graphics2D g2d) {
        // Dessiner le ciel (espace)
        GradientPaint ciel = new GradientPaint(
                0, 0, new Color(10, 10, 30),
                0, niveauMer, new Color(20, 20, 60)
        );
        g2d.setPaint(ciel);
        g2d.fillRect(0, 0, largeur, niveauMer);

        // Dessiner l'océan avec dégradé
        GradientPaint ocean = new GradientPaint(
                0, niveauMer, new Color(0, 100, 200),
                0, hauteur, new Color(0, 20, 80)
        );
        g2d.setPaint(ocean);
        g2d.fillRect(0, niveauMer, largeur, hauteur - niveauMer);

        // Dessiner des vagues à la surface
        dessinerVagues(g2d);

        // Étoiles dans l'espace (optionnel)
        dessinerEtoiles(g2d);

        // Incrémenter la phase pour l'animation des vagues
        phase += 0.05;
    }

    private void dessinerVagues(Graphics2D g2d) {
        Path2D vague = new Path2D.Double();
        vague.moveTo(0, niveauMer);

        // Créer une forme de vague sinusoïdale
        for (int x = 0; x <= largeur; x += 5) {
            double y = niveauMer + Math.sin((x + phase * 10) * 0.05) * 3;
            vague.lineTo(x, y);
        }

        vague.lineTo(largeur, niveauMer + 10);
        vague.lineTo(0, niveauMer + 10);
        vague.closePath();

        g2d.setColor(new Color(100, 150, 255, 100));
        g2d.fill(vague);
    }

    private void dessinerEtoiles(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);

        // Quelques étoiles fixes
        for (int i = 0; i < 50; i++) {
            int x = (i * 137) % largeur;
            int y = (i * 73) % niveauMer;
            g2d.fillOval(x, y, 2, 2);
        }
    }
}