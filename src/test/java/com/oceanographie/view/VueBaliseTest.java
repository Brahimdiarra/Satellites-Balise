package com.oceanographie.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.view.component.VueBalise;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Tests unitaires pour la classe VueBalise
 */
public class VueBaliseTest {
    
    private VueBalise vueBalise;
    private Balise balise;
    private Position position;
    
    @BeforeEach
    void setUp() {
        position = new Position(100.0, 0.0, -200.0);
        balise = new Balise("B-1", position, -200.0, null);
        vueBalise = new VueBalise(balise);
    }
    
    @Test
    @DisplayName("Test création de la vue balise")
    void testCreationVueBalise() {
        assertNotNull(vueBalise);
    }
    
    @Test
    @DisplayName("Test récupération de la balise associée")
    void testGetBalise() {
        assertEquals(balise, vueBalise.getBalise());
    }
    
    @Test
    @DisplayName("Test mise à jour de la position")
    void testMettreAJour() {
        // Changer la position de la balise
        Position nouvellePosition = new Position(200.0, 0.0, -100.0);
        balise.setPosition(nouvellePosition);
        
        // Mettre à jour la vue
        vueBalise.mettreAJour();
        
        // Vérifier que la position de la vue correspond
        Point positionVue = vueBalise.getPosition();
        assertEquals(200, positionVue.x);
        // Y = NIVEAU_MER + abs(Z) = 200 + 100 = 300
        assertEquals(300, positionVue.y);
    }
    
    @Test
    @DisplayName("Test position initiale de la vue")
    void testPositionInitiale() {
        Point positionVue = vueBalise.getPosition();
        assertNotNull(positionVue);
        assertEquals(100, positionVue.x);
        // Y = NIVEAU_MER + abs(-200) = 200 + 200 = 400
        assertEquals(400, positionVue.y);
    }
    
    @Test
    @DisplayName("Test changement d'état COLLECTE")
    void testChangementEtatCollecte() {
        balise.changerEtat(Balise.EtatBalise.COLLECTE);
        assertDoesNotThrow(() -> vueBalise.onEtatChanged(Balise.EtatBalise.COLLECTE));
    }
    
    @Test
    @DisplayName("Test changement d'état REMONTEE")
    void testChangementEtatRemontee() {
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        assertDoesNotThrow(() -> vueBalise.onEtatChanged(Balise.EtatBalise.REMONTEE));
    }
    
    @Test
    @DisplayName("Test changement d'état EN_SURFACE")
    void testChangementEtatSurface() {
        balise.changerEtat(Balise.EtatBalise.EN_SURFACE);
        assertDoesNotThrow(() -> vueBalise.onEtatChanged(Balise.EtatBalise.EN_SURFACE));
    }
    
    @Test
    @DisplayName("Test changement d'état TRANSFERT")
    void testChangementEtatTransfert() {
        balise.changerEtat(Balise.EtatBalise.TRANSFERT);
        assertDoesNotThrow(() -> vueBalise.onEtatChanged(Balise.EtatBalise.TRANSFERT));
    }
    
    @Test
    @DisplayName("Test dessin de la vue (ne doit pas lancer d'exception)")
    void testDessiner() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        assertDoesNotThrow(() -> vueBalise.dessiner(g2d));
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test dessin avec différents états")
    void testDessinerAvecDifferentsEtats() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Tester tous les états
        Balise.EtatBalise[] etats = Balise.EtatBalise.values();
        for (Balise.EtatBalise etat : etats) {
            balise.changerEtat(etat);
            vueBalise.onEtatChanged(etat);
            assertDoesNotThrow(() -> vueBalise.dessiner(g2d));
        }
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test dessin avec image chargée")
    void testDessinerAvecImage() {
        // Le constructeur charge l'image automatiquement si elle existe
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Dessiner avec ou sans image ne doit pas planter
        assertDoesNotThrow(() -> vueBalise.dessiner(g2d));
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test remontée vers la surface")
    void testRemonteeVersSurface() {
        // Position initiale en profondeur
        Position profondeur = new Position(100.0, 0.0, -300.0);
        balise.setPosition(profondeur);
        vueBalise.mettreAJour();
        
        Point positionInitiale = vueBalise.getPosition();
        int yInitial = positionInitiale.y;
        
        // Remonter
        Position surface = new Position(100.0, 0.0, -100.0);
        balise.setPosition(surface);
        vueBalise.mettreAJour();
        
        Point positionFinale = vueBalise.getPosition();
        int yFinal = positionFinale.y;
        
        // Y devrait diminuer (la balise remonte vers la surface)
        assertTrue(yFinal < yInitial);
    }
    
    @Test
    @DisplayName("Test balise en surface (Z = 0)")
    void testBaliseEnSurface() {
        Position surface = new Position(100.0, 0.0, 0.0);
        balise.setPosition(surface);
        vueBalise.mettreAJour();
        
        Point positionVue = vueBalise.getPosition();
        // Y devrait être au niveau de la mer (200)
        assertEquals(200, positionVue.y);
    }
    
    @Test
    @DisplayName("Test profondeur maximale")
    void testProfondeurMaximale() {
        Position profondeurMax = new Position(100.0, 0.0, -400.0);
        balise.setPosition(profondeurMax);
        vueBalise.mettreAJour();
        
        Point positionVue = vueBalise.getPosition();
        // Y = 200 + 400 = 600
        assertEquals(600, positionVue.y);
    }
    
    @Test
    @DisplayName("Test multiple changements d'état")
    void testMultipleChangementsEtat() {
        Balise.EtatBalise[] cycle = {
            Balise.EtatBalise.COLLECTE,
            Balise.EtatBalise.REMONTEE,
            Balise.EtatBalise.EN_SURFACE,
            Balise.EtatBalise.SYNCHRONISATION,
            Balise.EtatBalise.TRANSFERT
        };
        
        for (Balise.EtatBalise etat : cycle) {
            balise.changerEtat(etat);
            assertDoesNotThrow(() -> vueBalise.onEtatChanged(etat));
        }
    }
    
    @Test
    @DisplayName("Test position dans les limites de l'écran")
    void testPositionDansLimites() {
        Position positionLimite = new Position(1000.0, 0.0, -300.0);
        balise.setPosition(positionLimite);
        vueBalise.mettreAJour();
        
        Point positionVue = vueBalise.getPosition();
        assertTrue(positionVue.x >= 0);
        assertTrue(positionVue.x <= 1000);
        assertTrue(positionVue.y >= 200); // Au-dessus du niveau de la mer
        assertTrue(positionVue.y <= 600);
    }
    
    @Test
    @DisplayName("Test dessin à différentes positions")
    void testDessinerDifferentesPositions() {
        BufferedImage image = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Tester plusieurs profondeurs
        double[] profondeurs = {-50.0, -150.0, -300.0, 0.0};
        
        for (double z : profondeurs) {
            Position nouvellePos = new Position(100.0, 0.0, z);
            balise.setPosition(nouvellePos);
            vueBalise.mettreAJour();
            
            assertDoesNotThrow(() -> vueBalise.dessiner(g2d));
        }
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test dessin avec tous les états visuels")
    void testDessinerTousEtatsVisuels() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // État COLLECTE (Cyan)
        balise.changerEtat(Balise.EtatBalise.COLLECTE);
        assertDoesNotThrow(() -> vueBalise.dessiner(g2d));
        
        // État REMONTEE (Jaune)
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        assertDoesNotThrow(() -> vueBalise.dessiner(g2d));
        
        // État EN_SURFACE (Vert)
        balise.changerEtat(Balise.EtatBalise.EN_SURFACE);
        assertDoesNotThrow(() -> vueBalise.dessiner(g2d));
        
        // État TRANSFERT (Rouge)
        balise.changerEtat(Balise.EtatBalise.TRANSFERT);
        assertDoesNotThrow(() -> vueBalise.dessiner(g2d));
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test position après remontée progressive")
    void testPositionApresRemonteeProgressive() {
        balise.start();
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        
        Point posInitiale = vueBalise.getPosition();
        
        // Simuler plusieurs déplacements
        for (int i = 0; i < 10; i++) {
            balise.deplacer();
            vueBalise.mettreAJour();
        }
        
        Point posFinale = vueBalise.getPosition();
        
        // Y devrait avoir diminué (remontée)
        assertTrue(posFinale.y < posInitiale.y);
    }
    
    @Test
    @DisplayName("Test dessin ne modifie pas la position")
    void testDessinerNeModifiePasPosition() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        Point posBefore = vueBalise.getPosition();
        
        vueBalise.dessiner(g2d);
        
        Point posAfter = vueBalise.getPosition();
        
        assertEquals(posBefore.x, posAfter.x);
        assertEquals(posBefore.y, posAfter.y);
        
        g2d.dispose();
    }
}
