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

    public SimulationController() {
        this.eventHandler = new EventHandler();
        this.gestionnaire = new GestionnaireSynchronisation();
        this.simulationPanel = new SimulationPanel(eventHandler);
        this.vitesseSimulation = 5;
        this.enCours = false;
        this.random = new Random();

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
            System.out.println("Simulation démarrée");
        }
    }

    public void pauseSimulation() {
        enCours = false;
        System.out.println("Simulation en pause");
    }

    public void stopSimulation() {
        enCours = false;
        timer.stop();
        System.out.println("Simulation arrêtée");
    }

    public void setVitesseSimulation(int vitesse) {
        this.vitesseSimulation = vitesse;
        // Ajuster le délai du timer (plus rapide = délai plus court)
        timer.setDelay(Math.max(5, 20 - vitesse * 2));
    }

    // Ajouter un satellite
    public void ajouterSatellite(Satellite satellite) {
        gestionnaire.ajouterSatellite(satellite);

        // Créer la vue
        VueSatellite vue = new VueSatellite(satellite);
        simulationPanel.ajouterVueSatellite(vue);

        System.out.println("Satellite ajouté: " + satellite.getId());
    }

    // Ajouter une balise
    public void ajouterBalise(Balise balise) {
        gestionnaire.ajouterBalise(balise);

        // Créer la vue
        VueBalise vue = new VueBalise(balise);
        simulationPanel.ajouterVueBalise(vue);

        System.out.println("Balise ajoutée: " + balise.getId());
    }

    // Ajouter un satellite à position aléatoire
    public void ajouterSatelliteAleatoire() {
        int x = random.nextInt(SimulationPanel.getLargeur());
        int y = random.nextInt(SimulationPanel.getNiveauMer() - 50);

        Satellite sat = new Satellite(
                "SAT-" + (gestionnaire.getSatellites().size() + 1),
                new Position(x, y, 0),
                200
        );
        sat.start();

        ajouterSatellite(sat);
    }

    // Ajouter une balise à position aléatoire
    public void ajouterBaliseAleatoire() {
        int x = random.nextInt(SimulationPanel.getLargeur());
        double profondeur = -50 - random.nextInt(200);

        // Stratégie aléatoire
        StrategieDeplacementBalise strategie;
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                strategie = new DeplacementHorizontal(1.0);
                break;
            case 1:
                strategie = new DeplacementVerticale(0.5, -50, -200);
                break;
            default:
                strategie = new DeplacementSinusoidal(30, 0.1);
                break;
        }

        Balise balise = new Balise(
                "B-" + (gestionnaire.getBalises().size() + 1),
                new Position(x, 0, profondeur),
                Math.abs(profondeur),
                strategie
        );
        balise.start();

        ajouterBalise(balise);
    }

    // Getters
    public SimulationPanel getSimulationPanel() {
        return simulationPanel;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }
}