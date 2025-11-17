package src.main.java.com.oceanographie.view;

import src.main.java.com.eventHandler.EventHandler;
import src.main.java.com.oceanographie.events.*;
import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Satellite;
import src.main.java.com.oceanographie.view.component.VueBalise;
import src.main.java.com.oceanographie.view.component.VueSatellite;
import src.main.java.com.oceanographie.view.component.VueSynchronisation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationPanel extends JPanel implements  // ✅ JPanel au lieu de JComponent
        BaliseEventListener,
        SatelliteEventListener,
        SynchronisationEventListener {

    private static final long serialVersionUID = 1L;

    private static final int LARGEUR = 1000;
    private static final int HAUTEUR = 600;
    private static final int NIVEAU_MER = 200;

    private ArrayList<VueSatellite> vuesSatellites;
    private ArrayList<VueBalise> vuesBalises;
    private ArrayList<VueSynchronisation> vuesSynchronisations;

    private EventHandler eventHandler;
    private VueOcean vueOcean;

    public SimulationPanel(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        this.vuesSatellites = new ArrayList<>();
        this.vuesBalises = new ArrayList<>();
        this.vuesSynchronisations = new ArrayList<>();

        setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
        setLayout(null); // ✅ Layout null pour positionnement absolu
        setBackground(Color.BLACK);
        setDoubleBuffered(true); // ✅ Pour un meilleur rendu

        vueOcean = new VueOcean(LARGEUR, HAUTEUR, NIVEAU_MER);

        eventHandler.registerListener(BaliseEvent.class, this);
        eventHandler.registerListener(SatelliteEvent.class, this);
        eventHandler.registerListener(SynchronisationEvent.class, this);
    }

    public void ajouterVueSatellite(VueSatellite vue) {
        vuesSatellites.add(vue);

        // ✅ Ajouter le composant au panel
        JComponent comp = vue.getComponent();
        this.add(comp);
        comp.setVisible(true);

        // ✅ Positionner le composant
        vue.mettreAJour();

        System.out.println("🛰️ Satellite ajouté - Total: " + vuesSatellites.size());
        System.out.println("   Position: " + comp.getX() + "," + comp.getY());
        System.out.println("   Taille: " + comp.getWidth() + "x" + comp.getHeight());
        System.out.println("   Visible: " + comp.isVisible());

        revalidate();
        repaint();
    }

    public void ajouterVueBalise(VueBalise vue) {
        vuesBalises.add(vue);

        // ✅ Ajouter le composant au panel
        JComponent comp = vue.getComponent();
        this.add(comp);
        comp.setVisible(true);

        // ✅ Ajouter le label
        if (vue.getLabel() != null) {
            this.add(vue.getLabel());
            vue.getLabel().setVisible(true);
        }

        // ✅ Positionner les composants
        vue.mettreAJour();

        System.out.println("🔵 Balise ajoutée - Total: " + vuesBalises.size());
        System.out.println("   Position: " + comp.getX() + "," + comp.getY());
        System.out.println("   Taille: " + comp.getWidth() + "x" + comp.getHeight());
        System.out.println("   Visible: " + comp.isVisible());

        revalidate();
        repaint();
    }

    public void mettreAJour() {
        for (VueSatellite vue : vuesSatellites) {
            vue.mettreAJour();
        }
        for (VueBalise vue : vuesBalises) {
            vue.mettreAJour();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner le fond océan
        vueOcean.dessiner(g2d);

        // Dessiner les lignes de synchronisation
        for (VueSynchronisation sync : vuesSynchronisations) {
            sync.dessiner(g2d);
        }

        // Ligne de mer
        g2d.setColor(Color.CYAN);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, NIVEAU_MER, LARGEUR, NIVEAU_MER);

        // Légende
        dessinerLegende(g2d);

        // ✅ Debug: dessiner les rectangles des composants
        /*g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1));
        for (VueSatellite vue : vuesSatellites) {
            JComponent comp = vue.getComponent();
            g2d.drawRect(comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight());
        }
        for (VueBalise vue : vuesBalises) {
            JComponent comp = vue.getComponent();
            g2d.drawRect(comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight());
        }*/
    }

    private void dessinerLegende(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Surface de l'eau", 10, NIVEAU_MER - 5);
        g2d.drawString("Espace (satellites)", 10, 20);
        g2d.drawString("Océan (balises)", 10, NIVEAU_MER + 20);
    }

    @Override
    public void onBaliseStateChanged(BaliseEvent event) {
        SwingUtilities.invokeLater(() -> {
            Balise balise = event.getBalise();
            for (VueBalise vue : vuesBalises) {
                if (vue.getBalise().equals(balise)) {
                    vue.onEtatChanged(event.getNouvelEtat());
                    break;
                }
            }
            repaint();
        });
    }

    @Override
    public void onSatelliteDisponibiliteChanged(SatelliteEvent event) {
        SwingUtilities.invokeLater(() -> {
            Satellite satellite = event.getSatellite();
            for (VueSatellite vue : vuesSatellites) {
                if (vue.getSatellite().equals(satellite)) {
                    vue.setDisponible(event.isDisponible());
                    break;
                }
            }
            repaint();
        });
    }

    @Override
    public void onSynchronisationDebut(SynchronisationEvent event) {
        SwingUtilities.invokeLater(() -> {
            VueSynchronisation sync = new VueSynchronisation(
                    event.getBalise(),
                    event.getSatellite()
            );
            vuesSynchronisations.add(sync);
            repaint();
        });
    }

    @Override
    public void onSynchronisationFin(SynchronisationEvent event) {
        SwingUtilities.invokeLater(() -> {
            vuesSynchronisations.removeIf(sync ->
                    sync.getBalise().equals(event.getBalise()) &&
                            sync.getSatellite().equals(event.getSatellite())
            );
            repaint();
        });
    }



    public static int getNiveauMer() {
        return NIVEAU_MER;
    }

    public static int getLargeur() {
        return LARGEUR;
    }

    public static int getHauteurPanel() {
        return HAUTEUR;
    }
}