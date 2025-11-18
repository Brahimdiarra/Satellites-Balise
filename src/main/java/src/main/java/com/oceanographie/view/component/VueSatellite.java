package src.main.java.com.oceanographie.view.component;

import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.model.Satellite;
import src.main.java.com.oceanographie.model.observer.Observable;
import src.main.java.com.oceanographie.model.observer.Observateur;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VueSatellite implements Observateur { // ✅ Implémente Observateur
    private Satellite satellite;
    private BufferedImage image;
    private boolean utiliseImage;
    private static final int TAILLE = 50;
    private int x, y;

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
                image = new BufferedImage(TAILLE, TAILLE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(rawImage, 0, 0, TAILLE, TAILLE, null);
                g2d.dispose();
                utiliseImage = true;
                System.out.println("✅ Image satellite chargée");
            } else {
                utiliseImage = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            utiliseImage = false;
        }

        // ✅ S'ENREGISTRER comme observateur
        satellite.ajouterObservateur(this);

        mettreAJour();
    }

    // ✅ Implémentation de l'interface Observateur

    public void mettreAJour() {
        Position pos = satellite.getPosition();
        this.x = (int) pos.getX();
        this.y = (int) pos.getY();
    }

    public void dessiner(Graphics2D g2d) {
        if (utiliseImage && image != null) {
            g2d.drawImage(image, x - TAILLE / 2, y - TAILLE / 2, null);

            if (!satellite.isDisponible()) {
                g2d.setColor(new Color(255, 0, 0, 150));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(x - TAILLE / 2, y - TAILLE / 2, TAILLE, TAILLE);
            }
        } else {
            if (satellite.isDisponible()) {
                g2d.setColor(COULEUR_DISPONIBLE);
            } else {
                g2d.setColor(COULEUR_TRANSFERT);
            }
            g2d.fillOval(x - TAILLE / 2, y - TAILLE / 2, TAILLE, TAILLE);

            if (satellite.isDisponible()) {
                g2d.setColor(COULEUR_BORDURE_DISPONIBLE);
            } else {
                g2d.setColor(COULEUR_BORDURE_TRANSFERT);
            }
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(x - TAILLE / 2, y - TAILLE / 2, TAILLE, TAILLE);
        }
    }

    public void setDisponible(boolean disponible) {
        // Plus besoin, géré par actualiser()
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    @Override
    public void actualiser(Observable observable) {
        if (observable instanceof Satellite) {
            // Le satellite a changé, mettre à jour la vue
            mettreAJour();
            System.out.println("🔄 VueSatellite mise à jour pour " + satellite.getId());
        }
    }
}