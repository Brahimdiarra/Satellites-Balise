package src.main.java.com.oceanographie.model.observer;

import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Satellite;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireSynchronisation implements Observateur {
    private List<Balise> balises;
    private List<Satellite> satellites;
    private long tempsTransfert = 10000;


    public GestionnaireSynchronisation() {
        balises = new ArrayList<Balise>();
        satellites= new ArrayList<Satellite>();
    }

    public void ajouterBalise(Balise balise) {
        balises.add(balise);
        balise.ajouterObservateur(this);
    }

    public void ajouterSatellite(Satellite satellite) {
        satellites.add(satellite);
        satellite.ajouterObservateur(this);
    }

    @Override
    public void actualiser(src.main.java.com.oceanographie.model.observer.Observable observable) {
        if (observable instanceof Balise) {
            Balise balise = (Balise) observable;
            if (balise.getEtat() == Balise.EtatBalise.EN_SURFACE) {
                tenterSynchronisation(balise);
            }
        }
    }

    public void verifierSynchronisations() {
        for (Balise balise : balises) {
            if (balise.getEtat() == Balise.EtatBalise.EN_SURFACE) {
                tenterSynchronisation(balise);
            }
        }
    }

    private void tenterSynchronisation(Balise balise) {
        for (Satellite satellite : satellites) {
            if (satellite.isDisponible() && satellite.estAuDessusDe(balise)) {
                synchroniser(balise, satellite);
                return;
            }
        }
    }

    private void synchroniser(Balise balise, Satellite satellite) {
        System.out.println("Synchronisation: " + balise.getId() +
                " <-> " + satellite.getId());

        balise.changerEtat(Balise.EtatBalise.SYNCHRONISATION);
        satellite.setDisponible(false);

        // Simuler le transfert avec un timer
        new Thread(() -> {
            try {
                Thread.sleep(tempsTransfert);
                terminerTransfert(balise, satellite);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void terminerTransfert(Balise balise, Satellite satellite) {
        System.out.println("Transfert terminé: " + balise.getId());
        balise.recommencerCollecte();
        satellite.setDisponible(true);
    }

    // Getters
    public List<Balise> getBalises() { return balises; }
    public List<Satellite> getSatellites() { return satellites; }
}
