package src.main.java.com.oceanographie.events;

import src.main.java.com.eventHandler.AbstractEvent;
import src.main.java.com.oceanographie.model.Satellite;

public class SatelliteEvent extends AbstractEvent {
    private static final long serialVersionUID = 1L;

    private Satellite satellite;
    private boolean disponible;

    public SatelliteEvent(Satellite satellite, boolean disponible) {
        super(satellite);
        this.satellite = satellite;
        this.disponible = disponible;
    }

    @Override
    public void sendTo(Object target) {
        if (target instanceof SatelliteEventListener) {
            ((SatelliteEventListener) target).onSatelliteDisponibiliteChanged(this);
        }
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public boolean isDisponible() {
        return disponible;
    }
}