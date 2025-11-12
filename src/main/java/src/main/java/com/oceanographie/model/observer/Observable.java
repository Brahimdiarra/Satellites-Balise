package src.main.java.com.oceanographie.model.observer;

public interface Observable {
    void ajouterObservateur(Observateur obs);
    void retirerObservateur(Observateur obs);
    void notifierObservateurs();
}