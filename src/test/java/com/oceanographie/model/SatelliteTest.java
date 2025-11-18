package com.oceanographie.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import src.main.java.com.oceanographie.model.Satellite;
import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;

/**
 * Tests unitaires pour la classe Satellite
 */
public class SatelliteTest {
    
    private Satellite satellite;
    private Position positionInitiale;
    
    @BeforeEach
    void setUp() {
        positionInitiale = new Position(100.0, 50.0, 0.0);
        satellite = new Satellite("SAT-1", positionInitiale, 50.0);
    }
    
    @AfterEach
    void tearDown() {
        if (satellite != null) {
            satellite.stop();
        }
    }
    
    @Test
    @DisplayName("Test création satellite avec ID, position et hauteur")
    void testCreationSatellite() {
        assertNotNull(satellite);
        assertEquals("SAT-1", satellite.getId());
        assertEquals(positionInitiale, satellite.getPosition());
        assertEquals(50.0, satellite.getHauteur());
    }
    
    @Test
    @DisplayName("Test état initial disponible")
    void testEtatInitialDisponible() {
        assertTrue(satellite.isDisponible());
    }
    
    @Test
    @DisplayName("Test changement de disponibilité")
    void testChangementDisponibilite() {
        satellite.setDisponible(false);
        assertFalse(satellite.isDisponible());
        
        satellite.setDisponible(true);
        assertTrue(satellite.isDisponible());
    }
    
    @Test
    @DisplayName("Test vitesse par défaut du satellite")
    void testVitesseParDefaut() {
        // La vitesse par défaut est 5.0
        assertEquals(5.0, satellite.getVitesse());
    }
    
    @Test
    @DisplayName("Test rayon de couverture par défaut")
    void testRayonCouvertureParDefaut() {
        assertEquals(100.0, satellite.getRayonCouverture());
    }
    
    @Test
    @DisplayName("Test modification rayon de couverture")
    void testModificationRayonCouverture() {
        satellite.setRayonCouverture(150.0);
        assertEquals(150.0, satellite.getRayonCouverture());
    }
    
    @Test
    @DisplayName("Test déplacement horizontal du satellite")
    void testDeplacementHorizontal() {
        double xInitial = satellite.getPosition().getX();
        
        satellite.start();
        satellite.deplacer();
        
        double xFinal = satellite.getPosition().getX();
        
        // Le satellite devrait s'être déplacé
        assertNotEquals(xInitial, xFinal);
    }
    
    @Test
    @DisplayName("Test satellite se déplace vers la droite initialement")
    void testDeplacementVersDroite() {
        double xInitial = satellite.getPosition().getX();
        
        satellite.start();
        satellite.deplacer();
        
        // Devrait augmenter (vers la droite)
        assertTrue(satellite.getPosition().getX() > xInitial);
    }
    
    @Test
    @DisplayName("Test rebond au bord droit de l'écran")
    void testRebondBordDroit() {
        // Placer le satellite près du bord droit
        Position procheDroite = new Position(995.0, 50.0, 0.0);
        satellite.setPosition(procheDroite);
        
        satellite.start();
        
        // Se déplacer plusieurs fois
        for (int i = 0; i < 5; i++) {
            satellite.deplacer();
        }
        
        // Ne devrait pas dépasser 1000
        assertTrue(satellite.getPosition().getX() <= 1000.0);
    }
    
    @Test
    @DisplayName("Test rebond au bord gauche de l'écran")
    void testRebondBordGauche() {
        // Placer le satellite près du bord gauche, direction gauche
        Position procheGauche = new Position(5.0, 50.0, 0.0);
        satellite.setPosition(procheGauche);
        
        // Forcer direction gauche en déplaçant d'abord vers la droite puis inverser
        satellite.start();
        
        for (int i = 0; i < 100; i++) {
            satellite.deplacer();
        }
        
        // Ne devrait pas être négatif
        assertTrue(satellite.getPosition().getX() >= 0.0);
    }
    
    @Test
    @DisplayName("Test altitude constante du satellite")
    void testAltitudeConstante() {
        double yInitial = satellite.getPosition().getY();
        
        satellite.start();
        for (int i = 0; i < 10; i++) {
            satellite.deplacer();
        }
        
        // Y devrait rester constant
        assertEquals(yInitial, satellite.getPosition().getY());
    }
    
    @Test
    @DisplayName("Test satellite au-dessus d'une balise")
    void testEstAuDessusDe() {
        // Créer une balise à proximité
        Position positionBalise = new Position(120.0, 0.0, -200.0);
        Balise balise = new Balise("B-1", positionBalise, -200.0, null);
        
        // Distance = sqrt((120-100)² + (0-50)²) = sqrt(400 + 2500) = ~53.85
        // Rayon couverture = 100, donc devrait être au-dessus
        assertTrue(satellite.estAuDessusDe(balise));
    }
    
    @Test
    @DisplayName("Test satellite pas au-dessus d'une balise éloignée")
    void testPasAuDessusDe() {
        // Créer une balise loin
        Position positionBalise = new Position(500.0, 0.0, -200.0);
        Balise balise = new Balise("B-1", positionBalise, -200.0, null);
        
        // Distance > 100, donc pas au-dessus
        assertFalse(satellite.estAuDessusDe(balise));
    }
    
    @Test
    @DisplayName("Test commencer transfert avec balise")
    void testCommencerTransfert() {
        Position positionBalise = new Position(100.0, 0.0, 0.0);
        Balise balise = new Balise("B-1", positionBalise, -200.0, null);
        
        assertTrue(satellite.isDisponible());
        
        satellite.commencerTransfert(balise);
        
        assertFalse(satellite.isDisponible());
    }
    
    @Test
    @DisplayName("Test terminer transfert")
    void testTerminerTransfert() {
        Position positionBalise = new Position(100.0, 0.0, 0.0);
        Balise balise = new Balise("B-1", positionBalise, -200.0, null);
        
        satellite.commencerTransfert(balise);
        assertFalse(satellite.isDisponible());
        
        satellite.terminerTransfert();
        assertTrue(satellite.isDisponible());
    }
    
    @Test
    @DisplayName("Test ID de satellite unique")
    void testIdSatelliteUnique() {
        Satellite satellite2 = new Satellite("SAT-2", positionInitiale, 50.0);
        assertNotEquals(satellite.getId(), satellite2.getId());
    }
    
    @Test
    @DisplayName("Test format ID de satellite")
    void testFormatIdSatellite() {
        assertTrue(satellite.getId().startsWith("SAT-"));
    }
    
    @Test
    @DisplayName("Test satellite inactive ne se déplace pas")
    void testSatelliteInactif() {
        double xInitial = satellite.getPosition().getX();
        
        // Sans start(), satellite inactif
        satellite.deplacer();
        
        // Position ne change pas
        assertEquals(xInitial, satellite.getPosition().getX());
    }
    
    @Test
    @DisplayName("Test start/stop du satellite")
    void testStartStop() {
        assertFalse(satellite.isActif());
        
        satellite.start();
        assertTrue(satellite.isActif());
        
        satellite.stop();
        assertFalse(satellite.isActif());
    }
    
    @Test
    @DisplayName("Test modification de la hauteur")
    void testModificationHauteur() {
        satellite.setHauteur(100.0);
        assertEquals(100.0, satellite.getHauteur());
    }
    
    @Test
    @DisplayName("Test modification de la vitesse")
    void testModificationVitesse() {
        satellite.setVitesse(10.0);
        assertEquals(10.0, satellite.getVitesse());
        
        double xInitial = satellite.getPosition().getX();
        satellite.start();
        satellite.deplacer();
        
        // Devrait se déplacer plus vite
        assertEquals(xInitial + 10.0, satellite.getPosition().getX(), 0.1);
    }
}
