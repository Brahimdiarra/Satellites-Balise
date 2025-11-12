package src.main.java.com.oceanographie.events;


import src.main.java.com.eventHandler.AbstractEvent;
import src.main.java.com.oceanographie.model.Balise;
import src.main.java.com.oceanographie.model.Satellite;

public class SynchronisationEvent extends AbstractEvent {
    private static final long serialVersionUID = 1L;

    public enum TypeSync {
        DEBUT,
        EN_COURS,
        FIN
    }

    private Balise balise;
    private Satellite satellite;
    private TypeSync typeSync;
    private long timestamp;

    public SynchronisationEvent(Balise balise, Satellite satellite, TypeSync typeSync) {
        super(balise);
        this.balise = balise;
        this.satellite = satellite;
        this.typeSync = typeSync;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void sendTo(Object target) {
        if (target instanceof SynchronisationEventListener) {
            SynchronisationEventListener listener = (SynchronisationEventListener) target;
            switch (typeSync) {
                case DEBUT:
                    listener.onSynchronisationDebut(this);
                    break;
                case FIN:
                    listener.onSynchronisationFin(this);
                    break;
            }
        }
    }


    public Balise getBalise() { return balise; }
    public Satellite getSatellite() { return satellite; }
    public TypeSync getTypeSync() { return typeSync; }
    public long getTimestamp() { return timestamp; }
}
