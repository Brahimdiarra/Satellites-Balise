package src.main.java.com.oceanographie.model;

import src.main.java.com.oceanographie.model.deplacement.StrategieDeplacementBalise;
import src.main.java.com.oceanographie.model.observer.Observable;
import src.main.java.com.oceanographie.model.observer.Observateur;
import src.main.java.com.eventHandler.EventHandler;
import src.main.java.com.oceanographie.events.BaliseEvent;
import src.main.java.com.oceanographie.events.SynchronisationEvent;

import java.util.ArrayList;
import java.util.List;

public class Balise extends ElementMobile implements Observable {
    private double profondeur;
    private EtatBalise etat;
    private long tempsCollecte;
    private long debutCollecte;
    private long dureeCollecte = 9000;
    private StrategieDeplacementBalise strategieDeplacement;
    private List<Observateur> observateurs = new ArrayList<>();
    private Satellite satelliteConnecte;
    private EventHandler eventHandler;
    // pour notifier les observateurs seulement si la position a changé suffisamment
    private double lastNotifiedX;
    private double lastNotifiedY;
    private double lastNotifiedZ;
    private static final double MOVEMENT_NOTIFY_THRESHOLD = 0.1; // en unités de position

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

    public enum EtatBalise {
        COLLECTE,
        REMONTEE,
        EN_SURFACE,
        SYNCHRONISATION,
        TRANSFERT
    }

    public Balise(String id, Position position, double profondeur, StrategieDeplacementBalise strategieDeplacement) {
        super(id, position, 2.0);
        this.profondeur = profondeur;
        this.etat = EtatBalise.COLLECTE;
        this.debutCollecte = System.currentTimeMillis();
        this.strategieDeplacement = strategieDeplacement;
        this.satelliteConnecte = null;
        // initialiser le dernier point notifié
        if (position != null) {
            this.lastNotifiedX = position.getX();
            this.lastNotifiedY = position.getY();
            this.lastNotifiedZ = position.getZ();
        }
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void deplacer() {
        if (!actif)
            return;
        // position avant déplacement
        double beforeX = position.getX();
        double beforeY = position.getY();
        double beforeZ = position.getZ();

        switch (etat) {
            case COLLECTE:
                if (strategieDeplacement != null) {
                    strategieDeplacement.appliquerDeplacement(this);
                }
                verifierTempsCollecte();
                break;

            case REMONTEE:
                remonter();
                break;

            case EN_SURFACE:

                break;

            case SYNCHRONISATION:
            case TRANSFERT:
                break;
        }

        // notifier les observateurs si la position a changé au-delà du seuil
        double dx = Math.abs(position.getX() - lastNotifiedX);
        double dy = Math.abs(position.getY() - lastNotifiedY);
        double dz = Math.abs(position.getZ() - lastNotifiedZ);
        if (dx > MOVEMENT_NOTIFY_THRESHOLD || dy > MOVEMENT_NOTIFY_THRESHOLD || dz > MOVEMENT_NOTIFY_THRESHOLD) {
            // mettre à jour le dernier point notifié
            lastNotifiedX = position.getX();
            lastNotifiedY = position.getY();
            lastNotifiedZ = position.getZ();
            notifierObservateurs();
        }
    }

    private void verifierTempsCollecte() {
        long maintenant = System.currentTimeMillis();
        if (maintenant - debutCollecte >= dureeCollecte) {
            changerEtat(EtatBalise.REMONTEE);
        }
    }

    private void remonter() {
        // Remonter progressivement
        position.setZ(position.getZ() + 1.0);
        if (position.getZ() >= 0) {
            position.setZ(0); // Surface
            changerEtat(EtatBalise.EN_SURFACE);
        }
    }

    public void changerEtat(EtatBalise nouvelEtat) {
        EtatBalise ancien = this.etat;
        this.etat = nouvelEtat;
        System.out.println("Balise " + id + " : " + etat);
        // envoyer l'événement via EventHandler si disponible
        if (this.eventHandler != null) {
            this.eventHandler.send(new BaliseEvent(this, ancien, nouvelEtat));
        }
        // compatibilité avec l'ancien système d'observateurs
        notifierObservateurs();
    }

    public void recommencerCollecte() {
        this.debutCollecte = System.currentTimeMillis();
        changerEtat(EtatBalise.COLLECTE);
    }

    // les deux fonctions pour la gestion de transfer de donnees

    public void commencerTransfert(Satellite satellite) {
        this.satelliteConnecte = satellite;
        changerEtat(EtatBalise.TRANSFERT);
        if (this.eventHandler != null) {
            this.eventHandler.send(new SynchronisationEvent(this, satellite, SynchronisationEvent.TypeSync.DEBUT));
        }
    }

    public void terminerTransfert() {
        Satellite sat = this.satelliteConnecte;
        this.satelliteConnecte = null;
        // envoyer événement FIN avant de recommencer
        if (this.eventHandler != null && sat != null) {
            this.eventHandler.send(new SynchronisationEvent(this, sat, SynchronisationEvent.TypeSync.FIN));
        }
        // Redescendre et recommencer la collecte
        recommencerCollecte();
    }

    // Getters/Setters

    public double getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    public EtatBalise getEtat() {
        return etat;
    }

    public void setEtat(EtatBalise etat) {
        this.etat = etat;
    }

    public long getTempsCollecte() {
        return tempsCollecte;
    }

    public void setTempsCollecte(long tempsCollecte) {
        this.tempsCollecte = tempsCollecte;
    }

    public long getDebutCollecte() {
        return debutCollecte;
    }

    public void setDebutCollecte(long debutCollecte) {
        this.debutCollecte = debutCollecte;
    }

    public long getDureeCollecte() {
        return dureeCollecte;
    }

    public void setDureeCollecte(long dureeCollecte) {
        this.dureeCollecte = dureeCollecte;
    }

    public StrategieDeplacementBalise getStrategieDeplacement() {
        return strategieDeplacement;
    }

    public void setStrategieDeplacement(StrategieDeplacementBalise strategieDeplacement) {
        this.strategieDeplacement = strategieDeplacement;
    }

    public void setSatelliteConnecte(Satellite satelliteConnecte) {
        this.satelliteConnecte = satelliteConnecte;
    }
}