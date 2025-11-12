package src.main.java.com.oceanographie.model;

import src.main.java.com.oceanographie.model.observer.Observable;
import src.main.java.com.oceanographie.model.observer.Observateur;

import java.util.ArrayList;
import java.util.List;

public class Satellite extends ElementMobile implements Observable {
    private double hauteur; // Altitude au-dessus de la surface
    private boolean disponible;
    private double rayonCouverture; // Zone de détection
    private List<Observateur> observateurs = new ArrayList<>();
    private boolean versLaDroite;

    public Satellite(String id, Position position, double hauteur) {
        super(id, position, 5.0); // Vitesse par défaut
        this.hauteur = hauteur;
        this.disponible = true;
        this.rayonCouverture = 100.0; // Rayon de couverture
        this.versLaDroite = true;
    }

    @Override
    public void deplacer() {
        if (!actif) return;

        // Limites de l'écran pour les satellites
        final double LIMITE_GAUCHE = 0;
        final double LIMITE_DROITE = 1000; // Ajuste selon ta largeur d'écran

        // Déplacement horizontal avec rebond
        if (versLaDroite) {
            position.setX(position.getX() + vitesse);

            // Si atteint le bord droit, inverser
            if (position.getX() >= LIMITE_DROITE) {
                position.setX(LIMITE_DROITE);
                versLaDroite = false;
            }
        } else {
            position.setX(position.getX() - vitesse);

            // Si atteint le bord gauche, inverser
            if (position.getX() <= LIMITE_GAUCHE) {
                position.setX(LIMITE_GAUCHE);
                versLaDroite = true;
            }
        }
    }

    public boolean estAuDessusDe(Balise balise) {
        // Vérifier si la balise est dans la zone de couverture
        double distance = this.position.distance2D(balise.getPosition());
        return distance <= rayonCouverture;
    }

    // Getters/Setters

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public double getRayonCouverture() {
        return rayonCouverture;
    }

    public void setRayonCouverture(double rayonCouverture) {
        this.rayonCouverture = rayonCouverture;
    }

    @Override
    public void ajouterObservateur(Observateur obs) {
        observateurs.add(obs);
    }

    @Override
    public void retirerObservateur(Observateur obs) {
     observateurs.remove(obs);
    }

    @Override
    public void notifierObservateurs() {
        for (Observateur obs : observateurs) {
            obs.actualiser(this);
        }
    }



}