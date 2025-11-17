package src.main.java.com;

import javax.swing.SwingUtilities;


import src.main.java.com.oceanographie.controller.SimulationController;
import src.main.java.com.oceanographie.view.MainWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("=== 🛰️ SIMULATION SATELLITES ET BALISES 🌊 ===");
            System.out.println("Démarrage de l'application...\n");

            // Créer le contrôleur
            SimulationController controller = new SimulationController();

            // ✅ Définir la durée du transfert (20 secondes)
            controller.setDureeTransfert(20);

            // Créer la fenêtre principale
            MainWindow window = new MainWindow(controller);

            System.out.println("✅ Application prête !\n");
            System.out.println("📌 Instructions:");
            System.out.println("   1. Utilisez '🛰️ + Satellite' pour ajouter des satellites");
            System.out.println("   2. Utilisez '🔵 + Balise' pour ajouter des balises");
            System.out.println("   3. Cliquez sur '▶ Démarrer' pour lancer la simulation");
            System.out.println("   4. Ajustez la vitesse avec le slider\n");
            System.out.println("🎨 Les balises changent de couleur selon leur état:");
            System.out.println("   🔵 Cyan   → Collecte de données");
            System.out.println("   🟡 Jaune  → Remontée vers la surface");
            System.out.println("   🟢 Vert   → En surface, attend un satellite");
            System.out.println("   🔴 Rouge  → Transfert de données en cours");
            System.out.println("\n" + "=".repeat(50));
        });
    }
}