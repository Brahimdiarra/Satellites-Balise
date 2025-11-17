package src.main.java.com.oceanographie.model;


import src.main.java.com.oceanographie.model.deplacement.StrategieDeplacementBalise;
import src.main.java.com.oceanographie.model.observer.Observable;
import src.main.java.com.oceanographie.model.observer.Observateur;

import java.util.ArrayList;
import java.util.List;

public class Balise extends ElementMobile  implements Observable{
    private double profondeur;
    private EtatBalise etat;
    private long tempsCollecte;
    private long debutCollecte;
    private long dureeCollecte = 30000;
    private StrategieDeplacementBalise strategieDeplacement;
    private List<Observateur> observateurs = new ArrayList<>();
    private Satellite satelliteConnecte;

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
        for(Observateur obs : observateurs){
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

    public Balise(String id, Position position, double profondeur , StrategieDeplacementBalise strategieDeplacement) {
        super(id, position, 2.0);
        this.profondeur = profondeur;
        this.etat = EtatBalise.COLLECTE;
        this.debutCollecte = System.currentTimeMillis();
        this.strategieDeplacement = strategieDeplacement;
        this.satelliteConnecte = null;
    }

    @Override
    public void deplacer() {
        if (!actif) return;

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
        this.etat = nouvelEtat;
        System.out.println("Balise " + id + " : " + etat);
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
    }

    public void terminerTransfert() {
        this.satelliteConnecte = null;
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
    public void setStrategieDeplacement(  StrategieDeplacementBalise strategieDeplacement) {
        this.strategieDeplacement = strategieDeplacement;
    }

    public void setSatelliteConnecte(Satellite satelliteConnecte) {
        this.satelliteConnecte = satelliteConnecte;
    }
}