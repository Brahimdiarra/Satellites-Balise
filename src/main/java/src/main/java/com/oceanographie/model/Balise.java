package src.main.java.com.oceanographie.model;



public class Balise extends ElementMobile {
    private double profondeur;
    private EtatBalise etat;
    private long tempsCollecte;
    private long debutCollecte;
    private long dureeCollecte = 30000;

    public enum EtatBalise {
        COLLECTE,
        REMONTEE,
        EN_SURFACE,
        SYNCHRONISATION,
        TRANSFERT
    }

    public Balise(String id, Position position, double profondeur) {
        super(id, position, 2.0);
        this.profondeur = profondeur;
        this.etat = EtatBalise.COLLECTE;
        this.debutCollecte = System.currentTimeMillis();
    }

    @Override
    public void deplacer() {
        if (!actif) return;

        switch (etat) {
            case COLLECTE:
                // Appliquer stratégie de déplacement (Phase 2)
                verifierTempsCollecte();
                break;

            case REMONTEE:
                remonter();
                break;

            case EN_SURFACE:
                // Ne bouge pas
                break;

            case SYNCHRONISATION:
            case TRANSFERT:
                // Ne bouge pas pendant le transfert
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
    }

    public void recommencerCollecte() {
        this.debutCollecte = System.currentTimeMillis();
        changerEtat(EtatBalise.COLLECTE);
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
}