package src.main.java.com.oceanographie.model;

import src.main.java.com.oceanographie.model.observer.Observable;
import src.main.java.com.oceanographie.model.observer.Observateur;
import src.main.java.com.eventHandler.EventHandler;
import src.main.java.com.oceanographie.events.SatelliteEvent;
import src.main.java.com.oceanographie.events.SynchronisationEvent;

import java.util.ArrayList;
import java.util.List;

public class Satellite extends ElementMobile implements Observable {
    private double hauteur; // Altitude au-dessus de la surface
    private boolean disponible;
    private double rayonCouverture; // Zone de détection
    private List<Observateur> observateurs = new ArrayList<>();
    private boolean versLaDroite;
    private Balise baliseConnectee;
    private EventHandler eventHandler;

    public Satellite(String id, Position position, double hauteur) {
        super(id, position, 5.0); // Vitesse par défaut
        this.hauteur = hauteur;
        this.disponible = true;
        this.rayonCouverture = 100.0; // Rayon de couverture
        this.versLaDroite = true;
        this.baliseConnectee = null;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
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


    /* les fonctions de transfert des donnes entre les balises et les sattelites*/

    public void commencerTransfert(Balise balise) {
        this.baliseConnectee = balise;
        this.disponible = false;
        // envoyer événements via EventHandler
        if (this.eventHandler != null) {
            this.eventHandler.send(new SynchronisationEvent(balise, this, SynchronisationEvent.TypeSync.DEBUT));
            this.eventHandler.send(new SatelliteEvent(this, false));
        }
        notifierObservateurs();
    }

    public void terminerTransfert() {
        Balise b = this.baliseConnectee;
        this.baliseConnectee = null;
        this.disponible = true;
        if (this.eventHandler != null && b != null) {
            this.eventHandler.send(new SynchronisationEvent(b, this, SynchronisationEvent.TypeSync.FIN));
            this.eventHandler.send(new SatelliteEvent(this, true));
        }
        notifierObservateurs();
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
        if (this.disponible != disponible) {
            this.disponible = disponible;
            System.out.println("🛰️ Satellite " + id + " : " +
                    (disponible ? "DISPONIBLE" : "OCCUPÉ"));
            if (this.eventHandler != null) {
                this.eventHandler.send(new SatelliteEvent(this, disponible));
            }
            notifierObservateurs();
        }
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