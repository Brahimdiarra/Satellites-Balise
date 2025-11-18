# 🧪 Tests Unitaires - Simulation Satellites et Balises

## 📋 Vue d'ensemble

Ce projet contient des tests unitaires complets pour la simulation de satellites et balises océanographiques.

## 🗂️ Structure des tests

```
src/
├── test/
│   └── java/
│       └── com/
│           └── oceanographie/
│               ├── model/
│               │   ├── PositionTest.java        (Tests pour Position)
│               │   ├── BaliseTest.java          (Tests pour Balise)
│               │   └── SatelliteTest.java       (Tests pour Satellite)
│               └── view/
│                   └── component/
│                       ├── VueBaliseTest.java   (Tests pour VueBalise)
│                       └── VueSatelliteTest.java (Tests pour VueSatellite)
```

## 📦 Classes testées

### 1️⃣ **PositionTest.java**
Tests pour la classe Position :
- ✅ Création avec coordonnées valides
- ✅ Setters des coordonnées
- ✅ Position en surface et en profondeur
- ✅ Égalité de positions
- ✅ Coordonnées limites et négatives

### 2️⃣ **BaliseTest.java**
Tests pour la classe Balise :
- ✅ Création avec ID et position
- ✅ État initial (COLLECTE)
- ✅ Changements d'état (cycle complet)
- ✅ Modification de position
- ✅ Remontée vers la surface
- ✅ Balise en surface (Z = 0)
- ✅ ID unique et format

### 3️⃣ **SatelliteTest.java**
Tests pour la classe Satellite :
- ✅ Création avec ID, position et vitesse
- ✅ État disponible/occupé
- ✅ Déplacement horizontal
- ✅ Altitude constante
- ✅ Rebond aux limites de l'écran
- ✅ Vitesse de déplacement
- ✅ Arrêt du thread

### 4️⃣ **VueSatelliteTest.java**
Tests pour la vue du satellite :
- ✅ Création de la vue
- ✅ Mise à jour de la position
- ✅ Changement de disponibilité
- ✅ Dessin avec Graphics2D
- ✅ Multiples mises à jour
- ✅ Position dans les limites

### 5️⃣ **VueBaliseTest.java**
Tests pour la vue de la balise :
- ✅ Création de la vue
- ✅ Mise à jour de la position
- ✅ Changements d'état visuels
- ✅ Dessin avec différents états
- ✅ Remontée vers la surface
- ✅ Profondeur maximale

## 🚀 Exécution des tests

### Avec Gradle

```bash
# Exécuter tous les tests
./gradlew test

# Exécuter une classe de test spécifique
./gradlew test --tests BaliseTest

# Exécuter avec rapport détaillé
./gradlew test --info

# Générer un rapport HTML
./gradlew test
# Le rapport sera dans: build/reports/tests/test/index.html
```

### Avec IntelliJ IDEA

1. Clic droit sur le dossier `src/test/java`
2. Sélectionner **"Run 'All Tests'"**

Ou pour un test spécifique :
1. Ouvrir le fichier de test
2. Cliquer sur la flèche verte à côté de la classe ou méthode
3. Sélectionner **"Run"**

### Avec Maven (si tu utilises Maven)

```bash
# Exécuter tous les tests
mvn test

# Exécuter une classe spécifique
mvn test -Dtest=BaliseTest

# Avec rapport détaillé
mvn test -X
```

## 📊 Couverture des tests

Les tests couvrent :
- **Modèles** : Position, Balise, Satellite
- **Vues** : VueBalise, VueSatellite
- **États** : Tous les états de la balise (COLLECTE, REMONTEE, EN_SURFACE, SYNCHRONISATION, TRANSFERT)
- **Mouvements** : Déplacements, remontées, rebonds
- **Affichage** : Rendu graphique avec Graphics2D

## 🔧 Configuration requise

### Dépendances (ajoutées dans build.gradle)

```gradle
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.3'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.3'
    testImplementation 'org.junit.jupiter:junit-jupiter-params:5.9.3'
}

test {
    useJUnitPlatform()
}
```

## 📝 Conventions de test

### Nomenclature
- Classe de test : `NomClasseTest.java`
- Méthode de test : `testComportementAttendu()`
- Annotation : `@DisplayName("Description claire")`

### Structure d'un test
```java
@Test
@DisplayName("Description du comportement testé")
void testNomMethode() {
    // Arrange (préparation)
    Position position = new Position(100, 200, -50);
    
    // Act (action)
    double z = position.getZ();
    
    // Assert (vérification)
    assertEquals(-50, z);
}
```

## 🐛 Déboguer les tests qui échouent

### Test qui échoue
```bash
# Lancer avec stack trace complète
./gradlew test --stacktrace

# Ou avec plus de détails
./gradlew test --debug
```

### Logs détaillés
Les tests affichent des informations dans la console. Active les logs :
```java
testLogging {
    showStandardStreams = true  // Dans build.gradle
}
```

## ✅ Bonnes pratiques

1. **Indépendance** : Chaque test doit être indépendant
2. **Setup/Teardown** : Utiliser `@BeforeEach` et `@AfterEach`
3. **Clarté** : Noms de tests explicites
4. **Isolation** : Ne pas dépendre de l'ordre d'exécution
5. **Rapidité** : Tests rapides (< 1 seconde chacun)

## 📈 Amélioration continue

### Ajouter un nouveau test
```java
@Test
@DisplayName("Test du nouveau comportement")
void testNouveauComportement() {
    // Arrange
    // Act
    // Assert
}
```

### Tests paramétrés
```java
@ParameterizedTest
@ValueSource(doubles = {-100.0, -200.0, -300.0})
@DisplayName("Test avec différentes profondeurs")
void testDifferentesProfondeurs(double profondeur) {
    Position pos = new Position(100, 0, profondeur);
    assertTrue(pos.getZ() < 0);
}
```

## 🎯 Objectifs de couverture

- ✅ **90%+** de couverture des classes métier
- ✅ **80%+** de couverture des vues
- ✅ **100%** des cas limites testés

## 📞 Support

En cas de problème avec les tests :
1. Vérifier que JUnit 5 est bien configuré
2. Nettoyer le build : `./gradlew clean`
3. Reconstruire : `./gradlew build`
4. Relancer les tests : `./gradlew test`

## 🎓 Ressources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Gradle Testing Guide](https://docs.gradle.org/current/userguide/java_testing.html)

---

**Bonne chance avec tes tests ! 🚀**
