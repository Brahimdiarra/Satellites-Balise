package src.main.java.com.oceanographie.view;

import src.main.java.com.oceanographie.controller.SimulationController;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private SimulationPanel simulationPanel;
    private JPanel controlPanel;
    private SimulationController controller;

    public MainWindow(SimulationController controller) {
        this.controller = controller;

        setTitle("Simulation Satellites et Balises");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de simulation (centre)
        simulationPanel = controller.getSimulationPanel();
        add(simulationPanel, BorderLayout.CENTER);

        // Panel de contrôle (bas)
        controlPanel = creerPanelControle();
        add(controlPanel, BorderLayout.SOUTH);

        // Barre d'informations (haut)
        JPanel infoPanel = creerPanelInfo();
        add(infoPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel creerPanelControle() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setPreferredSize(new Dimension(0, 60));

        // Bouton Démarrer
        JButton btnDemarrer = new JButton("▶ Démarrer");
        btnDemarrer.addActionListener(e -> controller.demarrerSimulation());

        // Bouton Pause
        JButton btnPause = new JButton("⏸ Pause");
        btnPause.addActionListener(e -> controller.pauseSimulation());

        // Bouton Stop
        JButton btnStop = new JButton("⏹ Stop");
        btnStop.addActionListener(e -> controller.stopSimulation());

        // Bouton Ajouter Satellite
        JButton btnAjouterSat = new JButton("+ Satellite");
        btnAjouterSat.addActionListener(e -> controller.ajouterSatelliteAleatoire());

        // Bouton Ajouter Balise
        JButton btnAjouterBalise = new JButton("+ Balise");
        btnAjouterBalise.addActionListener(e -> controller.ajouterBaliseAleatoire());

        // Slider de vitesse
        JLabel labelVitesse = new JLabel("Vitesse:");
        labelVitesse.setForeground(Color.WHITE);
        JSlider sliderVitesse = new JSlider(1, 10, 5);
        sliderVitesse.addChangeListener(e ->
                controller.setVitesseSimulation(sliderVitesse.getValue())
        );

        panel.add(btnDemarrer);
        panel.add(btnPause);
        panel.add(btnStop);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnAjouterSat);
        panel.add(btnAjouterBalise);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(labelVitesse);
        panel.add(sliderVitesse);

        return panel;
    }

    private JPanel creerPanelInfo() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setPreferredSize(new Dimension(0, 40));

        JLabel titre = new JLabel("🛰️ Simulation Satellites et Balises 🌊");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(Color.CYAN);

        panel.add(titre);

        return panel;
    }
}