package src.main.java.com.oceanographie.view.component;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.view.SimulationPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VueBalise {
    private Balise balise;
    private BufferedImage image;
    private boolean utiliseImage;
    private static final int TAILLE = 40;

    private int x, y; // Position actuelle

    // Couleurs selon l'état
    private static final Color COULEUR_COLLECTE = Color.CYAN;
    private static final Color COULEUR_REMONTEE = Color.YELLOW;
    private static final Color COULEUR_SURFACE = Color.GREEN;
    private static final Color COULEUR_TRANSFERT = Color.RED;
    private static final Color COULEUR_BORDURE_TRANSFERT = new Color(139, 0, 0);

    public VueBalise(Balise balise) {
        this.balise = balise;
        this.utiliseImage = false;

        try {
            File imageFile = new File("src/main/resources/images/balise_transparent.png");
            if (imageFile.exists()) {
                BufferedImage rawImage = ImageIO.read(imageFile);

                // Redimensionner l'image
                image = new BufferedImage(TAILLE, TAILLE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(rawImage, 0, 0, TAILLE, TAILLE, null);
                g2d.dispose();

                utiliseImage = true;
                System.out.println("✅ Image balise chargée (BufferedImage)");
            } else {
                System.out.println("⚠️ Image balise non trouvée");
                utiliseImage = false;
            }
        } catch (IOException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            utiliseImage = false;
        }

        mettreAJour();
    }

    public void mettreAJour() {
        Position pos = balise.getPosition();
        this.x = (int) pos.getX();
        this.y = SimulationPanel.getNiveauMer() + (int) Math.abs(pos.getZ());
    }

    // ✅ Méthode pour dessiner la balise
    public void dessiner(Graphics2D g2d) {
        if (utiliseImage && image != null) {
            // Dessiner l'image
            g2d.drawImage(image, x - TAILLE/2, y - TAILLE/2, null);
        } else {
            // Dessiner une ellipse avec la couleur selon l'état
            Color couleur = getCouleurEtat(balise.getEtat());
            g2d.setColor(couleur);
            g2d.fillOval(x - TAILLE/2, y - TAILLE/2, TAILLE, TAILLE);

            // Bordure
            Color couleurBordure = getCouleurBordure(balise.getEtat());
            g2d.setColor(couleurBordure);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(x - TAILLE/2, y - TAILLE/2, TAILLE, TAILLE);
        }


    }

    public void onEtatChanged(Balise.EtatBalise nouvelEtat) {
        // Pas besoin de faire quoi que ce soit, sera mis à jour au prochain repaint
    }

    private Color getCouleurEtat(Balise.EtatBalise etat) {
        switch (etat) {
            case COLLECTE: return COULEUR_COLLECTE;
            case REMONTEE: return COULEUR_REMONTEE;
            case EN_SURFACE: return COULEUR_SURFACE;
            case SYNCHRONISATION:
            case TRANSFERT: return COULEUR_TRANSFERT;
            default: return COULEUR_COLLECTE;
        }
    }

    private Color getCouleurBordure(Balise.EtatBalise etat) {
        switch (etat) {
            case COLLECTE: return Color.WHITE;
            case REMONTEE: return new Color(255, 165, 0);
            case EN_SURFACE: return new Color(0, 200, 0);
            case SYNCHRONISATION:
            case TRANSFERT: return COULEUR_BORDURE_TRANSFERT;
            default: return Color.WHITE;
        }
    }

    public Balise getBalise() {
        return balise;
    }

    public Point getPosition() {
        return new Point(x, y);
    }
}