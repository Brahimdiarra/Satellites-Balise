package com.oceanographie.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Position;
import src.main.java.com.oceanographie.model.deplacement.StrategieDeplacementBalise;

/**
 * Tests unitaires pour la classe Balise
 */
public class BaliseTest {
    
    private Balise balise;
    private Position positionInitiale;
    private StrategieDeplacementBalise strategie;
    
    @BeforeEach
    void setUp() {
        positionInitiale = new Position(100.0, 0.0, -200.0);
        // Stratégie nulle pour les tests simples (pas de déplacement)
        strategie = null;
        balise = new Balise("B-1", positionInitiale, -200.0, strategie);
    }
    
    @AfterEach
    void tearDown() {
        if (balise != null) {
            balise.stop();
        }
    }
    
    @Test
    @DisplayName("Test création balise avec tous les paramètres")
    void testCreationBalise() {
        assertNotNull(balise);
        assertEquals("B-1", balise.getId());
        assertEquals(positionInitiale, balise.getPosition());
        assertEquals(-200.0, balise.getProfondeur());
    }
    
    @Test
    @DisplayName("Test état initial de la balise (COLLECTE)")
    void testEtatInitial() {
        assertEquals(Balise.EtatBalise.COLLECTE, balise.getEtat());
    }
    
    @Test
    @DisplayName("Test changement d'état COLLECTE -> REMONTEE")
    void testChangementEtatRemontee() {
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        assertEquals(Balise.EtatBalise.REMONTEE, balise.getEtat());
    }
    
    @Test
    @DisplayName("Test changement d'état REMONTEE -> EN_SURFACE")
    void testChangementEtatSurface() {
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        balise.changerEtat(Balise.EtatBalise.EN_SURFACE);
        assertEquals(Balise.EtatBalise.EN_SURFACE, balise.getEtat());
    }
    
    @Test
    @DisplayName("Test changement d'état EN_SURFACE -> SYNCHRONISATION")
    void testChangementEtatSynchronisation() {
        balise.changerEtat(Balise.EtatBalise.EN_SURFACE);
        balise.changerEtat(Balise.EtatBalise.SYNCHRONISATION);
        assertEquals(Balise.EtatBalise.SYNCHRONISATION, balise.getEtat());
    }
    
    @Test
    @DisplayName("Test changement d'état SYNCHRONISATION -> TRANSFERT")
    void testChangementEtatTransfert() {
        balise.changerEtat(Balise.EtatBalise.SYNCHRONISATION);
        balise.changerEtat(Balise.EtatBalise.TRANSFERT);
        assertEquals(Balise.EtatBalise.TRANSFERT, balise.getEtat());
    }
    
    @Test
    @DisplayName("Test cycle complet des états")
    void testCycleCompletEtats() {
        assertEquals(Balise.EtatBalise.COLLECTE, balise.getEtat());
        
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        assertEquals(Balise.EtatBalise.REMONTEE, balise.getEtat());
        
        balise.changerEtat(Balise.EtatBalise.EN_SURFACE);
        assertEquals(Balise.EtatBalise.EN_SURFACE, balise.getEtat());
        
        balise.changerEtat(Balise.EtatBalise.SYNCHRONISATION);
        assertEquals(Balise.EtatBalise.SYNCHRONISATION, balise.getEtat());
        
        balise.changerEtat(Balise.EtatBalise.TRANSFERT);
        assertEquals(Balise.EtatBalise.TRANSFERT, balise.getEtat());
    }
    
    @Test
    @DisplayName("Test modification de la position")
    void testModificationPosition() {
        Position nouvellePosition = new Position(200.0, 0.0, -100.0);
        balise.setPosition(nouvellePosition);
        assertEquals(nouvellePosition, balise.getPosition());
    }
    
    @Test
    @DisplayName("Test balise en profondeur")
    void testBaliseEnProfondeur() {
        assertTrue(balise.getPosition().getZ() < 0);
        assertTrue(balise.getProfondeur() < 0);
    }
    
    @Test
    @DisplayName("Test remontée vers la surface")
    void testRemonteeVersSurface() {
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        balise.start();
        
        double profondeurInitiale = balise.getPosition().getZ();
        
        // Simuler plusieurs déplacements
        for (int i = 0; i < 10; i++) {
            balise.deplacer();
        }
        
        // La balise devrait avoir remonté
        assertTrue(balise.getPosition().getZ() > profondeurInitiale);
    }
    
    @Test
    @DisplayName("Test balise atteint la surface (Z = 0)")
    void testBaliseAtteintSurface() {
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        balise.start();
        
        // Remonter jusqu'à la surface
        while (balise.getPosition().getZ() < 0 && 
               balise.getEtat() == Balise.EtatBalise.REMONTEE) {
            balise.deplacer();
        }
        
        // Vérifier qu'elle est en surface
        assertEquals(0.0, balise.getPosition().getZ(), 0.1);
        assertEquals(Balise.EtatBalise.EN_SURFACE, balise.getEtat());
    }
    
    @Test
    @DisplayName("Test ID de balise unique")
    void testIdBaliqueUnique() {
        Balise balise2 = new Balise("B-2", positionInitiale, -200.0, strategie);
        assertNotEquals(balise.getId(), balise2.getId());
    }
    
    @Test
    @DisplayName("Test format ID de balise")
    void testFormatIdBalise() {
        assertTrue(balise.getId().startsWith("B-"));
    }
    
    @Test
    @DisplayName("Test durée de collecte par défaut")
    void testDureeCollecte() {
        assertEquals(9000, balise.getDureeCollecte());
    }
    
    @Test
    @DisplayName("Test modification durée de collecte")
    void testModificationDureeCollecte() {
        balise.setDureeCollecte(5000);
        assertEquals(5000, balise.getDureeCollecte());
    }
    
    @Test
    @DisplayName("Test recommencer collecte")
    void testRecommencerCollecte() {
        balise.changerEtat(Balise.EtatBalise.EN_SURFACE);
        balise.recommencerCollecte();
        
        assertEquals(Balise.EtatBalise.COLLECTE, balise.getEtat());
    }
    
    @Test
    @DisplayName("Test changement de stratégie")
    void testChangementStrategie() {
        // Mock d'une stratégie
        StrategieDeplacementBalise nouvelleStrategie = new StrategieDeplacementBalise() {
            @Override
            public void appliquerDeplacement(Balise balise) {
                // Stratégie de test
            }
        };
        
        balise.setStrategieDeplacement(nouvelleStrategie);
        assertEquals(nouvelleStrategie, balise.getStrategieDeplacement());
    }
    
    @Test
    @DisplayName("Test vitesse de la balise")
    void testVitesseBalise() {
        // La vitesse est héritée d'ElementMobile et fixée à 2.0
        assertEquals(2.0, balise.getVitesse());
    }
    
    @Test
    @DisplayName("Test balise inactive ne se déplace pas")
    void testBaliseInactive() {
        double zInitial = balise.getPosition().getZ();
        balise.changerEtat(Balise.EtatBalise.REMONTEE);
        
        // Sans start(), balise inactive
        balise.deplacer();
        
        // Position ne change pas
        assertEquals(zInitial, balise.getPosition().getZ());
    }
    
    @Test
    @DisplayName("Test start/stop de la balise")
    void testStartStop() {
        assertFalse(balise.isActif());
        
        balise.start();
        assertTrue(balise.isActif());
        
        balise.stop();
        assertFalse(balise.isActif());
    }
}
