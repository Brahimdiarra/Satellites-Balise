package src.main.java.com.oceanographie.events;


import src.main.java.com.eventHandler.AbstractEvent;
import src.main.java.com.oceanographie.model.Balise;

public class BaliseEvent extends AbstractEvent {
    private static final long serialVersionUID = 1L;

    private Balise balise;
    private Balise.EtatBalise ancienEtat;
    private Balise.EtatBalise nouvelEtat;

    public BaliseEvent(Balise balise, Balise.EtatBalise ancienEtat, Balise.EtatBalise nouvelEtat) {
        super(balise);
        this.balise = balise;
        this.ancienEtat = ancienEtat;
        this.nouvelEtat = nouvelEtat;
    }

    @Override
    public void sendTo(Object target) {
        if (target instanceof BaliseEventListener) {
            ((BaliseEventListener) target).onBaliseStateChanged(this);
        }
    }

    public Balise getBalise() {
        return balise;
    }

    public Balise.EtatBalise getAncienEtat() {
        return ancienEtat;
    }

    public Balise.EtatBalise getNouvelEtat() {
        return nouvelEtat;
    }
}