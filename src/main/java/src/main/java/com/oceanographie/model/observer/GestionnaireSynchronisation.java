package src.main.java.com.oceanographie.model.observer;

import src.main.java.com.eventHandler.EventHandler;
import src.main.java.com.oceanographie.events.SynchronisationEvent;
import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Satellite;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireSynchronisation implements Observateur {
    private List<Balise> balises;
    private List<Satellite> satellites;
    private EventHandler eventHandler;
    private long dureeTransfert = 1000; // 2 secondes par défaut

    public GestionnaireSynchronisation(EventHandler eventHandler) {
        this.balises = new ArrayList<>();
        this.satellites = new ArrayList<>();
        this.eventHandler = eventHandler;
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
    public void actualiser(Observable observable) {
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
        System.out.println("🔗 SYNCHRONISATION: " + balise.getId() +
                " ↔️ " + satellite.getId());

        // Changer les états
        balise.commencerTransfert(satellite);
        satellite.commencerTransfert(balise);

        // Envoyer événement de début de synchronisation
        SynchronisationEvent eventDebut = new SynchronisationEvent(
                balise,
                satellite,
                SynchronisationEvent.TypeSync.DEBUT
        );
        eventHandler.send(eventDebut);

        // Simuler le transfert avec un timer
        new Thread(() -> {
            try {
                System.out.println("📡 Transfert en cours... (" +
                        (dureeTransfert/1000) + " secondes)");
                Thread.sleep(dureeTransfert);

                terminerTransfert(balise, satellite);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void terminerTransfert(Balise balise, Satellite satellite) {
        System.out.println("✅ Transfert terminé: " + balise.getId() +
                " ↔️ " + satellite.getId());

        // Remettre les états normaux
        balise.terminerTransfert();
        satellite.terminerTransfert();

        // Envoyer événement de fin de synchronisation
        SynchronisationEvent eventFin = new SynchronisationEvent(
                balise,
                satellite,
                SynchronisationEvent.TypeSync.FIN
        );
        eventHandler.send(eventFin);
    }

    // ✅ NOUVELLE MÉTHODE - Définir la durée du transfert
    public void setDureeTransfert(long dureeMs) {
        this.dureeTransfert = dureeMs;
    }

    // Getters
    public List<Balise> getBalises() {
        return balises;
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }
}