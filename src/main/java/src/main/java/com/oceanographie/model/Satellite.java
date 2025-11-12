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

    public Satellite(String id, Position position, double hauteur) {
        super(id, position, 5.0); // Vitesse par défaut
        this.hauteur = hauteur;
        this.disponible = true;
        this.rayonCouverture = 100.0; // Rayon de couverture
    }

    @Override
    public void deplacer() {
        if (!actif) return;

        position.setX(position.getX() + vitesse);
        // Si sort de l'écran, revenir de l'autre côté
        if (position.getX() > 800) {
            position.setX(0);
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