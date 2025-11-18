package com.oceanographie.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import src.main.java.com.oceanographie.model.Position;

/**
 * Tests unitaires pour la classe Position
 */
public class PositionTest {
    
    private Position position;
    
    @BeforeEach
    void setUp() {
        position = new Position(100.0, 200.0, -50.0);
    }
    
    @Test
    @DisplayName("Test création position avec coordonnées valides")
    void testCreationPositionValide() {
        assertNotNull(position);
        assertEquals(100.0, position.getX());
        assertEquals(200.0, position.getY());
        assertEquals(-50.0, position.getZ());
    }
    
    @Test
    @DisplayName("Test setters des coordonnées")
    void testSetters() {
        position.setX(150.0);
        position.setY(250.0);
        position.setZ(-100.0);
        
        assertEquals(150.0, position.getX());
        assertEquals(250.0, position.getY());
        assertEquals(-100.0, position.getZ());
    }
    
    @Test
    @DisplayName("Test position en surface (Z = 0)")
    void testPositionSurface() {
        Position surface = new Position(100.0, 200.0, 0.0);
        assertEquals(0.0, surface.getZ());
    }
    
    @Test
    @DisplayName("Test position en profondeur (Z négatif)")
    void testPositionProfondeur() {
        Position profondeur = new Position(100.0, 200.0, -300.0);
        assertTrue(profondeur.getZ() < 0);
    }
    
    @Test
    @DisplayName("Test distance 2D entre deux positions")
    void testDistance2D() {
        Position pos1 = new Position(0.0, 0.0, 0.0);
        Position pos2 = new Position(3.0, 4.0, 0.0);
        
        // Distance devrait être 5 (théorème de Pythagore: 3² + 4² = 5²)
        assertEquals(5.0, pos1.distance2D(pos2), 0.01);
    }
    
    @Test
    @DisplayName("Test distance 2D ignore Z")
    void testDistance2DIgnoreZ() {
        Position pos1 = new Position(0.0, 0.0, -100.0);
        Position pos2 = new Position(3.0, 4.0, -200.0);
        
        // La distance 2D ne prend pas en compte Z
        assertEquals(5.0, pos1.distance2D(pos2), 0.01);
    }
    
    @Test
    @DisplayName("Test distance 2D même position")
    void testDistance2DMemePosition() {
        Position pos1 = new Position(100.0, 200.0, -50.0);
        Position pos2 = new Position(100.0, 200.0, -50.0);
        
        assertEquals(0.0, pos1.distance2D(pos2), 0.01);
    }
    
    @Test
    @DisplayName("Test coordonnées limites")
    void testCoordonneesLimites() {
        Position limites = new Position(0.0, 0.0, 0.0);
        assertEquals(0.0, limites.getX());
        assertEquals(0.0, limites.getY());
        assertEquals(0.0, limites.getZ());
    }
    
    @Test
    @DisplayName("Test coordonnées négatives")
    void testCoordonneesNegatives() {
        Position negatives = new Position(-100.0, -200.0, -300.0);
        assertEquals(-100.0, negatives.getX());
        assertEquals(-200.0, negatives.getY());
        assertEquals(-300.0, negatives.getZ());
    }
    
    @Test
    @DisplayName("Test distance 2D avec grandes valeurs")
    void testDistance2DGrandesValeurs() {
        Position pos1 = new Position(0.0, 0.0, 0.0);
        Position pos2 = new Position(1000.0, 0.0, 0.0);
        
        assertEquals(1000.0, pos1.distance2D(pos2), 0.01);
    }
    
    @Test
    @DisplayName("Test distance 2D symétrique")
    void testDistance2DSymetrique() {
        Position pos1 = new Position(10.0, 20.0, 0.0);
        Position pos2 = new Position(40.0, 60.0, 0.0);
        
        // distance(A, B) = distance(B, A)
        assertEquals(pos1.distance2D(pos2), pos2.distance2D(pos1), 0.01);
    }
}
