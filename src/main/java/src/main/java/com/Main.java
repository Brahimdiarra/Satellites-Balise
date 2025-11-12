package src.main.java.com;

import src.main.java.com.oceanographie.controller.SimulationController;
import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.model.Satellite;
import src.main.java.com.oceanographie.model.deplacement.DeplacementHorizontal;
import src.main.java.com.oceanographie.model.deplacement.DeplacementSinusoidal;
import src.main.java.com.oceanographie.model.deplacement.DeplacementVerticale;
import src.main.java.com.oceanographie.view.MainWindow;
import src.main.java.com.oceanographie.view.SimulationPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer le contrôleur
            SimulationController controller = new SimulationController();

            // Créer la fenêtre principale
            MainWindow window = new MainWindow(controller);

            // Ajouter quelques éléments initiaux pour la démo
            ajouterElementsDemo(controller);

            System.out.println("Application démarrée. Appuyez sur Démarrer pour lancer la simulation.");
        });
    }

    private static void ajouterElementsDemo(SimulationController controller) {
        // Ajouter 2 satellites
        Satellite sat1 = new Satellite("SAT-1", new Position(200, 50, 0), 200);
        sat1.start();
        controller.ajouterSatellite(sat1);

        Satellite sat2 = new Satellite("SAT-2", new Position(600, 80, 0), 200);
        sat2.start();
        controller.ajouterSatellite(sat2);

        // Balise horizontale qui rebondit
        Balise b1 = new Balise(
                "B-Horizontal",
                new Position(100, 0, -100),
                100,
                new DeplacementHorizontal(2.0)
        );
        b1.start();
        controller.ajouterBalise(b1);

// Balise verticale qui oscille
        Balise b2 = new Balise(
                "B-Vertical",
                new Position(500, 0, -150),
                150,
                new DeplacementVerticale(1.0, -50, -300)
        );
        b2.start();
        controller.ajouterBalise(b2);

// Balise sinusoïdale
        Balise b3 = new Balise(
                "B-Sinus",
                new Position(200, 0, -100),
                100,
                new DeplacementSinusoidal(50, 0.08)
        );
        b3.start();
        controller.ajouterBalise(b3);
    }
}