package src.main.java.com.oceanographie.view;

import src.main.java.com.oceanographie.controller.SimulationController;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private SimulationPanel simulationPanel;
    private JPanel controlPanel;
    private JPanel statsPanel;
    private SimulationController controller;
    private JLabel labelNbSatellites;
    private JLabel labelNbBalises;
    private JLabel labelNbSyncs;

    public MainWindow(SimulationController controller) {
        this.controller = controller;

        setTitle("🛰️ Simulation Satellites et Balises 🌊");
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

        // Panel de statistiques (droite)
        statsPanel = creerPanelStats();
        add(statsPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Timer pour mettre à jour les stats
        Timer statsTimer = new Timer(1000, e -> mettreAJourStats());
        statsTimer.start();
    }

    private JPanel creerPanelControle() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 40, 40));
        panel.setPreferredSize(new Dimension(0, 70));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Boutons de contrôle simulation
        JButton btnDemarrer = creerBouton("▶ Démarrer", new Color(46, 204, 113));
        btnDemarrer.addActionListener(e -> controller.demarrerSimulation());

        JButton btnPause = creerBouton("⏸ Pause", new Color(241, 196, 15));
        btnPause.addActionListener(e -> controller.pauseSimulation());

        JButton btnStop = creerBouton("⏹ Stop", new Color(231, 76, 60));
        btnStop.addActionListener(e -> controller.stopSimulation());

        // Séparateur
        JSeparator sep1 = new JSeparator(SwingConstants.VERTICAL);
        sep1.setPreferredSize(new Dimension(2, 40));
        sep1.setForeground(Color.GRAY);

        // Boutons d'ajout
        JButton btnAjouterSat = creerBouton("🛰️ + Satellite", new Color(52, 152, 219));
        btnAjouterSat.addActionListener(e -> {
            controller.ajouterSatelliteAleatoire();
            mettreAJourStats();
        });

        JButton btnAjouterBalise = creerBouton("🔵 + Balise", new Color(26, 188, 156));
        btnAjouterBalise.addActionListener(e -> {
            controller.ajouterBaliseAleatoire();
            mettreAJourStats();
        });

        // Séparateur
        JSeparator sep2 = new JSeparator(SwingConstants.VERTICAL);
        sep2.setPreferredSize(new Dimension(2, 40));
        sep2.setForeground(Color.GRAY);

        // Slider de vitesse
        JLabel labelVitesse = new JLabel("Vitesse:");
        labelVitesse.setForeground(Color.WHITE);
        labelVitesse.setFont(new Font("Arial", Font.BOLD, 12));

        JSlider sliderVitesse = new JSlider(1, 10, 5);
        sliderVitesse.setPreferredSize(new Dimension(150, 40));
        sliderVitesse.setPaintTicks(true);
        sliderVitesse.setPaintLabels(true);
        sliderVitesse.setMajorTickSpacing(3);
        sliderVitesse.setMinorTickSpacing(1);
        sliderVitesse.setBackground(new Color(40, 40, 40));
        sliderVitesse.setForeground(Color.WHITE);
        sliderVitesse.addChangeListener(e ->
                controller.setVitesseSimulation(sliderVitesse.getValue())
        );

        panel.add(btnDemarrer);
        panel.add(btnPause);
        panel.add(btnStop);
        panel.add(sep1);
        panel.add(btnAjouterSat);
        panel.add(btnAjouterBalise);
        panel.add(sep2);
        panel.add(labelVitesse);
        panel.add(sliderVitesse);

        return panel;
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleur.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleur);
            }
        });

        return btn;
    }

    private JPanel creerPanelInfo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(0, 50));

        JLabel titre = new JLabel("🛰️ Simulation Satellites et Balises 🌊");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(new Color(236, 240, 241));

        panel.add(titre);

        return panel;
    }

    private JPanel creerPanelStats() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(44, 62, 80));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Titre
        JLabel titreStats = new JLabel("📊 Statistiques");
        titreStats.setFont(new Font("Arial", Font.BOLD, 16));
        titreStats.setForeground(Color.WHITE);
        titreStats.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titreStats);

        panel.add(Box.createVerticalStrut(20));

        // Labels de stats
        labelNbSatellites = creerLabelStat("Satellites: 0");
        labelNbBalises = creerLabelStat("Balises: 0");
        labelNbSyncs = creerLabelStat("Synchronisations: 0");

        panel.add(labelNbSatellites);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelNbBalises);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelNbSyncs);

        panel.add(Box.createVerticalStrut(30));

        // Légende des couleurs
        JLabel titreLegend = new JLabel("🎨 Légende");
        titreLegend.setFont(new Font("Arial", Font.BOLD, 14));
        titreLegend.setForeground(Color.WHITE);
        titreLegend.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titreLegend);

        panel.add(Box.createVerticalStrut(10));

        panel.add(creerLigneLegend("🔵 Cyan", "Collecte"));
        panel.add(creerLigneLegend("🟡 Jaune", "Remontée"));
        panel.add(creerLigneLegend("🟢 Vert", "En surface"));
        panel.add(creerLigneLegend("🔴 Rouge", "Transfert"));

        return panel;
    }

    private JLabel creerLabelStat(String texte) {
        JLabel label = new JLabel(texte);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(236, 240, 241));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel creerLigneLegend(String couleur, String description) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        ligne.setBackground(new Color(44, 62, 80));
        ligne.setMaximumSize(new Dimension(180, 25));

        JLabel label = new JLabel(couleur + " " + description);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(new Color(189, 195, 199));

        ligne.add(label);
        return ligne;
    }

    private void mettreAJourStats() {
        int nbSat = controller.getNombreSatellites();
        int nbBalises = controller.getNombreBalises();
        int nbSyncs = controller.getNombreSynchronisations();

        labelNbSatellites.setText("🛰️ Satellites: " + nbSat);
        labelNbBalises.setText("🔵 Balises: " + nbBalises);
        labelNbSyncs.setText("🔗 Synchronisations: " + nbSyncs);
    }
}