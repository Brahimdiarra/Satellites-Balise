package src.main.java.com.oceanographie.controller;

import src.main.java.com.eventHandler.EventHandler;
import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.model.Satellite;
import src.main.java.com.oceanographie.model.deplacement.DeplacementHorizontal;
import src.main.java.com.oceanographie.model.deplacement.DeplacementSinusoidal;
import src.main.java.com.oceanographie.model.deplacement.DeplacementVerticale;
import src.main.java.com.oceanographie.model.deplacement.StrategieDeplacementBalise;
import src.main.java.com.oceanographie.model.observer.GestionnaireSynchronisation;
import src.main.java.com.oceanographie.view.SimulationPanel;
import src.main.java.com.oceanographie.view.component.VueBalise;
import src.main.java.com.oceanographie.view.component.VueSatellite;

import javax.swing.Timer;
import java.util.Random;


public class SimulationController {
    private GestionnaireSynchronisation gestionnaire;
    private SimulationPanel simulationPanel;
    private EventHandler eventHandler;
    private Timer timer;
    private boolean enCours;
    private int vitesseSimulation;
    private Random random;
    private int compteurSynchronisations; // ✅ Nouveau

    public SimulationController() {
        this.eventHandler = new EventHandler();
        this.gestionnaire = new GestionnaireSynchronisation(eventHandler);
        this.simulationPanel = new SimulationPanel(eventHandler);
        this.vitesseSimulation = 5;
        this.enCours = false;
        this.random = new Random();
        this.compteurSynchronisations = 0; // ✅ Nouveau

        // Timer pour la boucle de simulation (60 FPS)
        timer = new Timer(16, e -> {
            if (enCours) {
                executerPas();
            }
        });
    }

    private void executerPas() {
        // Déplacer tous les satellites
        for (Satellite sat : gestionnaire.getSatellites()) {
            sat.deplacer();
        }

        // Déplacer toutes les balises
        for (Balise balise : gestionnaire.getBalises()) {
            balise.deplacer();
        }

        // Vérifier les synchronisations
        gestionnaire.verifierSynchronisations();

        // Mettre à jour la vue
        simulationPanel.mettreAJour();
    }

    public void demarrerSimulation() {
        if (!enCours) {
            enCours = true;
            timer.start();
            System.out.println("▶️ Simulation démarrée");
        }
    }

    public void pauseSimulation() {
        enCours = false;
        System.out.println("⏸️ Simulation en pause");
    }

    public void stopSimulation() {
        enCours = false;
        timer.stop();
        System.out.println("⏹️ Simulation arrêtée");
    }

    public void setVitesseSimulation(int vitesse) {
        this.vitesseSimulation = vitesse;
        timer.setDelay(Math.max(5, 20 - vitesse * 2));
    }

    // ✅ NOUVELLE MÉTHODE - Définir la durée du transfert
    public void setDureeTransfert(int secondes) {
        gestionnaire.setDureeTransfert(secondes * 1000L); // Convertir en millisecondes
        System.out.println("⏱️ Durée de transfert définie à " + secondes + " secondes");
    }

    // ✅ NOUVELLE MÉTHODE - Incrémenter le compteur de synchronisations
    public void incrementerCompteurSyncs() {
        compteurSynchronisations++;
    }

    // Ajouter un satellite
    public void ajouterSatellite(Satellite satellite) {
        // lier le EventHandler au satellite
        satellite.setEventHandler(this.eventHandler);
        gestionnaire.ajouterSatellite(satellite);

        VueSatellite vue = new VueSatellite(satellite);
        simulationPanel.ajouterVueSatellite(vue);

        System.out.println("🛰️ Satellite ajouté: " + satellite.getId());
    }

    // Ajouter une balise
    public void ajouterBalise(Balise balise) {
        // lier le EventHandler à la balise
        balise.setEventHandler(this.eventHandler);
        gestionnaire.ajouterBalise(balise);

        VueBalise vue = new VueBalise(balise);
        simulationPanel.ajouterVueBalise(vue);

        System.out.println("🔵 Balise ajoutée: " + balise.getId());
    }

    // Ajouter un satellite à position aléatoire
    public void ajouterSatelliteAleatoire() {
        int x = random.nextInt(SimulationPanel.getLargeur());
        int y = 10 + random.nextInt(70); // Entre 30 et 180

        Satellite sat = new Satellite(
                "SAT-" + (gestionnaire.getSatellites().size() + 1),
                new Position(x, y, 0),
                200
        );
    // lier le EventHandler avant de démarrer
    sat.setEventHandler(this.eventHandler);
    sat.start();

        ajouterSatellite(sat);
    }

    // Ajouter une balise à position aléatoire
    public void ajouterBaliseAleatoire() {
        int x = random.nextInt(SimulationPanel.getLargeur());
        double profondeur = -50 - random.nextInt(200); // Entre -50 et -250

        // Stratégie aléatoire
        StrategieDeplacementBalise strategie;
        int type = random.nextInt(4);
        switch (type) {
            case 0:
                strategie = new DeplacementHorizontal(1.5);
                System.out.println("  └─ Stratégie: Horizontal");
                break;
            case 1:
                strategie = new DeplacementVerticale(0.8, -50, -200);
                System.out.println("  └─ Stratégie: Vertical");
                break;
            default:
                strategie = new DeplacementSinusoidal(40, 0.1);
                System.out.println("  └─ Stratégie: Sinusoïdal");
                break;

        }

        Balise balise = new Balise(
                "B-" + (gestionnaire.getBalises().size() + 1),
                new Position(x, 0, profondeur),
                Math.abs(profondeur),
                strategie
        );
    // lier le EventHandler avant de démarrer
    balise.setEventHandler(this.eventHandler);
    balise.start();

        ajouterBalise(balise);
    }

    // ✅ NOUVELLES MÉTHODES pour les statistiques
    public int getNombreSatellites() {
        return gestionnaire.getSatellites().size();
    }

    public int getNombreBalises() {
        return gestionnaire.getBalises().size();
    }

    public int getNombreSynchronisations() {
        return compteurSynchronisations;
    }

    // Getters
    public SimulationPanel getSimulationPanel() {
        return simulationPanel;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public GestionnaireSynchronisation getGestionnaire() {
        return gestionnaire;
    }
}