package com.oceanographie.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import src.main.java.com.oceanographie.model.Satellite;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.view.component.VueSatellite;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Tests unitaires pour la classe VueSatellite
 */
public class VueSatelliteTest {
    
    private VueSatellite vueSatellite;
    private Satellite satellite;
    private Position position;
    
    @BeforeEach
    void setUp() {
        position = new Position(100.0, 50.0, 0.0);
        satellite = new Satellite("SAT-1", position, 50.0);
        vueSatellite = new VueSatellite(satellite);
    }
    
    @Test
    @DisplayName("Test création de la vue satellite")
    void testCreationVueSatellite() {
        assertNotNull(vueSatellite);
    }
    
    @Test
    @DisplayName("Test récupération du satellite associé")
    void testGetSatellite() {
        assertEquals(satellite, vueSatellite.getSatellite());
    }
    
    @Test
    @DisplayName("Test mise à jour de la position")
    void testMettreAJour() {
        // Changer la position du satellite
        Position nouvellePosition = new Position(200.0, 60.0, 0.0);
        satellite.setPosition(nouvellePosition);
        
        // Mettre à jour la vue
        vueSatellite.mettreAJour();
        
        // Vérifier que la position de la vue correspond
        Point positionVue = vueSatellite.getPosition();
        assertEquals(200, positionVue.x);
        assertEquals(60, positionVue.y);
    }
    
    @Test
    @DisplayName("Test position initiale de la vue")
    void testPositionInitiale() {
        Point positionVue = vueSatellite.getPosition();
        assertNotNull(positionVue);
        assertEquals(100, positionVue.x);
        assertEquals(50, positionVue.y);
    }
    
    @Test
    @DisplayName("Test changement de disponibilité")
    void testSetDisponible() {
        // Changer la disponibilité du satellite
        satellite.setDisponible(false);
        vueSatellite.setDisponible(false);
        
        // Vérifier que le satellite est bien indisponible
        assertFalse(satellite.isDisponible());
    }
    
    @Test
    @DisplayName("Test dessin de la vue (ne doit pas lancer d'exception)")
    void testDessiner() {
        // Créer un Graphics2D pour tester
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Dessiner ne doit pas lancer d'exception
        assertDoesNotThrow(() -> vueSatellite.dessiner(g2d));
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test dessin avec satellite disponible")
    void testDessinerDisponible() {
        satellite.setDisponible(true);
        
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        assertDoesNotThrow(() -> vueSatellite.dessiner(g2d));
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test dessin avec satellite occupé")
    void testDessinerOccupe() {
        satellite.setDisponible(false);
        
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        assertDoesNotThrow(() -> vueSatellite.dessiner(g2d));
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test dessin avec image chargée")
    void testDessinerAvecImage() {
        // Le constructeur charge l'image automatiquement si elle existe
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Dessiner avec ou sans image ne doit pas planter
        assertDoesNotThrow(() -> vueSatellite.dessiner(g2d));
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test multiple mises à jour successives")
    void testMultiplesMisesAJour() {
        for (int i = 0; i < 10; i++) {
            Position nouvellePosition = new Position(100.0 + i * 10, 50.0, 0.0);
            satellite.setPosition(nouvellePosition);
            
            assertDoesNotThrow(() -> vueSatellite.mettreAJour());
            
            Point positionVue = vueSatellite.getPosition();
            assertEquals(100 + i * 10, positionVue.x);
        }
    }
    
    @Test
    @DisplayName("Test position dans les limites de l'écran")
    void testPositionDansLimites() {
        Position positionLimite = new Position(1000.0, 200.0, 0.0);
        satellite.setPosition(positionLimite);
        vueSatellite.mettreAJour();
        
        Point positionVue = vueSatellite.getPosition();
        assertTrue(positionVue.x >= 0);
        assertTrue(positionVue.x <= 1000);
        assertTrue(positionVue.y >= 0);
        assertTrue(positionVue.y <= 600);
    }
    
    @Test
    @DisplayName("Test dessin à différentes positions")
    void testDessinerDifferentesPositions() {
        BufferedImage image = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Tester plusieurs positions
        int[][] positions = {{100, 50}, {500, 100}, {900, 150}};
        
        for (int[] pos : positions) {
            Position nouvellePos = new Position(pos[0], pos[1], 0.0);
            satellite.setPosition(nouvellePos);
            vueSatellite.mettreAJour();
            
            assertDoesNotThrow(() -> vueSatellite.dessiner(g2d));
        }
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test dessin avec changements d'état")
    void testDessinerAvecChangementsEtat() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Disponible
        satellite.setDisponible(true);
        assertDoesNotThrow(() -> vueSatellite.dessiner(g2d));
        
        // Occupé
        satellite.setDisponible(false);
        assertDoesNotThrow(() -> vueSatellite.dessiner(g2d));
        
        // Redevient disponible
        satellite.setDisponible(true);
        assertDoesNotThrow(() -> vueSatellite.dessiner(g2d));
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test position après déplacement du satellite")
    void testPositionApresDeplacement() {
        satellite.start();
        
        Point posInitiale = vueSatellite.getPosition();
        
        // Déplacer le satellite
        satellite.deplacer();
        vueSatellite.mettreAJour();
        
        Point posFinale = vueSatellite.getPosition();
        
        // La position devrait avoir changé
        assertNotEquals(posInitiale.x, posFinale.x);
    }
    
    @Test
    @DisplayName("Test dessin ne modifie pas la position")
    void testDessinerNeModifiePasPosition() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        Point posBefore = vueSatellite.getPosition();
        
        vueSatellite.dessiner(g2d);
        
        Point posAfter = vueSatellite.getPosition();
        
        assertEquals(posBefore.x, posAfter.x);
        assertEquals(posBefore.y, posAfter.y);
        
        g2d.dispose();
    }
}
