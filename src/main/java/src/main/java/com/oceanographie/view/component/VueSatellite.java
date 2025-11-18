package src.main.java.com.oceanographie.view.component;

import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.model.Satellite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VueSatellite {
    private Satellite satellite;
    private BufferedImage image;
    private boolean utiliseImage;
    private static final int TAILLE = 50;

    private int x, y; // Position actuelle

    private static final Color COULEUR_DISPONIBLE = Color.ORANGE;
    private static final Color COULEUR_BORDURE_DISPONIBLE = Color.YELLOW;
    private static final Color COULEUR_TRANSFERT = Color.RED;
    private static final Color COULEUR_BORDURE_TRANSFERT = new Color(139, 0, 0);

    public VueSatellite(Satellite satellite) {
        this.satellite = satellite;
        this.utiliseImage = false;

        try {
            File imageFile = new File("src/main/resources/images/satellite_transparent.png");
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
                System.out.println("✅ Image satellite chargée (BufferedImage)");
            } else {
                System.out.println("⚠️ Image satellite non trouvée");
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
        Position pos = satellite.getPosition();
        this.x = (int) pos.getX();
        this.y = (int) pos.getY();
    }

    // ✅ Méthode pour dessiner le satellite
    public void dessiner(Graphics2D g2d) {
        if (utiliseImage && image != null) {
            // Dessiner l'image
            g2d.drawImage(image, x - TAILLE/2, y - TAILLE/2, null);

            // Ajouter un contour rouge si occupé
            if (!satellite.isDisponible()) {
                g2d.setColor(new Color(255, 0, 0, 150));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(x - TAILLE/2, y - TAILLE/2, TAILLE, TAILLE);
            }
        } else {
            // Dessiner une ellipse par défaut
            if (satellite.isDisponible()) {
                g2d.setColor(COULEUR_DISPONIBLE);
            } else {
                g2d.setColor(COULEUR_TRANSFERT);
            }
            g2d.fillOval(x - TAILLE/2, y - TAILLE/2, TAILLE, TAILLE);

            // Bordure
            if (satellite.isDisponible()) {
                g2d.setColor(COULEUR_BORDURE_DISPONIBLE);
            } else {
                g2d.setColor(COULEUR_BORDURE_TRANSFERT);
            }
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(x - TAILLE/2, y - TAILLE/2, TAILLE, TAILLE);
        }
    }

    public void setDisponible(boolean disponible) {
        // Pas besoin de faire quoi que ce soit, sera mis à jour au prochain repaint
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public Point getPosition() {
        return new Point(x, y);
    }
}