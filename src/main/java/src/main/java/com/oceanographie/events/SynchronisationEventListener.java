package src.main.java.com.oceanographie.events;

public interface SynchronisationEventListener {
    void onSynchronisationDebut(SynchronisationEvent event);
    void onSynchronisationFin(SynchronisationEvent event);
}
