# Documentation des Patterns - Projet Satellites et Balises

## 📊 Liste des Diagrammes Générés

Tous les diagrammes UML ont été générés et sont disponibles dans le dossier outputs :

1. **pattern_strategie.png** - Pattern Stratégie pour les déplacements
2. **pattern_observer.png** - Pattern Observable/Observateur pour la synchronisation
3. **architecture_mvc.png** - Architecture MVC du projet
4. **diagramme_etat.png** - Machine à états de la Balise
5. **sequence_sync.png** - Séquence complète de synchronisation
6. **diagramme_classes_global.png** - Vue d'ensemble des classes principales

---

## 🎨 1. Pattern Stratégie (OBLIGATOIRE)

### 📖 Description
Le pattern Stratégie définit une famille d'algorithmes, les encapsule et les rend interchangeables. Il permet de varier l'algorithme indépendamment des clients qui l'utilisent.

### 🎯 Problème Résolu
Comment permettre aux balises d'avoir différents comportements de déplacement (horizontal, vertical, sinusoïdal, immobile) sans créer de nombreuses sous-classes et sans utiliser de gros blocs `if/else` ?

### ✅ Solution Implémentée

**Interface Strategy :**
```java
public interface StrategieDeplacementBalise {
    void appliquerDeplacement(Balise balise);
}
```

**Stratégies Concrètes :**
- `DeplacementHorizontal` : Déplacement latéral avec rebonds aux bords
- `DeplacementVertical` : Mouvement de haut en bas entre deux profondeurs
- `DeplacementSinusoidal` : Trajectoire en vague (horizontal + oscillation verticale)
- `DeplacementImmobile` : Balise stationnaire à profondeur fixe

**Contexte (Balise) :**
```java
public class Balise extends ElementMobile {
    private StrategieDeplacementBalise strategie;
    
    public void deplacer() {
        if (strategie != null) {
            strategie.appliquerDeplacement(this);
        }
    }
    
    // Changement dynamique possible
    public void setStrategie(StrategieDeplacementBalise strategie) {
        this.strategie = strategie;
    }
}
```

### 📊 Diagramme UML
Voir : **pattern_strategie.png**

### ✅ Avantages
- ✅ **Flexibilité** : Ajout facile de nouvelles stratégies sans modifier Balise
- ✅ **Changement dynamique** : Possibilité de changer le comportement à l'exécution
- ✅ **Open/Closed** : Ouvert à l'extension, fermé à la modification
- ✅ **Lisibilité** : Chaque stratégie est dans sa propre classe
- ✅ **Testabilité** : Chaque stratégie peut être testée indépendamment

### 🔬 Exemple d'Utilisation
```java
// Création avec stratégie horizontale
Balise b1 = new Balise("B-1", position, 100, 
                       new DeplacementHorizontal(2.0));

// Changement vers stratégie sinusoïdale
b1.setStrategie(new DeplacementSinusoidal(50, 0.1));
```

### 📝 Dans le Rapport
> "Le pattern Stratégie permet de définir plusieurs algorithmes de déplacement pour les balises océanographiques. Chaque stratégie (horizontale, verticale, sinusoïdale, immobile) est encapsulée dans une classe distincte implémentant l'interface `StrategieDeplacementBalise`. Cette approche permet de changer dynamiquement le comportement d'une balise sans modifier son code source, respectant ainsi le principe Open/Closed."

---

## 👁️ 2. Pattern Observable/Observateur (OBLIGATOIRE)

### 📖 Description
Le pattern Observable/Observateur définit une dépendance un-à-plusieurs entre objets : quand un objet (le sujet) change d'état, tous ses dépendants (observateurs) sont notifiés et mis à jour automatiquement.

### 🎯 Problème Résolu
Comment détecter automatiquement quand une balise arrive en surface pour tenter une synchronisation avec un satellite, sans créer un couplage fort entre les classes ?

### ✅ Solution Implémentée

**Interfaces du Pattern :**
```java
// Sujet Observable
public interface Observable {
    void ajouterObservateur(Observateur obs);
    void retirerObservateur(Observateur obs);
    void notifierObservateurs();
}

// Observateur
public interface Observateur {
    void actualiser(Observable observable);
}
```

**Observables Concrets :**
```java
// Balise notifie quand elle change d'état
public class Balise implements Observable {
    private List<Observateur> observateurs = new ArrayList<>();
    
    public void changerEtat(EtatBalise nouvelEtat) {
        this.etat = nouvelEtat;
        notifierObservateurs(); // ← Notification automatique
    }
    
    @Override
    public void notifierObservateurs() {
        for (Observateur obs : observateurs) {
            obs.actualiser(this);
        }
    }
}

// Satellite notifie quand sa disponibilité change
public class Satellite implements Observable {
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        notifierObservateurs();
    }
}
```

**Observateur Concret :**
```java
public class GestionnaireSynchronisation implements Observateur {
    @Override
    public void actualiser(Observable observable) {
        if (observable instanceof Balise) {
            Balise balise = (Balise) observable;
            if (balise.getEtat() == EtatBalise.EN_SURFACE) {
                tenterSynchronisation(balise);
            }
        }
    }
}
```

### 📊 Diagramme UML
Voir : **pattern_observer.png**

### ✅ Avantages
- ✅ **Couplage faible** : Les observables ne connaissent pas les détails des observateurs
- ✅ **Extensibilité** : Ajout facile de nouveaux observateurs
- ✅ **Communication automatique** : Pas besoin d'appeler manuellement les vérifications
- ✅ **Réactivité** : Détection immédiate des changements d'état
- ✅ **Séparation des responsabilités** : Chaque classe a un rôle clair

### 🔬 Flux de Communication
```
1. Balise remonte à la surface
2. Balise.changerEtat(EN_SURFACE)
3. Balise.notifierObservateurs()
4. GestionnaireSynchronisation.actualiser(balise)
5. Gestionnaire vérifie s'il y a un satellite disponible
6. Si oui → synchronisation
```

### 📝 Dans le Rapport
> "Le pattern Observable/Observateur gère la détection automatique des opportunités de synchronisation. Les balises et satellites jouent le rôle d'observables : lorsqu'une balise change d'état (arrive en surface) ou qu'un satellite devient disponible, ils notifient automatiquement le `GestionnaireSynchronisation` qui agit comme observateur. Ce pattern assure un couplage faible entre les composants et permet une réaction immédiate aux changements d'état."

---

## 🏗️ 3. Architecture MVC (Model-View-Controller)

### 📖 Description
MVC est un patron d'architecture qui sépare les données (Model), leur présentation (View) et la logique de traitement (Controller).

### 🎯 Problème Résolu
Comment organiser le code pour séparer la logique métier de l'affichage et faciliter la maintenance et les tests ?

### ✅ Solution Implémentée

**MODEL (Logique Métier) :**
- `Balise.java`, `Satellite.java`, `Position.java`
- `StrategieDeplacementBalise` et ses implémentations
- `GestionnaireSynchronisation.java`
- `Observable`, `Observateur`

**VIEW (Interface Graphique) :**
- `SimulationPanel.java` : Panel principal
- `VueBalise.java`, `VueSatellite.java` : Rendu des éléments
- `VueSynchronisation.java` : Visualisation des transferts
- `VueOcean.java` : Fond graphique
- `MainWindow.java` : Fenêtre principale

**CONTROLLER (Coordination) :**
- `SimulationController.java` : Coordonne Model et View
  - Gère la boucle de simulation (Timer)
  - Traite les événements utilisateur
  - Met à jour le modèle
  - Rafraîchit la vue

### 📊 Diagramme UML
Voir : **architecture_mvc.png**

### ✅ Avantages
- ✅ **Séparation des responsabilités** : Chaque couche a un rôle clair
- ✅ **Testabilité** : Le modèle peut être testé sans interface graphique
- ✅ **Maintenance** : Modifications de la vue sans toucher au modèle
- ✅ **Réutilisabilité** : Le modèle peut être utilisé avec différentes vues
- ✅ **Parallélisation** : Plusieurs développeurs peuvent travailler sur différentes couches

### 🔬 Flux de Données
```
User Input → Controller → Model (mise à jour) → Controller → View (affichage)
```

### 📝 Dans le Rapport
> "L'architecture MVC structure le projet en trois couches distinctes. Le Modèle contient toute la logique de simulation (déplacements, synchronisations, patterns Stratégie et Observer). La Vue gère l'affichage graphique avec Nicellipse, sans connaître les détails de la logique métier. Le Contrôleur (`SimulationController`) coordonne les deux : il fait tourner la simulation, récupère les changements du modèle et met à jour la vue en conséquence."

---

## 🔄 4. Pattern État (State)

### 📖 Description
Le pattern État permet à un objet de modifier son comportement lorsque son état interne change. L'objet semblera avoir changé de classe.

### 🎯 Problème Résolu
Comment gérer les différents comportements d'une balise selon sa phase du cycle (collecte, remontée, en surface, transfert) de manière claire et maintenable ?

### ✅ Solution Implémentée

**Enum des États :**
```java
public enum EtatBalise {
    COLLECTE,      // Sous l'eau, collecte des données
    REMONTEE,      // Monte vers la surface
    EN_SURFACE,    // Attend un satellite
    SYNCHRONISATION, // Connexion établie
    TRANSFERT      // Transfert de données en cours
}
```

**Machine à États dans Balise :**
```java
public class Balise {
    private EtatBalise etat;
    
    @Override
    public void deplacer() {
        switch (etat) {
            case COLLECTE:
                // Applique la stratégie de déplacement
                if (strategie != null) {
                    strategie.appliquerDeplacement(this);
                }
                verifierTempsCollecte();
                break;
                
            case REMONTEE:
                remonter();
                break;
                
            case EN_SURFACE:
                // Ne bouge pas, attend
                break;
                
            case TRANSFERT:
                // Ne bouge pas pendant le transfert
                break;
        }
    }
}
```

### 📊 Diagramme UML
Voir : **diagramme_etat.png**

### ✅ Transitions d'États
```
COLLECTE → REMONTEE (mémoire pleine)
REMONTEE → EN_SURFACE (surface atteinte)
EN_SURFACE → TRANSFERT (satellite détecté)
TRANSFERT → COLLECTE (transfert terminé)
```

### 🎨 Couleurs Associées
- **COLLECTE** : Cyan (🔵)
- **REMONTEE** : Jaune (🟡)
- **EN_SURFACE** : Vert (🟢)
- **TRANSFERT** : Rouge (🔴)

### ✅ Avantages
- ✅ **Clarté** : Le code reflète clairement les différents états
- ✅ **Maintenabilité** : Ajout facile de nouveaux états
- ✅ **Sécurité** : Transitions contrôlées entre états
- ✅ **Débogage** : Facile de tracer les changements d'état

### 📝 Dans le Rapport
> "Le pattern État modélise le cycle de vie d'une balise océanographique à travers cinq états distincts. Chaque état définit un comportement spécifique : en COLLECTE, la balise applique sa stratégie de déplacement et accumule des données ; en REMONTEE, elle monte vers la surface ; EN_SURFACE, elle attend un satellite ; et en TRANSFERT, elle transmet ses données. Les transitions entre états sont déclenchées par des événements (mémoire pleine, surface atteinte, satellite détecté)."

---

## 🔗 5. Communication entre Patterns (EventHandler)

### 📖 Description
Un système d'événements basé sur le pattern Observer (version avancée) permet la communication découplée entre le modèle et la vue.

### 🎯 Problème Résolu
Comment faire communiquer le modèle (backend) avec la vue (frontend) sans créer de dépendances directes ?

### ✅ Solution Implémentée

**Événements Personnalisés :**
```java
// Événement de synchronisation
public class SynchronisationEvent extends AbstractEvent {
    private Balise balise;
    private Satellite satellite;
    private TypeSync typeSync; // DEBUT ou FIN
    
    @Override
    public void sendTo(Object target) {
        if (target instanceof SynchronisationEventListener) {
            ((SynchronisationEventListener) target).onSynchronisationDebut(this);
        }
    }
}
```

**Flux de Communication :**
```
1. GestionnaireSynchronisation détecte une opportunité
2. Envoie un SynchronisationEvent via EventHandler
3. SimulationPanel (listener) reçoit l'événement
4. SimulationPanel change les couleurs des vues
5. Affiche la ligne de connexion
```

### ✅ Avantages
- ✅ **Découplage total** : Le modèle ne connaît pas la vue
- ✅ **Extensibilité** : Ajout facile de nouveaux listeners
- ✅ **Testabilité** : Le modèle peut être testé sans interface graphique
- ✅ **Communication asynchrone** : Pas de blocage

### 📝 Dans le Rapport
> "Le système EventHandler implémente une version avancée du pattern Observer pour découpler complètement le modèle de la vue. Lorsqu'une synchronisation commence, le `GestionnaireSynchronisation` envoie un `SynchronisationEvent` via l'EventHandler. Le `SimulationPanel`, enregistré comme listener, reçoit l'événement et met à jour l'affichage (changement de couleurs, affichage de la ligne de connexion) sans que le modèle ait besoin de connaître les détails de la vue."

---

## 📊 Diagramme de Séquence Complet

### 📖 Description
Le diagramme de séquence montre l'interaction entre tous les composants lors d'une synchronisation complète.

### 📊 Diagramme UML
Voir : **sequence_sync.png**

### 🔬 Étapes Détaillées

1. **Phase Collecte**
   - Balise applique sa stratégie de déplacement (Pattern Stratégie)
   - Collecte des données jusqu'à ce que la mémoire soit pleine

2. **Phase Remontée**
   - Balise change d'état vers REMONTEE
   - Monte progressivement vers la surface

3. **Notification (Pattern Observer)**
   - Balise arrive en surface
   - Change d'état vers EN_SURFACE
   - Notifie tous ses observateurs

4. **Détection**
   - GestionnaireSynchronisation reçoit la notification
   - Vérifie si un satellite est disponible
   - Vérifie si le satellite est au-dessus de la balise

5. **Synchronisation**
   - Si conditions remplies → synchronisation
   - Balise passe en état TRANSFERT
   - Satellite marque comme occupé
   - Les deux deviennent ROUGES (🔴)

6. **Communication avec la Vue (EventHandler)**
   - GestionnaireSynchronisation envoie SynchronisationEvent.DEBUT
   - SimulationPanel reçoit l'événement
   - Change les couleurs des vues
   - Affiche la ligne de connexion verte

7. **Transfert (20 secondes)**
   - Timer simule le transfert de données
   - Balise et Satellite restent immobiles et rouges

8. **Fin du Transfert**
   - GestionnaireSynchronisation envoie SynchronisationEvent.FIN
   - Balise vide sa mémoire
   - Balise redescend (état COLLECTE)
   - Satellite redevient disponible
   - Les couleurs reviennent à la normale

### 📝 Dans le Rapport
> "Le diagramme de séquence illustre l'orchestration de tous les patterns lors d'une synchronisation. On y voit le pattern Stratégie appliqué pour le déplacement, le pattern Observer pour la notification des changements d'état, le pattern État gérant le cycle de vie de la balise, et l'architecture MVC assurant la séparation entre logique métier et affichage via EventHandler."

---

## 📈 Vue d'Ensemble des Classes

### 📊 Diagramme UML
Voir : **diagramme_classes_global.png**

### 📖 Description
Ce diagramme montre l'intégration de tous les patterns dans la structure globale des classes.

### 🔗 Relations Importantes

1. **Héritage**
   - `Balise` et `Satellite` héritent de `ElementMobile`
   - Pattern Template Method implicite

2. **Implémentation d'Interfaces**
   - `Balise` implémente `Observable`
   - Stratégies implémentent `StrategieDeplacementBalise`
   - `GestionnaireSynchronisation` implémente `Observateur`

3. **Associations**
   - `Balise` utilise une `StrategieDeplacementBalise`
   - `Balise` notifie des `Observateur`
   - `GestionnaireSynchronisation` observe `Balise` et `Satellite`

### 📝 Dans le Rapport
> "Le diagramme de classes global montre comment tous les patterns s'intègrent dans une architecture cohérente. La classe `Balise` est au centre de trois patterns : elle utilise le pattern Stratégie pour son déplacement, implémente le pattern Observable pour notifier ses changements d'état, et utilise le pattern État pour gérer son cycle de vie. Cette conception modulaire facilite la maintenance et l'évolution du système."

---

## 📋 Récapitulatif pour le Rapport

### Tableau des Patterns

| Pattern | Objectif | Implémentation | Avantage Principal |
|---------|----------|----------------|-------------------|
| **Stratégie** | Varier le déplacement des balises | `StrategieDeplacementBalise` + 4 stratégies | Flexibilité et extensibilité |
| **Observable/Observateur** | Détection automatique des synchronisations | `Balise`/`Satellite` observables, `GestionnaireSynchronisation` observateur | Couplage faible |
| **État** | Gérer le cycle de vie des balises | Enum `EtatBalise` + switch dans `deplacer()` | Clarté du code |
| **MVC** | Séparer logique/affichage/contrôle | Packages distincts Model/View/Controller | Maintenabilité |
| **EventHandler** | Communication Model-View | `AbstractEvent` + listeners | Découplage total |

### Diagrammes à Inclure dans le Rapport

✅ **Obligatoires :**
1. `pattern_strategie.png` - Montre le pattern Stratégie
2. `pattern_observer.png` - Montre le pattern Observer
3. `diagramme_etat.png` - Machine à états de la Balise
4. `sequence_sync.png` - Séquence complète avec tous les patterns

✅ **Recommandés :**
5. `architecture_mvc.png` - Architecture globale
6. `diagramme_classes_global.png` - Vue d'ensemble

### Structure Proposée pour le Rapport

```markdown
# Partie 3 : Patterns de Conception

## 3.1 Pattern Stratégie
- Description théorique
- Problème résolu dans notre contexte
- Diagramme UML (pattern_strategie.png)
- Code source commenté
- Avantages de l'implémentation

## 3.2 Pattern Observable/Observateur
- Description théorique
- Problème résolu dans notre contexte
- Diagramme UML (pattern_observer.png)
- Code source commenté
- Flux de communication

## 3.3 Architecture MVC
- Description de l'architecture
- Organisation des packages
- Diagramme (architecture_mvc.png)
- Séparation des responsabilités

## 3.4 Pattern État
- Machine à états de la Balise
- Diagramme (diagramme_etat.png)
- Transitions et couleurs associées

## 3.5 Intégration des Patterns
- Diagramme de séquence (sequence_sync.png)
- Vue d'ensemble (diagramme_classes_global.png)
- Comment les patterns fonctionnent ensemble
```

Tous les diagrammes sont prêts et disponibles dans le dossier outputs ! 🎉
